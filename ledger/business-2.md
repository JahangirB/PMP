# Question ledger — business-2 · Compliance

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

### business-2 · Compliance — 107 items

*Establish what the project must comply with, and demonstrate that it does.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b2-001`|core|pulldown|predictive|3|Prompts: The activity that establishes which laws… / The activity that produces the records a…|
|`pmp-b2-002`|core|single|hybrid|4|Two months from go-live, a regulator publishes a rule change that will make one module of your product non-compliant from a date…|
|`pmp-b2-003`|core|matching|predictive|3|Prompts: A control is being operated correctly by… / A regulation that applied when the progr… / A control has not been operated at all f…|
|`pmp-b2-004`|core|single|predictive|2|A team member proposes copying live customer records into the test environment so testing can use realistic data. The test…|
|`pmp-b2-005`|core|matching|predictive|3|Prompts: A supplier has been operating a required… / A team has been following a documented c… / A new obligation takes effect in eight m…|
|`pmp-b2-006`|core|hotspot|predictive|3|The diagram shows the chain your organisation relies on to demonstrate compliance. Your monthly access review is genuinely…|
|`pmp-b2-008`|core|pulldown|predictive|3|Prompts: The inspector asks for the 14 pressure-t… / The inspector asks a commissioning techn… / The inspector instructs you verbally to…|
|`pmp-b2-021`|core|single|agile|3|An agile team has shipped twelve increments of a patient feedback app. Preparing for a regulator's visit, you find the team's…|
|`pmp-b2-022`|core|single|agile|2|A product owner keeps ranking a mandatory record-retention feature near the bottom of the backlog because users never ask for it.…|
|`pmp-b2-023`|core|single|agile|4|Your team wants to release several times a day. The organisation's financial controls policy requires a documented approval for…|
|`pmp-b2-024`|core|single|hybrid|3|A hybrid project is building a concert venue to fixed plans while an agile team develops its digital wayfinding screens. The team…|
|`pmp-b2-025`|core|multi|agile|3|An auditor asks your agile team to show that each release meets the organisation's security testing requirement. Select two.|
|`pmp-b2-026`|core|single|hybrid|4|An ethical audit finds that a subcontractor making uniforms for your hybrid rail project breaches your organisation's labour…|
|`pmp-case1-005`|case-01|multi|predictive|3|To recover time, your planner proposes bringing the next shutdown forward so that it begins in three weeks. Connecting filter…|
|`pmp-case2-006`|case-02|single|agile|3|The permit service will store residents' names, addresses and vehicle registrations. The council's information governance officer…|
|`pmp-case4-003`|case-04|single|predictive|3|The contractor proposes a new method for fixing the display case supports into the original walls, different from the approved…|
|`pmp-case5-004`|case-05|single|hybrid|4|The human resources director wants to tell Calder advisers about their new roles at next week's team meetings to stop the…|
|`pmp-b2-101`|business-2|single|predictive|1|You manage the construction of a four-storey medical centre on a residential street. The planning permission carries a condition…|
|`pmp-b2-102`|business-2|single|agile|2|Your agile team ships a downloadable desktop app to paying customers every two weeks. Company policy, set by the legal team, bans…|
|`pmp-b2-103`|business-2|single|hybrid|4|Your hybrid programme develops software for an infusion pump under a regulated design-control process. An internal audit sampled…|
|`pmp-b2-104`|business-2|multi|predictive|3|Your firm has built wind farms only in its home country and has just won a contract to build one abroad. The contract makes your…|
|`pmp-b2-105`|business-2|single|predictive|3|Your project is upgrading the signalling on a regional rail line. The line may return to passenger service only once the…|
|`pmp-b2-106`|business-2|single|agile|5|Your agile team at an investment platform releases weekly. Regulation requires any change to customer-facing financial promotions…|
|`pmp-b2-107`|business-2|matching|predictive|3|Prompts: The client's contract requires every tes… / Your company's information-security poli… / Building regulations require a fire-safe…|
|`pmp-b2-108`|business-2|single|hybrid|3|On a hybrid programme replacing a retailer's online checkout, a tester notices that the payment service released three weeks ago…|
|`pmp-b2-109`|business-2|fillblank|predictive|3|Under its hygiene approval, a food plant your project is commissioning must record the temperature of each of its 3 cold rooms…|
|`pmp-b2-110`|business-2|single|agile|4|Your agile team runs the ordering app for a grocery delivery firm that also sells alcohol, about 12% of revenue. A new law takes…|
|`pmp-b2-111`|business-2|single|hybrid|2|You are running a hybrid programme to install a new bottling line for a drinks manufacturer. The client's contract requires every…|
|`pmp-b2-112`|business-2|multi|agile|3|Your agile team builds the ticket-buying app for a public transport authority and releases every two weeks. The authority must…|
|`pmp-b2-113`|business-2|matching|hybrid|3|Prompts: One run of 2,000 cartons was printed wit… / The same font-size error has now appeare… / A sister site using the same printer mod…|
|`pmp-b2-114`|business-2|pulldown|predictive|2|Prompts: Last week's sample at the discharge poin… / Samples have risen from 14 to 26 mg/l ov… / All samples are within the limit and log…|
|`pmp-b2-115`|business-2|hotspot|predictive|3|Your predictive project is refurbishing a 1960s office block, and stripping the roof is due to start in ten days. The excerpt…|
|`pmp-b2-116`|business-2|single|agile|3|Your agile team builds firmware for a smart thermostat sold across Europe, releasing every fortnight. The device's radio…|
|`pmp-b2-117`|business-2|single|agile|4|Your agile team develops flight-control software for commercial survey drones. To speed delivery, the company has contracted a…|
|`pmp-b2-118`|business-2|single|hybrid|5|Your hybrid programme is replacing a consumer lender's customer-management system: data migration follows a fixed plan while…|
|`pmp-b2-119`|business-2|single|predictive|1|Your predictive project to build a tram depot is closing. It was part-funded by a European grant whose agreement requires all…|
|`pmp-b2-120`|business-2|single|agile|2|Your agile team is rebuilding an online furniture retailer's checkout. Company policy, written to meet data-protection law, says…|
|`pmp-b2-121`|business-2|multi|hybrid|3|You are starting a hybrid programme to build a visitor centre in a national park, with a ticketing app developed in sprints. You…|
|`pmp-b2-122`|business-2|single|predictive|3|Your predictive project is upgrading a waste-to-energy plant. A tightened emissions limit takes effect in eight months, and the…|
|`pmp-b2-123`|business-2|single|agile|3|Your agile team builds the membership app for a chain of gyms. The product owner wants members to be able to pay for annual…|
|`pmp-b2-124`|business-2|single|predictive|2|You are managing a planned two-week maintenance shutdown at an oil refinery. Site rules, required under the refinery's safety…|
|`pmp-b2-125`|business-2|multi|agile|3|Your agile team at a motor insurer plans to release, in two sprints, a feature that sends customers' support-chat transcripts to…|
|`pmp-b2-126`|business-2|matching|hybrid|3|Prompts: The booking app will process patients' h… / The X-ray room's radiation shielding mus… / Once the unit is open, the hospital must…|
|`pmp-b2-127`|business-2|single|predictive|3|Your predictive project is building an integration facility for a naval combat system. The prime contract requires every…|
|`pmp-b2-128`|business-2|single|agile|4|Your agile team maintains the billing platform for an energy supplier. A new regulation requires bills to show a tariff…|
|`pmp-b2-129`|business-2|fillblank|hybrid|3|Your hybrid programme is building a university science campus. Its planning consent requires at least 70% by weight of all…|
|`pmp-b2-130`|business-2|single|predictive|2|Your predictive project for a city council is procuring a new parking-enforcement system. Council procurement rules require…|
|`pmp-b2-131`|business-2|pulldown|agile|3|Prompts: The rule that every patient record field… / The hospital trusts buying the app requi… / Company policy requires every developer…|
|`pmp-b2-132`|business-2|single|predictive|4|Your predictive project is building a data centre for a bank. An independent audit has checked the facility against 400…|
|`pmp-b2-133`|business-2|multi|hybrid|4|Your hybrid programme for a regional tourism body is part-funded by a development grant of €1.2 million. The grant agreement…|
|`pmp-b2-134`|business-2|hotspot|predictive|3|Your predictive project is laying a fibre-optic cable along a coastal route, and you are reviewing the draft schedule before it…|
|`pmp-b2-135`|business-2|single|agile|5|Your agile team builds the online shop for a mobile network operator, working in two-week sprints. A new consumer rule requires…|
|`pmp-b2-1`|export-batch-1|single|predictive|3|A project manager is assigned to a project in a country where the company has not previously done business. At the beginning of…|
|`pmp-b2-2`|export-batch-1|single|predictive|4|A project manager is implementing a phase of an IT project in a location that is prone to security issues. A team member, working…|
|`pmp-b2-3`|export-batch-1|single|predictive|3|A project is about to begin and a new regulation has imposed a deadline that will adversely impact the project. Failing to comply…|
|`pmp-b2-4`|export-batch-1|single|predictive|3|An organization is halfway through a five year project and everything has progressed as scheduled. The latest project assessment…|
|`pmp-b2-5`|export-batch-1|single|predictive|3|A compliance issue was found during the course of a project. This caused an inspection of the product by the internal audit team.…|
|`pmp-b2-6`|export-batch-1|single|agile|4|At the start of a project, the team chose to leverage an agile approach to deliver the project, using user stories to capture…|
|`pmp-b2-7`|export-batch-1|single|predictive|3|A team member raises a concern that some deliverables may not be compliant with certain regulations.|
|`pmp-b2-8`|export-batch-1|single|predictive|3|During project execution, the audit team determined that critical regulatory requirements are not being met.|
|`pmp-b2-9`|export-batch-1|single|predictive|4|A project team is installing a fiscal metering system for a crude oil terminal. The project manager has requested a metering…|
|`pmp-b2-10`|export-batch-1|single|agile|4|A company must launch a global product to respond to a competition threat. As part of an agile approach, the team chooses Region…|
|`pmp-b2-11`|export-batch-1|single|agile|4|A team is working in a highly regulated industry and the compliance department keeps pushing documentation and process steps that…|
|`pmp-b2-12`|export-batch-1|single|predictive|3|A project manager is contracted to manage a project for a startup with grants from a foreign source. Midway into this project,…|
|`pmp-b2-13`|export-batch-1|single|predictive|2|A project manager was recently assigned to an investigation project. Due to the complexity of the project, the project manager is…|
|`pmp-b2-14`|export-batch-1|single|predictive|4|An IT contract for a project states that all team members must be certified in IT security. The project manager discovers that…|
|`pmp-b2-201`|export-batch-2|single|hybrid|3|While planning a global solution delivery in a multinational organization, the project manager and team drafted all work items…|
|`pmp-b2-202`|export-batch-2|single|hybrid|3|A project manager is assigned a complex project with significant legal, regulatory, and contractual requirements. However, these…|
|`pmp-b2-203`|export-batch-2|single|predictive|2|A new project manager takes over a construction project. Upon reviewing the project management plan, the project manager realizes…|
|`pmp-b2-204`|export-batch-2|single|predictive|2|A project manager for a building project has been informed that a required permit from the local government has not yet been…|
|`pmp-b2-205`|export-batch-2|single|agile|3|A project manager is working in a regulated industry, and the team is working on a project where the requirements are prioritized…|
|`pmp-b2-206`|export-batch-2|single|predictive|2|During a design workshop for a new robot, the engineers ask the project manager about safety and environmental protocols for its…|
|`pmp-b2-207`|export-batch-2|single|predictive|2|While attending a governance meeting with upper management, a project manager is verbally informed about an impending external…|
|`pmp-b2-208`|export-batch-2|single|predictive|2|During the execution phase of a project, a new director joined the management team and introduced a mandatory compliance…|
|`pmp-b2-209`|export-batch-2|single|predictive|3|A company initiated a project to build a new hydropower plant. Analysis was performed to determine regulatory, safety, and…|
|`pmp-b2-210`|export-batch-2|single|predictive|2|A gas pipeline construction project was planned for completion in 36 months. After 24 months, the project was suspended by the…|
|`pmp-b2-211`|export-batch-2|single|predictive|3|While planning a new project, a new project manager finds that the request for proposal (RFP) references several government…|
|`pmp-b2-212`|export-batch-2|single|agile|3|A new Scrum project manager at a company is contracted to build a traffic measurement and control application for a municipality.…|
|`pmp-b2-213`|export-batch-2|single|hybrid|2|A project manager is working on a hybrid (predictive-agile) project. During a standup meeting, a team member mentions that…|
|`pmp-b2-214`|export-batch-2|single|predictive|2|A majority portion of a local company was recently sold to an international company. The new parent company is mandating the use…|
|`pmp-b2-215`|export-batch-2|multi|hybrid|4|A department store chain is in the implementation stage of a project to replace the payment systems across all of its stores in…|
|`pmp-b2-216`|export-batch-2|single|agile|2|During the last iteration of planning in an agile project, the team discovers that mandatory regulations were not included in the…|
|`pmp-b2-217`|export-batch-2|single|predictive|2|A project manager identifies that the work started by the project team is inconsistent with industry regulations.|
|`pmp-b2-218`|export-batch-2|single|agile|3|During a product backlog refinement meeting, a stakeholder from the legal department and the product owner disagree on the…|
|`pmp-b2-219`|export-batch-2|single|predictive|2|A software development project is in the testing phase. New government regulation requires that additional documentation be…|
|`pmp-b2-220`|export-batch-2|single|predictive|2|What should a project manager do first when people are not following the project compliance process?|
|`pmp-b2-221`|export-batch-2|single|hybrid|3|A project manager is leading a complex project with diverse stakeholders and a strict regulatory environment. The project team is…|
|`pmp-b2-222`|export-batch-2|single|hybrid|3|A project manager has made compliance-related changes by adopting a new project delivery approach. During external validation, an…|
|`pmp-b2-223`|export-batch-2|single|agile|3|A project manager considers compliance to be part of the organization's process assets. The project team, however, has decided to…|
|`pmp-b2-224`|export-batch-2|single|predictive|3|An organization conducted an external audit in an Offshore Data Centre (ODC). The audit team noticed that the project manager is…|
|`pmp-b2-225`|export-batch-2|single|predictive|3|A project manager has engaged the sponsor and discussed concerns regarding noncompliance. The project manager understands that…|
|`pmp-b2-226`|export-batch-2|single|hybrid|3|A project manager is working on a new compliance project that is different from regular projects as it will involve multinational…|
|`pmp-b2-227`|export-batch-2|single|predictive|2|A complex construction project entails the meeting of several regulations throughout project execution. The project team is…|
|`pmp-b2-228`|export-batch-2|single|agile|3|An agile project team has implemented design changes over several iterations. The data privacy officer is concerned because their…|
|`pmp-b2-229`|export-batch-2|single|predictive|2|A new project is initiated to develop several new financial products and services for a company with offices in several…|
|`pmp-b2-230`|export-batch-2|single|predictive|3|A project manager is leading a development project for an external customer in another country. The project manager's company is…|
|`pmp-b2-231`|export-batch-2|single|agile|2|On an agile project, a team member raises concerns about health and safety issues related to the project deliverable.|
|`pmp-b2-232`|export-batch-2|single|predictive|3|A project manager needs to ensure that all data security requirements are covered during a data warehouse development project’s…|
|`pmp-b2-233`|export-batch-2|multi|agile|3|A project manager is leading an agile project developing a new customer relationship management (CRM) system. Midway through the…|
|`pmp-b2-234`|export-batch-2|single|hybrid|3|In anticipation of new environmental regulations, a manufacturing project involving two distinct operational teams-one using…|
|`pmp-b2-235`|export-batch-2|single|predictive|2|A project manager is working on a government infrastructure project that requires strict compliance with company standards.…|
|`pmp-b2-236`|export-batch-2|single|predictive|3|In the rollout of a critical update to an e-commerce platform, the project manager is presented with several pressing issues:…|
|`pmp-b2-237`|export-batch-2|single|predictive|2|During an environmental impact review of an energy project, the project manager discovers that the environmental plan does not…|
|`pmp-b2-238`|export-batch-2|single|predictive|3|Launching a project to introduce a new health information system across multiple clinics, the project manager realizes there are…|
|`pmp-b2-239`|export-batch-2|single|predictive|2|A project manager is tasked with launching a new drug product, which requires adherence to stringent health and safety…|
|`pmp-b2-240`|export-batch-2|single|predictive|2|You are halfway through a large scale project for your organization when you are approached by the internal Audit department.…|
|`pmp-b2-241`|export-batch-2|single|predictive|1|You are working as a project manager in a foreign country. It is customary that gifts be made to the government to obtain the…|
