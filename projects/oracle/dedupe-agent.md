# Patient Dedupe Agent

System for detecting potential duplicate patient records during registration.

<p><span class="project-pill project-pill-python">Python</span> <span class="project-pill project-pill-agentic">Agentic</span> <span class="project-pill project-pill-search">Semantic Index</span></p>

## What I built

A Python agent that identifies potential duplicate patients by querying the Semantic Index using fuzzy matching techniques.

-   implemented as part of an **agent-oriented architecture**
-   deployed as an independent service and registered in an internal agent registry
-   callable by other agents/services (e.g. registration flow)
-   returned potential duplicate matches in real time

This allowed duplicate detection to be handled as a reusable, intelligent service.

---

## Why it mattered

Duplicate patient records are a major issue in healthcare systems:

-   lead to fragmented medical histories
-   increase risk of medical errors
-   create operational inefficiencies

Previously, duplicate detection was:

-   inconsistent or manual
-   limited in scope
-   difficult to standardize across systems

We needed:

-   a scalable, automated way to detect duplicates
-   integration directly into the registration workflow
-   flexibility to improve matching logic over time

The Dedupe Agent solved this by providing **real-time, intelligent duplicate detection**.

---

## How it worked

### Core concepts

**1. Blocking + matching strategy**

-   queried the Semantic Index using keys like:
    -   `soundsLike(first_name)`
    -   `soundsLike(last_name)`
    -   `date_of_birth`
-   used these as a **blocking key** to narrow candidate matches

**2. Semantic Index (SI)**

-   served as the data layer for patient records
-   supported efficient querying and fuzzy matching

**3. Agent architecture**

-   agent registered in a central registry
-   callable by other services/agents
-   encapsulated duplicate detection logic behind a clean interface

---

### Execution flow

1. user begins patient registration
2. registration service invokes the Dedupe Agent
3. agent queries SI for potential matches using blocking keys
4. candidate duplicates are returned
5. UI displays possible matches to the administrator

---

## My impact

-   designed and built the agent end-to-end independently
-   implemented efficient querying strategies against the Semantic Index
-   integrated the agent into the registration workflow
-   deployed and validated the system in a real environment

→ Result: reduced risk of duplicate patient records and improved data integrity during registration
