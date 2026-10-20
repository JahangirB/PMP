# Question ledger — business-3 · Change control

**What this is for.** No two questions in the app may repeat a scenario. The
app's audit only catches identical stems, so two questions can share a
situation, a lesson and a correct answer in different words and both pass.
This ledger is the check that closes that gap: before writing a question for a
task, read that task's rows and confirm your situation is not already there.
The obvious scenario for every task is long gone; go looking for the
second-order one.

**How the gists are made.** Mechanically, by `tools/ledger.mjs`: the question
sentence is dropped and the situation is kept verbatim up to about 130
characters. Matching and drop-down rows list their prompts instead. The full
item is findable by its id. **Source** says where it lives: `core` is built
into the app, `case-NN` is a case study, anything else is a file in `packs/`.

Generated — do not edit by hand. Run `node tools/ledger.mjs` after changing
any question.

---

### business-3 · Change control — 115 items

*Route changes through a controlled process and keep records aligned.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b3-001`|core|single|predictive|2|On a warehouse fit-out, the change control board has approved a request from the client's operations director to add a second…|
|`pmp-b3-002`|core|single|agile|3|A finance director on the steering committee of an agile customer-portal rebuild asks why the project needs change control at…|
|`pmp-b3-003`|core|hotspot|predictive|2|The diagram shows your project's change control sequence. A developer has begun building a requested change as soon as the impact…|
|`pmp-b3-004`|core|single|agile|3|Your team works from a prioritised backlog under a contract that pays for a fixed number of sprints. The client's product owner…|
|`pmp-b3-005`|core|multi|hybrid|2|Your programme has a plan-driven hardware workstream under formal change control and a software team working from a backlog the…|
|`pmp-b3-006`|core|single|predictive|2|A failure in a live payroll interface will stop three thousand staff being paid on Friday unless a configuration change is made…|
|`pmp-b3-007`|core|single|hybrid|2|An internal audit of your programme finds three changes that were properly requested, assessed and approved by the change board…|
|`pmp-b3-021`|core|single|agile|3|Your fixed-price agile contract lets the client swap backlog items of equal estimated size. At sprint planning, the client's…|
|`pmp-b3-022`|core|single|agile|2|Mid-sprint, a marketing manager asks a developer directly to change how a promotion banner behaves. The developer agrees and…|
|`pmp-b3-023`|core|single|agile|4|Your product owner accepts a major change of direction that would make three months of completed work obsolete. The budget and…|
|`pmp-b3-024`|core|single|hybrid|3|An approved change altered your platform's external interface. The team implemented and released it, but the interface…|
|`pmp-b3-025`|core|multi|agile|3|A stakeholder asks your product team to add a new payment method to an online shop before the next release. Select two.|
|`pmp-b3-026`|core|single|hybrid|4|The change board has rejected a request to extend a hybrid programme's data warehouse. A week later, the sponsor's deputy asks…|
|`pmp-case1-003`|case-01|single|predictive|4|The civil contractor has started additional piling under filter bank 3 on your site supervisor's instruction, and has submitted a…|
|`pmp-b3-101`|business-3|single|predictive|4|You manage a billing replacement for a regional water utility. The change management plan lets you approve changes under £15,000;…|
|`pmp-b3-102`|business-3|single|agile|1|Your agile team builds a claims-handling app for an insurer, and a product owner orders its backlog. The sales director has…|
|`pmp-b3-103`|business-3|single|hybrid|4|A logistics programme combines a predictive warehouse-automation build with three agile software teams. The change management…|
|`pmp-b3-104`|business-3|single|predictive|3|On a €9 million school extension, the cladding subcontractor proposes replacing the specified façade fixings with another brand.…|
|`pmp-b3-105`|business-3|single|agile|3|Three sprints into a six-sprint release of a payments app, a near miss leads the team and product owner to add automated security…|
|`pmp-b3-106`|business-3|matching|predictive|2|Prompts: The client's engineer asks which revisio… / The change board wants to know, before i… / The sponsor asks how many change request…|
|`pmp-b3-107`|business-3|single|hybrid|3|Your hybrid project for a county council is implementing a cloud HR platform. The configuration design was signed off under a…|
|`pmp-b3-108`|business-3|multi|predictive|3|You are the supplier's project manager on a fixed-price contract to build a booking system for a ferry operator. In a design…|
|`pmp-b3-109`|business-3|single|agile|3|An online grocer's mobile team releases every two weeks, and a national launch campaign begins in six weeks. The developers ask…|
|`pmp-b3-110`|business-3|hotspot|predictive|2|A telecoms fibre-rollout project has revised its plan twice. CR-07, approved in March, re-issued the schedule and cost baselines.…|
|`pmp-b3-111`|business-3|fillblank|hybrid|2|On a hybrid airport baggage-system upgrade, product owners may approve changes within a release from a €120,000 change pool. Any…|
|`pmp-b3-112`|business-3|pulldown|agile|3|Prompts: Mid-sprint, the developers find that the… / On day three of a sprint, a new fares re… / Customers using a journey-planner screen…|
|`pmp-b3-113`|business-3|matching|hybrid|4|Prompts: Seat-frame welds from the new jig are al… / The entertainment team's burn-up shows i… / The seat supplier has renumbered the sea…|
|`pmp-b3-114`|business-3|multi|agile|3|Your Scrum team is rebuilding an online donations platform for a national charity. Over the last four sprints, the product owner…|
|`pmp-b3-115`|business-3|single|predictive|5|You are installing a packaging line for a pharmaceutical manufacturer. The change management plan lets you approve changes up to…|
|`pmp-b3-116`|business-3|single|agile|4|Your team at a video-streaming company releases fortnightly and freezes a tested release candidate 48 hours before each release.…|
|`pmp-b3-117`|business-3|single|hybrid|3|A housing association is replacing its repairs system in a hybrid project: a fixed go-live date for the core system, with later…|
|`pmp-b3-118`|business-3|single|predictive|3|Two weeks ago, the change board of a £22 million university laboratory build approved adding a cleanroom suite, based on an…|
|`pmp-b3-119`|business-3|single|predictive|1|You are refurbishing a borough council's leisure centre. Last week the change board rejected a request from the centre's…|
|`pmp-b3-120`|business-3|single|agile|3|Your Scrum team is building a mortgage-application app for a building society. Midway through a release, the developers conclude…|
|`pmp-b3-121`|business-3|single|hybrid|2|A hospital trust is replacing its pharmacy stock system in a hybrid project. The vendor's team builds approved changes as stories…|
|`pmp-b3-122`|business-3|single|predictive|3|You are refurbishing forty rooms for a hotel group. The change management plan names the sponsor as the approver for any change…|
|`pmp-b3-123`|business-3|multi|agile|3|Two Scrum teams build a travel-booking platform. The search team owns a pricing interface that the checkout team calls. Yesterday…|
|`pmp-b3-124`|business-3|hotspot|predictive|2|You manage the extension of a rail maintenance depot. Before CR-22 goes to Thursday's change board, you review its change request…|
|`pmp-b3-125`|business-3|single|hybrid|3|An energy company runs a hybrid programme: meter installation is plan-driven, while a customer app is built in sprints. The…|
|`pmp-b3-126`|business-3|multi|predictive|4|You are building a secondary school for a multi-academy trust. Two change requests reach you in the same week. The head of…|
|`pmp-b3-127`|business-3|single|agile|3|You support a Scrum team building a language-learning app, where the product owner is accountable for the product goal. This…|
|`pmp-b3-128`|business-3|fillblank|predictive|3|An offshore substation project has a cost baseline of €48.0 million, which includes €3.2 million of contingency reserve. A…|
|`pmp-b3-129`|business-3|pulldown|agile|2|Prompts: A merchandiser asks for a new gift-card… / Card payments have failed on the website… / While an item adding delivery-slot booki…|
|`pmp-b3-130`|business-3|single|hybrid|3|You lead a hybrid project moving a bank's card-servicing platform to a new data centre. The change board has just approved a…|
|`pmp-b3-131`|business-3|single|predictive|4|Your team has delivered a new contact-centre telephony system for a regional bus operator. Acceptance tests against the agreed…|
|`pmp-b3-132`|business-3|multi|agile|4|Your agile team builds a public-sector benefits portal and can deploy small, fully tested changes several times a day. IT…|
|`pmp-b3-133`|business-3|matching|hybrid|3|Prompts: A lift in the gatehouse would open the u… / Audio-guide downloads in the app have st… / A themed courtyard café would cost €140,…|
|`pmp-b3-134`|business-3|single|agile|3|Your Scrum team builds the companion app for an insulin pump, which is regulated as medical-device software. An auditor has asked…|
|`pmp-b3-135`|business-3|single|predictive|5|You are fourteen months into building a fire station for a fire and rescue service. No scope has changed, but poor groundworks…|
|`pmp-b3-1`|export-batch-1|single|predictive|3|During a predictive project, the project manager is in the process of finalizing the exit criteria for a significant system…|
|`pmp-b3-2`|export-batch-1|single|predictive|3|A major change in a government policy may cause a project's completion date to be delayed. The project is 90% complete.|
|`pmp-b3-3`|export-batch-1|single|hybrid|4|A company that has traditionally used predictive approaches has been experimenting with the use of agile in portions of a…|
|`pmp-b3-4`|export-batch-1|single|predictive|2|In the middle of executing an IT project, two project team members discover a new feature that will change the appearance of the…|
|`pmp-b3-5`|export-batch-1|single|predictive|4|A project manager is working on a project that has been ongoing for some time and is currently at the 70% completion mark. The…|
|`pmp-b3-6`|export-batch-1|single|predictive|3|Halfway through implementation, the project co-sponsor, who is contributing a significant amount of capital to the project,…|
|`pmp-b3-7`|export-batch-1|single|predictive|2|A project manager is working on a project to modernize a bank's information technology (IT) infrastructure. The project manager…|
|`pmp-b3-8`|export-batch-1|single|predictive|5|A project is facing a one-month delay, and the project manager realizes that a change request is necessary. The change control…|
|`pmp-b3-9`|export-batch-1|single|predictive|3|A project manager is responsible for a 12-month waterfall project to create a new product. The project is nine months into…|
|`pmp-b3-10`|export-batch-1|single|predictive|4|During execution, the customer submits a change request to maintain the project's business value, which has been threatened by a…|
|`pmp-b3-11`|export-batch-1|single|predictive|4|A new solution is being deployed in several countries. Country A asked for a change in the solution in order to comply with its…|
|`pmp-b3-201`|export-batch-2|single|predictive|3|A project manager is leading a project that follows the company's predictive approach. The project manager discovers that the…|
|`pmp-b3-202`|export-batch-2|single|predictive|3|While validating deliverables, a project manager realizes that one of the project team members used an older version of the scope…|
|`pmp-b3-203`|export-batch-2|single|predictive|3|The sponsor for a multimillion-dollar project has just called the project manager for a crisis meeting. The client submitted a…|
|`pmp-b3-204`|export-batch-2|single|predictive|2|During a product acceptance test for a software project, the project sponsor tells the project manager that the software will…|
|`pmp-b3-205`|export-batch-2|single|predictive|3|A stakeholder has suggested using an off-the-shelf, open-source product to speed up development. The team has analyzed the…|
|`pmp-b3-206`|export-batch-2|single|predictive|2|A project manager is assigned to redesign a store's point-of-sale system. The team is currently testing the new functionality…|
|`pmp-b3-207`|export-batch-2|single|predictive|2|During project execution, a customer asks to replace a piece of planned hardware with one that has more features and a lower…|
|`pmp-b3-208`|export-batch-2|single|predictive|3|A project manager is reviewing work performance data in preparation for the close project process. The data reveals that one of…|
|`pmp-b3-209`|export-batch-2|single|hybrid|3|A project manager is leading a critical project to deliver a new solution. The CEO is highly invested in the project, and the CFO…|
|`pmp-b3-210`|export-batch-2|multi|hybrid|4|A project manager is leading a project that received a request for additional scope caused by changes in market demand. The…|
|`pmp-b3-211`|export-batch-2|single|predictive|2|A project team discovers an opportunity to enhance software without impacting the budget or schedule. The team makes the…|
|`pmp-b3-212`|export-batch-2|single|predictive|2|A functional manager has been making changes to a project outside of the established management plan. The functional manager…|
|`pmp-b3-213`|export-batch-2|single|predictive|3|The change control board (CCB) has evaluated and approved several change requests in a timely manner in the last several weeks.…|
|`pmp-b3-214`|export-batch-2|single|predictive|2|In the middle of project execution, the project sponsor leaves, and a new sponsor is assigned. The new sponsor is requesting…|
|`pmp-b3-215`|export-batch-2|single|predictive|4|A client requests a major scope change due to a change in the market environment, which will significantly add to the project…|
|`pmp-b3-216`|export-batch-2|single|predictive|3|During the development of a project management plan, the project team inadvertently omitted an essential component required for a…|
|`pmp-b3-217`|export-batch-2|single|predictive|2|While reviewing project documentation, a project manager discovers that modifications were made without proper approvals.|
|`pmp-b3-218`|export-batch-2|single|hybrid|3|A project manager is managing a technology project that is in the implementation phase. A key stakeholder communicates the need…|
|`pmp-b3-219`|export-batch-2|single|predictive|2|A key stakeholder requests new features to be added to a product during the project implementation phase. The stakeholder insists…|
|`pmp-b3-220`|export-batch-2|single|predictive|2|While working on a project, the project team realizes that recent data center upgrades could impact the project's outcome.…|
|`pmp-b3-221`|export-batch-2|single|agile|2|An agile team is working on a new product development project. The product owner has requested a new change to accommodate recent…|
|`pmp-b3-222`|export-batch-2|single|predictive|3|A customer's requested design change is approved by the change control board (CCB). The project manager then discovers that the…|
|`pmp-b3-223`|export-batch-2|single|predictive|2|A project team has identified a number of defects with some deliverables and intends to resolve these as soon as possible. The…|
|`pmp-b3-224`|export-batch-2|single|predictive|2|During nondestructive testing at a construction site, it was discovered that certain components are failing workability tests.…|
|`pmp-b3-225`|export-batch-2|single|predictive|2|During the planning phase for the expansion of a services company, stakeholders conducted a feasibility study to establish the…|
|`pmp-b3-226`|export-batch-2|single|hybrid|3|A new project is about to start and is subject to strict time and budget constraints. When reviewing the lessons learned…|
|`pmp-b3-227`|export-batch-2|single|predictive|2|During project execution, key stakeholders request a change to ensure that the final product will satisfy their requirements.|
|`pmp-b3-228`|export-batch-2|single|predictive|2|While reviewing the approved project schedule, the project manager realizes that the milestones are unrealistic due to…|
|`pmp-b3-229`|export-batch-2|single|predictive|2|During a status review meeting, it was determined that the product will not meet project scope requirements in a particular…|
|`pmp-b3-230`|export-batch-2|single|predictive|2|A customer wants to add new features to the project deliverable.|
|`pmp-b3-231`|export-batch-2|single|predictive|2|A key stakeholder requests a change that will solve a critical issue that might affect continuity of operations. However, the…|
|`pmp-b3-232`|export-batch-2|single|predictive|2|A stakeholder learns that the equipment purchased for a project is of higher quality than originally agreed upon. The stakeholder…|
|`pmp-b3-233`|export-batch-2|single|predictive|2|A project manager is leading a project to deliver a new product to the market. The project sponsor has asked for a broadened…|
|`pmp-b3-234`|export-batch-2|single|predictive|2|The development phase of a new product has concluded. The validation test is about to start, but the test lead disagrees with the…|
|`pmp-b3-235`|export-batch-2|single|predictive|2|After a new project is executed, an executive suggests incorporating a new addon to provide more function to the product.|
|`pmp-b3-236`|export-batch-2|single|predictive|2|As a critical update to an e-commerce platform nears completion, the project manager receives a last-minute request from a…|
|`pmp-b3-237`|export-batch-2|single|predictive|3|As part of an initiative to upgrade an educational institution's digital infrastructure, a department head proposes the…|
|`pmp-b3-238`|export-batch-2|single|agile|2|While working on an innovative web platform, a project manager receives a request from a crucial stakeholder for the inclusion of…|
|`pmp-b3-239`|export-batch-2|single|predictive|2|During a significant landscape redesign project, the project manager finds a cutting-edge irrigation system that could…|
|`pmp-b3-240`|export-batch-2|single|predictive|2|As the project manager for an innovative smart home device project, you receive a request from the stakeholder to incorporate…|
|`pmp-b3-241`|export-batch-2|single|predictive|2|The project manager for a renewable energy initiative receives a suggestion from an environmental consultant to incorporate a new…|
|`pmp-b3-242`|export-batch-2|single|agile|1|While implementing an enterprise resource planning (ERP) system, the agile project manager receives several requests for…|
|`pmp-b3-243`|export-batch-2|single|predictive|2|At the conclusion of an enterprise software implementation project, a department head suggests the addition of a new analytics…|
|`pmp-b3-244`|export-batch-2|single|agile|2|You are half way through your agile project when there is a significant change to the project scope, after it was found that the…|
|`pmp-b3-245`|export-batch-2|single|hybrid|3|You are working on a hybrid project, where the majority of the work has been planned up front. The team manages their work via a…|
|`pmp-b3-246`|export-batch-2|single|predictive|1|One of the stakeholders in your project has raised a change request, after some of the deliverables contained defects and a…|
|`pmp-b3-247`|export-batch-2|single|agile|1|You are a project manager in an agile team developing new features for an app. An executive stakeholder with a high influence in…|
|`pmp-b3-248`|export-batch-2|single|predictive|2|During User Acceptance Testing a senior user points out a major change needed to the project scope for it to be successful. You…|
|`pmp-b3-249`|export-batch-2|single|predictive|1|One of the senior users testing your product found is was missing a key requirement that he had assumed would be there. It was…|
|`pmp-b3-250`|export-batch-2|single|predictive|2|A significant change has been made to the project scope after finding a serious system flaw. This scope change will impact the…|
|`pmp-b3-251`|export-batch-2|single|predictive|1|Five major defects have been found during quality testing, resulting in the scope changing significantly and affecting your…|
|`pmp-b3-252`|export-batch-2|single|predictive|3|You have just taken over as project manager on a difficult waterfall project. Multiple project stakeholders have approached you…|
|`pmp-b3-253`|export-batch-2|single|predictive|2|One of your stakeholders with a very high influence within the company has raised an urgent change that they want to be…|
|`pmp-b3-254`|export-batch-2|single|predictive|2|Your deliverables have recently been quality checked by a specialist testing team, and passed with flying colours. You take them…|
|`pmp-b3-255`|export-batch-2|single|agile|2|You are working on a new project in your organization, where the team is trying to move to an Agile way of work. The team are not…|
