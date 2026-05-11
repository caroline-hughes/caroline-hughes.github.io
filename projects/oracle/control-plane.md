# Control Plane

Platform for provisioning and managing tenant-specific infrastructure in a multi-tenant healthcare system.

## What I built

A control plane service responsible for automating the setup of new client (hospital) environments in Oracle Cloud.

-   orchestrated infrastructure provisioning using **Workflow as a Service (WFaaS)**
-   leveraged internal services like **Kiev as a Service (KaaS)** for resource lifecycle management
-   exposed APIs for triggering and monitoring provisioning workflows
-   acted as the bridge between application-level requests and cloud infrastructure

This allowed new tenants to be provisioned programmatically instead of manually.

---

## Why it mattered

Onboarding a new hospital required:

-   creating cloud infrastructure (VCNs, subnets, databases, etc.)
-   configuring services and environments
-   ensuring everything was wired together correctly

Previously, this process was:

-   manual and error-prone
-   slow to execute
-   difficult to scale across many tenants

We needed a system that could:

-   standardize environment creation
-   reduce human intervention
-   safely scale to many hospital deployments

The Control Plane solved this by turning provisioning into a **repeatable, automated workflow**.

---

## How it worked

### Core concepts

**1. API layer (Control Plane Service)**

-   received requests to provision new tenant environments
-   validated input and initiated workflows

**2. Workflow orchestration (WFaaS)**

-   defined multi-step provisioning flows
-   handled sequencing, retries, and failure handling
-   orchestrated tasks like network setup, compute provisioning, and service deployment

**3. Resource provisioning (KaaS + OCI)**

-   interacted with underlying infrastructure services
-   created and configured cloud resources in OCI

---

### Execution flow

1. request to provision a new tenant is sent to Control Plane
2. Control Plane triggers a workflow via WFaaS
3. workflow executes a series of infrastructure provisioning steps
4. KaaS / OCI services create resources (networking, compute, etc.)
5. workflow completes and returns status

---

## My impact

-   contributed to design and implementation of the control plane service
-   integrated with WFaaS to define provisioning workflows
-   helped model infrastructure provisioning as structured, repeatable processes
-   worked through environment-specific challenges across tenants and regions

→ Result: enabled scalable, reliable onboarding of new hospital clients through automated infrastructure provisioning
