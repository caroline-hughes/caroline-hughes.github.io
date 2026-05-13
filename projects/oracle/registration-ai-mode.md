# Registration AI Mode

End-to-end reimagining of patient registration using agent-driven workflows, conversational interfaces, and rapid AI-assisted development.

<p><span class="project-pill project-pill-python">Python</span> <span class="project-pill project-pill-kafka">Kafka</span> <span class="project-pill project-pill-frontend">React/TypeScript</span> <span class="project-pill project-pill-agentic">Agentic</span></p>

## What I built

A set of **AI-native registration experiences** that replaced traditional form-based workflows with agent-driven interactions.

-   built **3 end-to-end prototypes** of registration flows
-   supported:
    -   chat-based registration
    -   hybrid (form + chat) interaction
    -   voice input and OCR-based document scanning
-   powered by **Python agents** that executed registration logic
-   integrated with **Semantic Index (SI)** to create real patient records

The result was a fully working system where a patient could be registered through an AI interface and persisted into production systems.

---

## Why it mattered

Traditional registration flows are:

-   rigid, form-heavy, and time-consuming
-   difficult to adapt to different workflows
-   not intuitive for all users (especially in high-stress environments like hospitals)

We wanted to explore:

-   how AI could **simplify and accelerate registration**
-   whether conversational interfaces could replace or augment forms
-   how to make workflows more **flexible and human-friendly**

AI Mode was an exploration of what a **next-generation registration experience** could look like.

---

## What we explored

We built three distinct UX approaches:

**1. Hybrid (Form + AI)**

-   users could fill out forms manually
-   or use chat to populate fields dynamically

**2. Fully conversational (Chat-first)**

-   no forms — entire flow driven through chat
-   agent guided the user step-by-step

**3. Multimodal enhancements**

-   voice-to-text input
-   OCR-based scanning of insurance cards and documents

These prototypes helped compare usability, speed, and flexibility across approaches.

---

## How it worked

### Architecture overview

-   frontend communicated via chat interface
-   messages published to a **Kafka event bus**
-   Python agent consumed events and executed workflow logic
-   agent interacted with:
    -   Semantic Index (patient create / lookup)
    -   LLM (OpenAI) for reasoning + interpretation

---

### Agent design

The backend was built around **agent-based workflows**:

-   agents had a **knowledge base of registration steps**
-   logic defined in **plain English rules** (not hardcoded flows)
-   could dynamically:
    -   decide next steps
    -   validate inputs
    -   call downstream services

This made flows:

-   flexible
-   easy to extend
-   closer to how humans reason about workflows

---

### AI-assisted development

The system was built using **Codex + MCP tooling**, enabling rapid end-to-end development.

-   PDF Reader MCP → ingested full product requirements
-   Figma MCP → generated frontends from wireframes
-   AGENTS.md → enabled fast onboarding, debugging, and consistent architecture

Multiple full-stack prototypes were built in **~1–2 weeks each**.

---

## My impact

-   co-designed and built all three AI Mode prototypes end-to-end
-   implemented agent-based backend architecture (Python + Kafka)
-   integrated SI + LLM into registration workflows
-   led Demo Day presentation to org-wide audience
-   gave internal session on optimizing Codex for full-stack development

→ Result: demonstrated a viable path toward AI-native workflows and influenced how the org approaches intelligent systems
