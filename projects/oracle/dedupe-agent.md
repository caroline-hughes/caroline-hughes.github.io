# Patient Dedupe Agent

A system for detecting potential duplicate patient records during registration.

<p><span class="project-pill project-pill-python">Python</span> <span class="project-pill project-pill-agentic">Agentic</span> <span class="project-pill project-pill-search">Semantic Index</span></p>

## What I built

A Python agent that identifies potential duplicate patients by querying the Semantic Index using matching rules written in plain-English markdown.

-   recieved info about a patient to be registered, uses its tools to compute the blocking key, and check it against Semantic Index (database)
-   ingested markdown-defined duplicate criteria (for example, combinations like first name + last name + date of birth)
-   deployed as an independent service and registered in an internal agent registry
-   callable by other agents/services (e.g. registration flow)
-   returned potential duplicate matches in real time

This allowed duplicate detection to be handled as a reusable, intelligent service.

---

## Why it mattered

Duplicate patient records pose major issues for healthcare systems:

-   fragmented medical histories
-   increased risk of medical errors
-   operational inefficiencies

We needed:

-   a scalable, automated way to detect duplicates
-   integration directly into the registration workflow
-   flexibility to change/improve matching logic over time

The Dedupe Agent solved this by providing intelligent duplicate detection whose matching logic could be defined in plain English in a markdown file, then interpreted by the agent at runtime.

---

## How it worked

```mermaid
%%{init: {'flowchart': {'curve': 'basis'}}}%%
flowchart LR
    H([Human Editor])
    R[[Registration Service]]
    K[[Kafka Message Bus]]
    SI[("Semantic Index")]

    subgraph D["Dedupe Agent"]
        direction TB
        BR[/blocking-rules.md/]
        A[[Agent Runtime]]
    end

    H -->|"read / write"| BR
    BR -->|"duplicate-check rules"| A
    R <--> K
    K <--> A
    A -->|"soundsLike(first_name) + soundsLike(last_name) + date_of_birth"| SI

    classDef service fill:#DBEAFE,stroke:#2563EB,stroke-width:1.5px,color:#172033
    classDef core fill:#DBEAFE,stroke:#2563EB,stroke-width:1.5px,color:#172033
    classDef storage fill:#FFF3E0,stroke:#FB8C00,stroke-width:1px,color:#172033
    classDef client fill:#F3E8FF,stroke:#8B5CF6,stroke-width:1.5px,color:#172033

    class R,K service
    class A core
    class SI storage
    class H,BR client
    style D fill:#F8FAFC,stroke:#60A5FA,stroke-width:2px,stroke-dasharray: 8 4,rx:18px,ry:18px
```

1. duplicate-check rules were authored in markdown using plain-English criteria
2. while a user was actively entering patient registration information, the registration service invoked the Dedupe Agent by publishing to a Kafka bus
3. the agent, deployed as an independent service in the same cluster and registered in a central registry, interpreted those rules into Semantic Index queries
    - "similar first name, similar last name, same DOB" -> `soundsLike(first_name) + soundsLike(last_name) + date_of_birth`
    - "same first initial, same last name, same phone number, same zip code" -> `first_initial(first_name) + last_name + phone_number + zip_code`
4. those criteria were used as **blocking keys** to narrow candidate matches
5. the Semantic Index served as the data layer for patient records and supported the fuzzy matching needed to retrieve potential duplicates
6. candidate duplicates were returned and displayed to the administrator through a clean service interface

<!-- → Result: reduced risk of duplicate patient records and improved data integrity during registration -->
