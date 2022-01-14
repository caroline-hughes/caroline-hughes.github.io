import React, { useEffect, useRef } from 'react';
import moment from 'moment';
import { UserContext } from '../../userContext';
import { useFleetData } from '../../fleetContext';
import useWebsocketClient from '../../internal/websocketClient';

import { findVehInData, setCurrUpdate, getVehicleUpdates } from '../live/fleetmap/mapUtils';

const BUFFER = 600; // If not live, get the next 10 minutes worth of vehicle data each buffer
const RETRIEVE_BEFORE = 60; // Get more data 1 minute before the end of the buffer

const LIVE_BUFFER = 40; // If live, get 40 seconds worth of vehicle data the first frame
const NEXT_UPDATE = 5; // Each subsequent frame, get the next 5 seconds worth of data
const LIVE_RETRIEVE_BEFORE = 10; // Get more data 10 seconds before the end of the buffer

// useVehicleAnimation controls vehicle animation on the live map through a frame requesting loop
export default function useVehicleAnimation(
  viewport,
  time,
  live,
  sliding,
  animationPlaying,
  setVehUpdates,
  animationSpeed,
  setLoading,
) {
  const { apiKey } = React.useContext(UserContext);
  const { fleetKey } = useFleetData();

  const wsClient = useWebsocketClient(import.meta.env.VITE_REALTIME_URL);

  const prevTimestamp = useRef(0); // Unix timestamp of previous frame in ms
  const curFrame = useRef(0); // Unique reference to the current frame

  const speed = useRef(animationSpeed);
  const playing = useRef(animationPlaying);

  // Listens for change in speed
  useEffect(() => {
    speed.current = animationSpeed;
  }, [animationSpeed]);

  // Listens for slider play/pause event
  useEffect(() => {
    playing.current = animationPlaying;
  }, [animationPlaying]);

  // Initializes the frame requesting loop
  useEffect(() => {
    setLoading(true);
    let firstFrame = true;
    let previouslyEmpty = true;
    let animationTime = time.clone();

    const retrieveBefore = live ? LIVE_RETRIEVE_BEFORE : RETRIEVE_BEFORE;
    let nextBufferPeriod = live ? LIVE_BUFFER : BUFFER;

    const buffer = {
      data: [],
    };
    setBufferBounds();

    const frame = async (timestamp) => {
      if (wsClient && !sliding) {
        const timeDelta = timestamp - prevTimestamp.current;

        // Check if animation time is near the end of the current buffer period
        const nearBufferEnd = buffer.end.diff(animationTime, 'seconds') <= retrieveBefore;

        // If the slider is not paused, increment the animation time
        if (playing.current) {
          animationTime.add(speed.current * timeDelta, 'ms');
        }

        // If live animation has been in background for >30s, need to reset data retrieval bounds
        if (live && pageWasInBackground()) {
          setBufferBounds();
        }

        if (nearBufferEnd) {
          buffer.start = buffer.end.clone();
          buffer.end.add(nextBufferPeriod, 'seconds');
        }

        if (firstFrame || nearBufferEnd) {
          // Increment bounds of buffer if reached the end
          await getVehicleUpdates(
            apiKey,
            fleetKey,
            buffer.start.toISOString(),
            nextBufferPeriod,
            wsClient,
            (newData) => setBufferData(newData, animationTime),
          );
        }

        // Set which data from buffer state should be displayed for the current frame
        setCurrUpdate(viewport, buffer.data, animationTime, setVehUpdates);

        // Subsequent buffers will only be 5 seconds if the animation is live
        nextBufferPeriod = live ? NEXT_UPDATE : BUFFER;

        prevTimestamp.current = timestamp;
        setLoading(false);
        firstFrame = false;
      }
      curFrame.current = requestAnimationFrame(frame);
    };

    // A large difference between the animation time and buffer start indicates the page
    // was previously in the background and the buffer bounds are no longer aligned with time.
    function pageWasInBackground() {
      return !firstFrame && Math.abs(animationTime.diff(buffer.start, 'seconds')) > 30;
    }

    // Set the state of the current data
    function setBufferData(nextData, curTime) {
      // On mount, ensure the animation time reflects the current moment
      if (live && nextData.length && previouslyEmpty) {
        animationTime = moment().subtract(20, 'seconds');
        setBufferBounds();
        previouslyEmpty = false;
      }

      let res = [];
      if (!buffer.data.length) {
        res = nextData;
      } else {
        // Update data of vehicles in current buffer
        buffer.data.forEach((veh) => {
          const d = veh;
          const lowerBound = curTime.clone().subtract(10, 'seconds').toISOString();
          // Trim updates at least 10 seconds in the past
          d.updates = veh?.updates?.filter((u) => u?.time > lowerBound);

          const matchingVeh = findVehInData(nextData, veh);
          // Assign the schedule to be the updated schedule
          d.schedule = matchingVeh?.schedule;
          // Append updates from new updates payload
          d.updates = veh?.updates?.concat(matchingVeh?.updates || []);

          if (d.updates?.length || d.schedule?.length) {
            res.push(d);
          }
        });

        // Add data of vehicles which are present in the next buffer and not the current
        nextData.forEach((veh, i) => {
          if (!findVehInData(buffer.data, veh)) {
            res.push(nextData[i]);
          }
        });
      }
      buffer.data = res;
    }

    // Set the time bounds for which to query for updates
    function setBufferBounds() {
      buffer.start = animationTime.clone().subtract(20, 'seconds');
      buffer.end = buffer.start.clone().add(nextBufferPeriod, 'seconds');
    }

    // Kick off the recursive loop
    curFrame.current = requestAnimationFrame(frame);

    // Cleanup when the parent component unmounts
    return () => cancelAnimationFrame(curFrame.current);
  }, [apiKey,
    fleetKey,
    live,
    time,
    wsClient,
    setVehUpdates,
    setLoading,
    sliding,
    viewport]);

  // This hook returns nothing, as it uses the parent state setter setVehUpdates to alter state
  return null;
}
