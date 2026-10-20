# Question ledger — process-7 · Quality

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

### process-7 · Quality — 118 items

*Set quality requirements, run reviews, manage cost of quality, improve.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr7-001`|core|matching|predictive|3|Prompts: Training installers on the new torque pr… / Inspecting a sample of finished units ag… / Reworking units found defective on the p… / Recalling units from customers after a f…|
|`pmp-pr7-002`|core|single|agile|3|Over six sprints, your team's velocity has fallen steadily while the number of defects found in each sprint has risen. The team…|
|`pmp-pr7-003`|core|multi|agile|3|Defects found after release have tripled over four sprints. Your testers are working longer hours and finding more, but the…|
|`pmp-pr7-004`|core|single|predictive|2|Your finance business partner has flagged that the project is spending £180,000 on inspection, testing and audit activities, and…|
|`pmp-pr7-005`|core|single|predictive|3|Acceptance testing has found the same category of defect eleven times: incorrect handling of dates that fall in the final week of…|
|`pmp-pr7-006`|core|hotspot|predictive|2|Your project applies the five quality steps shown in the diagram to every deliverable it produces. Your sponsor has asked where…|
|`pmp-pr7-014`|core|multi|agile|4|Your team is building the allergen-labelling module of a food manufacturer's production system. A major retailer buying from the…|
|`pmp-pr7-008`|core|single|predictive|3|You are three weeks from a committed release date and two weeks behind. Your test manager reports that the regression suite will…|
|`pmp-pr7-015`|core|multi|agile|3|Your team builds the rider app for a city bike-share operator. A separate platform team, with its own product owner, owns the…|
|`pmp-pr7-010`|core|matching|predictive|3|Prompts: Measurements sit comfortably inside the… / Ninety detected defects fall into eleven… / A batch passes every dimensional and mat…|
|`pmp-pr7-011`|core|single|agile|2|On your team an item is done when the code is merged and reviewed. Testing happens in a dedicated hardening iteration before each…|
|`pmp-pr7-012`|core|single|agile|3|Your team has invested heavily in automated unit tests and reports 94 per cent code coverage. The build has been green for six…|
|`pmp-pr7-013`|core|multi|predictive|3|A quality audit of your project finds full compliance: every design review was held, every checklist completed, every sign-off…|
|`pmp-case2-008`|case-02|single|agile|3|A resident who uses a screen reader reports that the payment page cannot be completed without a mouse. The team's definition of…|
|`pmp-case3-003`|case-03|multi|hybrid|3|Site A is four weeks from go-live. Exhibit 3 shows 92 per cent of validation tests passed and all interfaces ready, and…|
|`pmp-pr7-101`|process-7|single|predictive|1|You are planning a £410,000 project to design and install wayfinding signage across a 600-bed hospital. The estates director says…|
|`pmp-pr7-102`|process-7|single|predictive|2|A bottling line installed for a drinks launch fills 500 ml bottles. The customer specification allows 495 to 505 ml, and the…|
|`pmp-pr7-103`|process-7|matching|predictive|2|Prompts: The budget guest-house wing has basic fi… / A new laboratory balance gives repeat re… / The executive suite has imported marble…|
|`pmp-pr7-104`|process-7|single|agile|3|Your team builds a clinic-booking app in two-week sprints, and its Definition of Done includes accessibility checks against the…|
|`pmp-pr7-105`|process-7|multi|hybrid|3|A retail group's e-commerce programme has six delivery teams: four agile teams and two working to a phased plan on the warehouse…|
|`pmp-pr7-106`|process-7|single|agile|3|At a sprint review for a council tenant-repairs portal, the housing manager rejects the 'report a repair' story. It passed every…|
|`pmp-pr7-107`|process-7|single|hybrid|4|Your hybrid project delivers 60 factory-built classroom modules, with interior layouts refined with the school in fortnightly…|
|`pmp-pr7-108`|process-7|single|predictive|3|Halfway through a €3.2 million substation upgrade, the cost performance index is 0.98 and the steering group is satisfied.…|
|`pmp-pr7-109`|process-7|pulldown|predictive|2|Prompts: An implantable pump housing, where a sin… / Burst testing destroys a pressure seal,… / A moulding press whose part dimensions d…|
|`pmp-pr7-110`|process-7|single|agile|4|Your team ships a payments API weekly. Its end-to-end suite of 400 tests takes 70 minutes and fails about one run in eight for…|
|`pmp-pr7-111`|process-7|single|agile|3|Your team maintains billing software for an energy retailer. Yesterday it released, behind a feature flag, a new way of…|
|`pmp-pr7-112`|process-7|single|predictive|5|Your project is replacing 40,000 water meters for a utility, and 9,000 are done. Installers self-certify, and an independent…|
|`pmp-pr7-113`|process-7|single|hybrid|4|On your hybrid programme for a housing association, a supplier builds a tenant self-service portal in sprints while the…|
|`pmp-pr7-114`|process-7|multi|agile|3|Your team builds a ticket-booking web app for a concert promoter in two-week sprints. Every story has met its acceptance criteria…|
|`pmp-pr7-115`|process-7|matching|hybrid|2|Prompts: A clerk of works measures the installed… / Night-shift nurses use a mock-up bay to… / An assessor from the trust's quality off…|
|`pmp-pr7-116`|process-7|fillblank|predictive|3|Handover snagging on a 120-home development has logged 250 defects: 112 sealant gaps (about £40 each to put right), 64 sticking…|
|`pmp-pr7-117`|process-7|hotspot|hybrid|3|Your hybrid programme traced each of last quarter's 40 production defects back to the stage where it was introduced. Each box…|
|`pmp-pr7-118`|process-7|single|predictive|3|On a hospital multi-storey car park, the inspection and test plan you own sets a hold point: no slab is poured until the…|
|`pmp-pr7-119`|process-7|single|agile|3|Your team releases an insurer's online claims portal to production twice a week. After a breach elsewhere in the group, the chief…|
|`pmp-pr7-120`|process-7|multi|agile|4|Your team builds a pharmacy chain's repeat-prescription app and releases at the end of every sprint. After a customer complaint…|
|`pmp-pr7-121`|process-7|single|predictive|2|Your team is building a hydraulic test rig for an aerospace customer to a signed, fixed-price specification. During a…|
|`pmp-pr7-122`|process-7|single|agile|2|Your team builds a parcel-tracking app and integrates code continuously. The main build has been failing for a day and a half…|
|`pmp-pr7-123`|process-7|single|hybrid|3|Your hybrid programme is automating a distribution warehouse: conveyors are installed to a fixed plan while the picking software…|
|`pmp-pr7-124`|process-7|single|predictive|4|You are implementing a payroll shared-service centre for a university. For 20 weeks the percentage of payroll queries resolved…|
|`pmp-pr7-125`|process-7|single|agile|4|Your team builds a supermarket's loyalty app. To cut escaped defects, it agreed at a retrospective to trial a rule that every…|
|`pmp-pr7-126`|process-7|multi|predictive|3|On a gas pipeline project, a pressure gauge used for hydrostatic testing fails its scheduled calibration check: it reads 6 per…|
|`pmp-pr7-127`|process-7|pulldown|agile|1|Prompts: A rule that every story must pass the au… / The condition that a member may freeze a… / The expectation that a story's payment-p…|
|`pmp-pr7-128`|process-7|single|predictive|3|You are managing your company's project to commission an injection-moulding tool for the battery housings of its new electric…|
|`pmp-pr7-129`|process-7|single|hybrid|3|Your hybrid programme is replacing the practice-management system for a group of 60 veterinary surgeries; the software is…|
|`pmp-pr7-130`|process-7|single|agile|4|Your team has taken over a 12-year-old pension calculation engine that has no automated tests. Three changes required by new…|
|`pmp-pr7-131`|process-7|matching|predictive|3|Prompts: The floor contractor must choose among t… / The client wants a target for temperatur… / The team must decide whether a £45,000 a…|
|`pmp-pr7-132`|process-7|single|hybrid|2|Your hybrid programme is rolling out a new field-service app to boiler engineers in three regions. The dashboard shows first-time…|
|`pmp-pr7-133`|process-7|multi|agile|3|Your team supports a travel company's booking site using Kanban. Over the past month the 'awaiting test' column has grown from…|
|`pmp-pr7-134`|process-7|single|predictive|4|Your project is building foundations for 30 wind turbines. The contract's sampling plan for anchor bolts says: from each lot of…|
|`pmp-pr7-135`|process-7|matching|agile|2|Prompts: Code from different developers is merged… / Only one developer understands the inter… / Developers write the code first and add…|
|`pmp-pr7-136`|process-7|fillblank|hybrid|3|A research funder's hybrid programme has rebuilt its grant-application process in three stages. Last quarter, 96 per cent of…|
|`pmp-pr7-137`|process-7|single|predictive|5|Your firm is repainting a steel railway bridge. The quality management plan, approved by the client's engineer as a contract…|
|`pmp-pr7-138`|process-7|single|agile|3|Your team runs a university's online learning platform. The product owner consistently ranks new features above defect fixes, and…|
|`pmp-pr7-139`|process-7|multi|predictive|3|You are managing the fit-out of 400 cabins on a cruise ship. Two inspectors check veneer panels for finish defects. In a weekly…|
|`pmp-pr7-140`|process-7|hotspot|hybrid|3|Your hybrid programme is commissioning a new bread line: equipment is installed to a fixed plan and its control software is tuned…|
|`pmp-pr7-1`|export-batch-1|single|agile|3|A project team member informs the project manager that all items are complete and the team is ready to move on to the next…|
|`pmp-pr7-2`|export-batch-1|single|agile|3|A company is developing a new product that needs to implement nonfunctional security requirements. The product owner is concerned…|
|`pmp-pr7-3`|export-batch-1|single|agile|3|A functional manager involved in a project complains about the latest quality metrics reports associated with all previous…|
|`pmp-pr7-4`|export-batch-1|single|predictive|3|A customer has rejected an interim project deliverable.|
|`pmp-pr7-5`|export-batch-1|single|predictive|3|While closing a project, the client finds some defects in the deliverable.|
|`pmp-pr7-6`|export-batch-1|single|predictive|3|During a final inspection of a new product, the project team discovers a significant defect in one of the key deliverables. This…|
|`pmp-pr7-201`|export-batch-2|single|agile|2|The first iteration of an agile project consists of a minimum viable product (MVP) that is now successfully completed. The team…|
|`pmp-pr7-202`|export-batch-2|single|predictive|3|A project manager is leading a project to deliver a new product in a heavily regulated industry and must prioritize quality…|
|`pmp-pr7-203`|export-batch-2|single|predictive|3|An organization has a policy requiring all project managers to conduct a standardized audit of another project manager's project…|
|`pmp-pr7-204`|export-batch-2|single|predictive|3|A project manager is newly assigned to an ongoing project and determines that the quality has been consistent. There is a…|
|`pmp-pr7-205`|export-batch-2|single|predictive|3|A project manager assigned to an existing project finds that the deliverable quality has been inconsistent. The project manager…|
|`pmp-pr7-206`|export-batch-2|single|predictive|3|A project manager in a manufacturing company was notified of a failure of the delivered machinery during the initial tests. The…|
|`pmp-pr7-207`|export-batch-2|single|predictive|3|A new product was accepted by the customer, but after two weeks the project manager received a complaint from the customer saying…|
|`pmp-pr7-208`|export-batch-2|single|predictive|2|A serious quality issue has been identified during the implementation of a major public infrastructure project. The issue is…|
|`pmp-pr7-209`|export-batch-2|single|hybrid|2|A company is working on a hybrid project in which the deliverables have quality issues at the end of one of the sprints.|
|`pmp-pr7-210`|export-batch-2|single|predictive|3|A project manager working in a matrix organization is managing a project that is scheduled to be completed in one month. At…|
|`pmp-pr7-211`|export-batch-2|single|predictive|3|A project for an accounts receivable system that was recently launched into production received a fine from a regulatory body…|
|`pmp-pr7-212`|export-batch-2|single|predictive|2|A project is in the execution phase, and product testing is almost complete. A team member proposes that the testing can be…|
|`pmp-pr7-213`|export-batch-2|single|predictive|2|The client agreed to the standards that would be used to assess final deliverables prior to hand-off, but is now asking for…|
|`pmp-pr7-214`|export-batch-2|single|predictive|2|A major project deliverable is completed.|
|`pmp-pr7-215`|export-batch-2|single|predictive|3|A project team has not been performing user acceptance tests on a deliverable because the product has been working as expected in…|
|`pmp-pr7-216`|export-batch-2|single|predictive|3|Due to a budget issue, the project team decides to use low-cost equipment with higher testing uncertainty to reduce cost.…|
|`pmp-pr7-217`|export-batch-2|single|predictive|3|During final inspection of a deliverable, the quality team observes that there is a deviation on a particular component. The…|
|`pmp-pr7-218`|export-batch-2|single|agile|3|An agile project team is implementing a supporting technology platform across departments. They're planning a readiness review…|
|`pmp-pr7-219`|export-batch-2|single|predictive|3|All quality controls have passed, and a project deliverable is ready to be released to the customer. However, a change request to…|
|`pmp-pr7-220`|export-batch-2|single|agile|3|During a retrospective meeting, the project team confirmed that all deliverables were completed according to the specifications…|
|`pmp-pr7-221`|export-batch-2|single|agile|3|A project manager is creating a software project's quality management plan. While reviewing lessons learned from past, similar…|
|`pmp-pr7-222`|export-batch-2|single|agile|3|A project manager is responsible for a new agile project that is global in nature. After a few sprints have been completed, the…|
|`pmp-pr7-223`|export-batch-2|single|predictive|2|While reviewing the quality of deliverables with the team, the project manager determines that several quality issues could have…|
|`pmp-pr7-224`|export-batch-2|single|predictive|2|A new project manager joins a project overseen by a senior project manager. The sponsor asks the new project manager to develop…|
|`pmp-pr7-225`|export-batch-2|single|predictive|2|A customer has informed the project manager that they do not accept the project deliverables.|
|`pmp-pr7-226`|export-batch-2|single|predictive|3|A customer identifies performance issues on a newly implemented product.|
|`pmp-pr7-227`|export-batch-2|multi|agile|3|One of the retrospective action items that has recurred for the past two iterations is related to the verification and validation…|
|`pmp-pr7-228`|export-batch-2|single|predictive|2|For quality assurance, a quality audit was conducted on the processes being used in the project execution plan. One of the…|
|`pmp-pr7-229`|export-batch-2|multi|predictive|3|A project is challenged by a multitude of problems resulting in defects and stakeholder complaints. The project manager has been…|
|`pmp-pr7-230`|export-batch-2|single|predictive|2|A project manager is assigned to take over an ongoing project. During the quality review, the team discovered that 40% of the…|
|`pmp-pr7-231`|export-batch-2|multi|predictive|3|A project team is unexpectedly audited. Several nonconformances were identified during the audit. (Choose three)|
|`pmp-pr7-232`|export-batch-2|single|agile|2|During product demonstration, a stakeholder suggests that the product does not meet expectations.|
|`pmp-pr7-233`|export-batch-2|single|predictive|2|A stakeholder complains that a product component is not easy to use.|
|`pmp-pr7-234`|export-batch-2|single|agile|1|A developer has completed the deliverable for a feature. The product owner is not sure how to assess the quality of the…|
|`pmp-pr7-235`|export-batch-2|single|hybrid|3|A project manager is leading an intricate biotechnology research project characterized by significant ambiguity and…|
|`pmp-pr7-236`|export-batch-2|single|predictive|3|Just before the final review of a new software development project, the project manager is informed by the quality assurance team…|
|`pmp-pr7-237`|export-batch-2|single|predictive|2|A project manager is preparing for a quality audit of a new software development project. The audit team needs to ensure the…|
|`pmp-pr7-238`|export-batch-2|single|predictive|2|During the execution phase of a manufacturing project, an internal quality audit reveals several non conformances in the…|
|`pmp-pr7-239`|export-batch-2|single|predictive|2|In the middle of a large infrastructure project, the project manager receives a report from an external quality auditor…|
|`pmp-pr7-240`|export-batch-2|single|predictive|2|In the initial stages of launching a pioneering software tool aimed at small businesses, the project manager is focused on…|
|`pmp-pr7-241`|export-batch-2|single|hybrid|2|Towards the end of a large-scale infrastructure development project, the project manager detects a slowdown in progress with a…|
|`pmp-pr7-242`|export-batch-2|single|agile|2|You are working on a project where senior users have been testing the product as parts are released. They have found some missing…|
|`pmp-pr7-243`|export-batch-2|single|agile|1|Your project team are trialling an agile way of work. The Product Owner has a clear vision for the product. There is confusion in…|
|`pmp-pr7-244`|export-batch-2|single|predictive|2|You have been working closely with the quality manager of your project, planning the budget for the project phase that is due to…|
|`pmp-pr7-245`|export-batch-2|single|agile|3|Your agile project has recently had multiple quality issues. Project features have been prioritised by the Product Owner, and…|
|`pmp-pr7-246`|export-batch-2|single|agile|2|The product owner has prioritised the product backlog of features, and the team creates user stories with estimates and places…|
|`pmp-pr7-247`|export-batch-2|single|predictive|2|You are working on a new software application with a lot of complex interdependencies. The senior user providing and testing most…|
|`pmp-pr7-248`|export-batch-2|single|agile|2|You are working with the team on the team charter and have outlined the project vision, mission, agreed meeting times and…|
|`pmp-pr7-249`|export-batch-2|single|predictive|2|You have been asked to work on a process improvement within your team. Your manager has secured a developer to assist you with…|
|`pmp-pr7-250`|export-batch-2|single|predictive|2|As your project begins, it becomes clear the most of the business analysts, developers and testers are not familiar with system…|
|`pmp-pr7-251`|export-batch-2|single|predictive|2|You have been asked to monitor the changes to a product before completing your next project phase. You review the control chart.…|
|`pmp-pr7-252`|export-batch-2|single|predictive|2|A quality assurance manager has been hired externally to test the project and manage defects. She executed the process in the…|
|`pmp-pr7-253`|export-batch-2|single|predictive|3|You are in the process of measuring the outcome of one of your deliverables on the business. The data shows that there seems to…|
|`pmp-pr7-254`|export-batch-2|single|predictive|2|You take the completed scope to your project sponsor for them to accept. The Project Sponsor advises she cannot accept the…|
|`pmp-pr7-255`|export-batch-2|single|agile|2|You are working as a project manager in an Agile team.|
|`pmp-pr7-256`|export-batch-2|single|agile|1|You are working with your agile team, and they are confused about when the best time is to determine the acceptance test criteria…|
|`pmp-pr7-257`|export-batch-2|single|hybrid|2|You have been working with a project team, delivering new items very quickly. The team were so focused on delivering items…|
