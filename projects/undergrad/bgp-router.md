# BGP Router

A simplified [Border Gateway Protocol (BGP)](https://en.wikipedia.org/wiki/Border_Gateway_Protocol) router implemented in Python for a simulated network environment. The router maintains routes from multiple neighbors, processes updates and withdrawals, applies longest-prefix matching and BGP tie-breaking rules, and forwards packets while enforcing customer / provider / peer policy constraints.

This one stands out because it is more than a socketing exercise. It combines protocol handling, route selection logic, table maintenance, policy enforcement, and route aggregation into a single system.

## Highlights

-   Multi-neighbor communication over UDP sockets
-   Processing of update, withdraw, dump, and data messages
-   Longest-prefix match and full BGP-style tie-breaking
-   Route aggregation and deaggregation support
-   Valley-free forwarding and announcement policy enforcement

[View on GitHub](https://github.com/caroline-hughes/bgp-router#readme)
