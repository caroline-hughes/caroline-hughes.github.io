# Control Plane

An automated cloud tenant provisioning layer.

<p><span class="project-pill project-pill-java">Java</span> <span class="project-pill project-pill-oci">OCI</span> <span class="project-pill project-pill-terraform">Terraform</span></p>

## What I built

A control plane that provisions and configures all cloud and app resources for a new tenant (environment) of a multi-tenant healthcare client.

-   orchestrated infrastructure provisioning using **Workflow as a Service (WFaaS)**
-   leveraged **Kiev<sup class="footnote-ref"><a href="#fn-kiev">1</a></sup> as a Service (KaaS)** for resource lifecycle management
-   exposed APIs for triggering and monitoring provisioning workflows
-   acted as the bridge between application-level requests and cloud infrastructure

---

## Why it mattered

Previously, our infra team spent 15+ hours per week initializing cloud resources for new tenants..

-   updating terraform config files
-   deploying the new envs, and
-   verifying all cloud resources were created (VCNs, subnets, databases, IAM policies etc.)
-   ensuring everything was wired together correctly

We needed a system that could:

-   standardize environment creation
-   reduce human intervention
-   safely scale to many hospital deployments

The control plane allowed new tenants to be provisioned programmatically instead of manually. It turned provisioning into a **repeatable, automated workflow**.

---

## How it worked

[[diagram-float-right]]

```mermaid
%%{init: {
  'theme': 'base',
  'themeVariables': {
    'actorBkg': '#DBEAFE',
    'actorBorder': '#2563EB',
    'actorTextColor': '#172033',
    'actorLineColor': '#60A5FA',
    'signalColor': '#2563EB',
    'signalTextColor': '#172033',
    'labelBoxBkgColor': '#EFF6FF',
    'labelBoxBorderColor': '#60A5FA',
    'labelTextColor': '#172033',
    'noteBkgColor': '#FFF3E0',
    'noteBorderColor': '#FB8C00',
    'noteTextColor': '#172033',
    'activationBkgColor': '#EFF6FF',
    'activationBorderColor': '#60A5FA',
    'sequenceNumberColor': '#172033'
  }
}}%%
sequenceDiagram
    actor U as Operator / Internal Service
    participant CP as Control Plane API
    participant K as Kiev / KaaS
    participant W as WFaaS Workflow
    participant OCI as OCI Services
    participant T as Tenant Environment

    U->>CP: Request new tenant provisioning
    CP->>CP: Validate request + tenant metadata
    CP->>K: Create lifecycle record<br/>(tenantName, phase = REQUESTED)
    CP->>W: Start provisioning workflow
    W-->>CP: Return workflow id
    CP-->>U: Return provisioning accepted

    loop Provisioning workflow steps
        W->>K: Update tenant phase
        W->>OCI: Provision required resources
        OCI-->>W: Resource created / configured
    end

    W->>T: Tenant environment ready
    W->>K: Update tenant phase = ACTIVE

    U->>CP: Check provisioning status
    CP->>K: Read tenant phase + workflow state
    K-->>CP: ACTIVE
    CP-->>U: Tenant ready
```

Provisioning was modeled as a workflow-driven system spanning three layers:

-   **Control Plane API:** received tenant provisioning requests, validated metadata, and kicked off workflows
-   **WFaaS workflow:** orchestrated multi-step provisioning, including sequencing, retries, and failure handling
-   **OCI + KaaS:** OCI created the actual cloud resources, while KaaS tracked tenant lifecycle state across phases

[[flow-clear]]

---

<!-- ## My impact

-   contributed to design and implementation of the control plane service
-   integrated with WFaaS to define provisioning workflows
-   helped model infrastructure provisioning as structured, repeatable processes
-   worked through environment-specific challenges across tenants and regions -->
<!--
→ Result: enabled scalable, reliable onboarding of new hospital clients through automated infrastructure provisioning -->

<details class="footnotes-block" id="control-plane-notes">
<summary>Notes</summary>

<ol class="footnote-list">
  <li id="fn-kiev">
    Kiev is a NoSQL key-value store that supports mini-transactions for convenience. Here, it was used for resource lifecycle management. Once the workflow begins, a <code>{tenantName -> currentPhase}</code> entry can be stored in Kiev. As the workflow progresses, the tenant's phase is updated. This tracking supports workflow visibility and idempotency.
  </li>
</ol>
</details>
