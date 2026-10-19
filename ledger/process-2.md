# Question ledger — process-2 · Scope

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

### process-2 · Scope — 118 items

*Define scope, secure agreement on it, and break it down.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr2-001`|core|pulldown|agile|2|Prompts: The shared standard that every backlog i… / The set of conditions specific to one ba…|
|`pmp-pr2-002`|core|single|predictive|3|A business analyst on your team has been accepting small refinements directly from users during requirements validation sessions,…|
|`pmp-pr2-003`|core|single|agile|2|Your team consistently spends the first two days of each two-week sprint working out what the top items actually mean, and often…|
|`pmp-pr2-004`|core|hotspot|predictive|2|The diagram shows the sequence your project follows to establish and hold its scope. A stakeholder has just asked for an…|
|`pmp-pr2-005`|core|single|predictive|3|A developer on your fixed-price contract has added a configurable dashboard to a reporting module. It took her four days, it was…|
|`pmp-pr2-006`|core|multi|predictive|3|Reviewing the last two months, you find your team has delivered eleven small pieces of functionality that appear nowhere in the…|
|`pmp-pr2-007`|core|single|agile|2|Your team's backlog contains around forty items that have never been refined, several written over a year ago by people who have…|
|`pmp-pr2-014`|core|single|predictive|3|On a £6.8 million accessibility upgrade at four railway stations, lift refurbishment has come in £290,000 over estimate. At last…|
|`pmp-pr2-009`|core|single|agile|2|A backlog item reading as a complete claims submission journey has been carried over three sprints running. Each time the team…|
|`pmp-pr2-010`|core|multi|predictive|2|You inherit a work breakdown structure for an eighteen-month plant upgrade. Its lowest level contains fourteen work packages,…|
|`pmp-pr2-011`|core|single|agile|2|During sprint planning, your team says it cannot size a story called "improve reporting performance" because it does not know…|
|`pmp-pr2-012`|core|single|agile|2|Reviewing your product backlog, you notice the product owner has split each feature by technical layer: a database story, a…|
|`pmp-pr2-013`|core|hotspot|predictive|2|A workstream lead has drafted the second level of the work breakdown structure for a depot systems rollout, shown below. You are…|
|`pmp-pr2-061`|core|graphic|hybrid|3|The release burnup for your programme's customer portal shows the points completed and the total release scope at the end of each…|
|`pmp-case3-009`|case-03|single|hybrid|3|The Site A laboratory manager has asked the vendor for an additional turnaround-time report. The vendor says it is outside the…|
|`pmp-pr2-101`|process-2|single|predictive|1|You are drafting the project scope statement for fitting out a temporary exhibition at a regional museum. Over coffee, the…|
|`pmp-pr2-102`|process-2|single|predictive|3|On an electronic prescribing rollout at a district hospital, the chief pharmacist requires a barcode scan for every dose given.…|
|`pmp-pr2-103`|process-2|single|predictive|4|Your organisation's PMO requires every phase of a project to be decomposed to work packages before the scope baseline is…|
|`pmp-pr2-104`|process-2|single|predictive|3|On a water treatment upgrade, two contractors each claim that installing the cable trays between the new dosing building and the…|
|`pmp-pr2-105`|process-2|single|agile|2|A product owner at a publishing company writes acceptance criteria for a story about keeping the online catalogue in step with…|
|`pmp-pr2-106`|process-2|single|agile|4|At a sprint review for a council's planning-application portal, the head of planning says the new upload-documents feature is not…|
|`pmp-pr2-107`|process-2|single|agile|4|A ferry operator's new booking app must go live for the summer timetable in nine weeks; the season fixes the date. At the team's…|
|`pmp-pr2-108`|process-2|single|agile|3|The product owner for your studio's mobile puzzle game is on leave for three weeks and named no stand-in. Since she left, the art…|
|`pmp-pr2-109`|process-2|single|hybrid|5|A pharmaceutical laboratory is replacing its sample-tracking system. Its user requirements are baselined under formal change…|
|`pmp-pr2-110`|process-2|single|hybrid|3|A university has a fixed-price contract for a timetabling system delivered in two-week iterations. The contract lets the…|
|`pmp-pr2-111`|process-2|single|predictive|2|You are managing the replacement of a pension administrator's benefits calculation engine. The plan schedules detailed…|
|`pmp-pr2-112`|process-2|single|hybrid|3|A logistics company is rolling out a warehouse management system. Its steering committee approves the epics, each with a budget,…|
|`pmp-pr2-113`|process-2|multi|predictive|3|Your firm is refurbishing forty passenger carriages for a train operator. The operator's inspector has rejected the first…|
|`pmp-pr2-114`|process-2|multi|agile|3|Four days into a ten-day sprint, a charity's donations team finds that the gift-aid declaration story, central to the sprint goal…|
|`pmp-pr2-115`|process-2|multi|hybrid|4|A football club's €38 million stadium redevelopment pairs a fixed-design construction contract with a fan app built in monthly…|
|`pmp-pr2-116`|process-2|matching|predictive|2|Prompts: A test lead wants to know which business… / The conveyor subcontractor asks exactly… / The airline liaison asks whether the pro…|
|`pmp-pr2-117`|process-2|matching|agile|3|Prompts: The team cannot size a meter-reading sto… / Stakeholders keep arguing about what bel… / Marketing keeps adding feature requests,…|
|`pmp-pr2-118`|process-2|pulldown|hybrid|3|Prompts: Before inviting the trust to inspect the… / The trust's estates director walks the l… / At the end of a sprint, the product owne…|
|`pmp-pr2-119`|process-2|fillblank|predictive|2|On a grid substation project, the WBS element 'Substation 3' carries a budget of €480,000. Below it sit two work packages, civils…|
|`pmp-pr2-120`|process-2|hotspot|agile|3|A veterinary group's story map for its appointment app has five journey steps. Group policy says no appointment is confirmed…|
|`pmp-pr2-121`|process-2|single|predictive|1|You are finalising the project scope statement for refurbishing three floors of a law firm's London office. The partners' draft…|
|`pmp-pr2-122`|process-2|single|predictive|3|On a £14 million data-centre build, your planning lead has decomposed the WBS into 620 work packages, most representing half a…|
|`pmp-pr2-123`|process-2|single|predictive|2|Your company is installing a new keycard system across a hotel group's twelve sites. The contract names the group's operations…|
|`pmp-pr2-124`|process-2|single|predictive|4|Your team is building a digital archive for a city library in Cardiff under a signed scope statement that includes 'a public…|
|`pmp-pr2-125`|process-2|single|agile|4|A payments team at a fintech adopted a definition of ready after a sprint in which half the stories proved unclear. It now…|
|`pmp-pr2-126`|process-2|single|agile|2|A sports equipment retailer is starting an agile rebuild of its online shop. Before sprint one, the new product owner plans to…|
|`pmp-pr2-127`|process-2|single|agile|3|The team building an airline's crew-rostering tool wants to replace a rule engine it put together quickly in early sprints; each…|
|`pmp-pr2-128`|process-2|single|agile|3|An agri-tech company's team ran a three-day spike on a story to detect crop disease from drone images at single-leaf resolution.…|
|`pmp-pr2-129`|process-2|single|hybrid|5|A fashion retailer approved a £2.1 million programme whose business case counts on £400,000 a year of savings from self-service…|
|`pmp-pr2-130`|process-2|single|hybrid|3|A housing association is using three-week prototype iterations to settle requirements for a tenant repairs portal, which a…|
|`pmp-pr2-131`|process-2|single|hybrid|3|A domestic heating manufacturer is building a smart thermostat. The circuit board and enclosure go to a contract manufacturer…|
|`pmp-pr2-132`|process-2|single|hybrid|4|A diagnostics company's laboratory analyser software is regulated: every baselined design input must trace to the code and tests…|
|`pmp-pr2-133`|process-2|multi|predictive|3|You are managing an insurer's move of 600 staff into a leased office in Dublin. The approved scope statement assumes the landlord…|
|`pmp-pr2-134`|process-2|multi|agile|4|Three days into a two-week sprint, a payroll software company's team is building support for a proposed change to employee tax…|
|`pmp-pr2-135`|process-2|multi|predictive|3|A music festival is commissioning a new ticketing website under a fixed-price contract. Reviewing the requirements document…|
|`pmp-pr2-136`|process-2|matching|predictive|2|Prompts: Listing clerks book hearings so quickly… / Around 900 magistrates across forty cour… / Listing, finance and judicial office lea…|
|`pmp-pr2-137`|process-2|matching|agile|3|Prompts: Items vary widely in size, and some lose… / The team needs to know which features su… / A launch date is fixed, and stakeholders…|
|`pmp-pr2-138`|process-2|pulldown|hybrid|3|Prompts: The steering committee funds the capabil… / A driver suggests that proof of delivery… / Installing the extension's loading-bay d…|
|`pmp-pr2-139`|process-2|fillblank|agile|3|A fitness app team has six sprints left before a fixed launch date. Its last eight sprints delivered between 34 and 42 points…|
|`pmp-pr2-140`|process-2|hotspot|predictive|2|The context diagram for a café chain's new loyalty system shows five external interfaces. Each label states how the draft scope…|
|`pmp-pr2-1`|pmp-lectures-1-11|single|agile|4|A Scrum team is halfway to its release. During the sprint demo, new software is selected by one of the developers and presented…|
|`pmp-pr2-2`|pmp-lectures-1-11|single|hybrid|3|During a Scrum board review for a hybrid project, a team member suggests adding a task that is outside of the baseline scope.…|
|`pmp-pr2-3`|pmp-lectures-1-11|single|agile|3|During an iteration, the team spent twice as much time as expected because one of the product backlog items was complex and could…|
|`pmp-pr2-4`|pmp-lectures-1-11|single|predictive|4|A project manager has been asked to manage an existing complex project. Some goals have already been delivered by the previous…|
|`pmp-pr2-5`|pmp-lectures-1-11|single|predictive|3|A project manager has developed a schedule for a project involving various departments. The project manager met with each team…|
|`pmp-pr2-6`|pmp-lectures-1-11|single|agile|3|A customer reviewed a product and requested additional features that were not in the requirements documentation. The…|
|`pmp-pr2-7`|pmp-lectures-1-11|single|predictive|3|Part of a project has already been delivered as planned and on schedule. However, unclear requirements for some of the remaining…|
|`pmp-pr2-8`|pmp-lectures-1-11|single|predictive|3|There are concerns that a scheduled-for-release product may not meet the agreed-upon end user requirements.|
|`pmp-pr2-201`|pmp-mocks-1-10|single|agile|2|A project manager is working on an agile project with a cross-functional team. The project manager is informed that some team…|
|`pmp-pr2-202`|pmp-mocks-1-10|single|predictive|3|A project manager received a complaint that the project team did not respond correctly to a client's request. However, the…|
|`pmp-pr2-203`|pmp-mocks-1-10|single|predictive|3|After nine months, a project concludes its first phase with the release of a new product to the customer. The client is…|
|`pmp-pr2-204`|pmp-mocks-1-10|single|predictive|3|The quality management plan (QMP) calls for regression testing before each product release, but these tests were not included in…|
|`pmp-pr2-205`|pmp-mocks-1-10|single|hybrid|4|A project manager is asked to manage a project that will allow the company to comply with a new, upcoming regulation. Just after…|
|`pmp-pr2-206`|pmp-mocks-1-10|single|predictive|3|During acceptance testing of a deliverable, some senior stakeholders complain that a feature essential to their department was…|
|`pmp-pr2-207`|pmp-mocks-1-10|single|predictive|3|A new project manager has been assigned to a project during its execution stage. During a review of the work breakdown structure…|
|`pmp-pr2-208`|pmp-mocks-1-10|single|predictive|2|During the project planning phase, the stakeholders neglected to document an essential system feature. The client realizes the…|
|`pmp-pr2-209`|pmp-mocks-1-10|single|agile|2|An application developer took a story card to work that read "Customer can pay using credit card". After the implementation, the…|
|`pmp-pr2-210`|pmp-mocks-1-10|single|predictive|2|During the weekly meeting, team members are reviewing the tasks assigned by the Project Manager. A few team members are…|
|`pmp-pr2-211`|pmp-mocks-1-10|single|hybrid|3|A project manager is working with a team to develop work packages. The project manager noticed a gap in the requirements…|
|`pmp-pr2-212`|pmp-mocks-1-10|single|predictive|2|A key stakeholder has submitted several requests for new functions for a product under development. The project manager just…|
|`pmp-pr2-213`|pmp-mocks-1-10|single|hybrid|3|A transportation company is transforming its operational processes and the main system this company uses to move freight. The…|
|`pmp-pr2-214`|pmp-mocks-1-10|single|predictive|3|A project is entering the user acceptance testing phase. After several business end users raised questions and concerns, the…|
|`pmp-pr2-215`|pmp-mocks-1-10|single|predictive|2|A project manager is hired by an organization and is given a project that has a vague scope and unclear deliverable requirements.|
|`pmp-pr2-216`|pmp-mocks-1-10|single|predictive|3|During a periodic review with stakeholders, the project sponsor asks how the project will measure satisfactory completion of the…|
|`pmp-pr2-217`|pmp-mocks-1-10|single|agile|1|A project manager was assigned to deliver new software for university students. In this project, the software is being delivered…|
|`pmp-pr2-218`|pmp-mocks-1-10|single|agile|2|A project manager is assigned to a project that is transitioning the organization to adopt agile principles. The project scope…|
|`pmp-pr2-219`|pmp-mocks-1-10|multi|hybrid|3|During a status meeting, a team member alerts the project manager that the delivery timeline for a component prototype is…|
|`pmp-pr2-220`|pmp-mocks-1-10|single|predictive|2|The research and development team is planning to build a new facility that will focus on improving existing products the…|
|`pmp-pr2-221`|pmp-mocks-1-10|single|predictive|1|Following the approval of the project charter, the project manager begins working with the team to organize the project's scope…|
|`pmp-pr2-222`|pmp-mocks-1-10|single|agile|1|A project manager is leading the development of a new customer support software system. During a sprint, a developer encounters…|
|`pmp-pr2-223`|pmp-mocks-1-10|single|agile|1|During the development of a new healthcare software system, the project manager requests that the project owner prioritize…|
|`pmp-pr2-224`|pmp-mocks-1-10|single|predictive|2|During a healthcare facility upgrade project, a project manager is informed by a key stakeholder that certain advanced…|
|`pmp-pr2-225`|pmp-mocks-1-10|single|predictive|3|In the critical validation stage of a software development project, a third-party component fails to meet the expected…|
|`pmp-pr2-226`|pmp-mocks-1-10|single|predictive|2|During a project review, the client identifies a crucial feature in the new software application that was discussed but not…|
|`pmp-pr2-227`|pmp-mocks-1-10|single|predictive|3|In the midst of a software development project, the project manager notices that the software's features are not aligning with…|
|`pmp-pr2-228`|pmp-mocks-1-10|single|predictive|3|To counteract the issue of certain team members disproportionately influencing the outcome of project scope discussions, the…|
|`pmp-pr2-229`|pmp-mocks-1-10|single|predictive|1|When the project manager of a software development project finds that the specifications provided by the client are ambiguous,…|
|`pmp-pr2-230`|pmp-mocks-1-10|single|predictive|2|You have collected the requirements for your project, matched them to project scope, and broken them down into smaller parts and…|
|`pmp-pr2-231`|pmp-mocks-1-10|single|agile|2|You are working on an agile project where the functional area you are delivering to only has a high level idea of the system…|
|`pmp-pr2-232`|pmp-mocks-1-10|single|agile|2|You are an Agile project manager and have begun creating user stories with your Agile project team to go into the product…|
|`pmp-pr2-233`|pmp-mocks-1-10|single|predictive|1|You are working on a project to build a new parkland for the community. The person in charge of parkland design approaches you as…|
|`pmp-pr2-234`|pmp-mocks-1-10|single|agile|2|You are working in a project team that has recently moved to an Agile way of work. Your business analysts want to gather the…|
|`pmp-pr2-235`|pmp-mocks-1-10|single|agile|2|You are working as a Product Owner, and have placed high level information about the product in the product backlog, and the…|
|`pmp-pr2-236`|pmp-mocks-1-10|single|agile|2|You are gathering requirements for a new feature in a customer management system. The product owner has given a user interface…|
|`pmp-pr2-237`|pmp-mocks-1-10|single|agile|1|You are working in an agile team who are trying to break down the project requirements and scope. They have created a scope…|
|`pmp-pr2-238`|pmp-mocks-1-10|single|agile|1|You are half way through your current sprint of two weeks. The work is progressing well, however you notice that there is not…|
|`pmp-pr2-239`|pmp-mocks-1-10|single|predictive|1|A new executive has started in the functional area you are delivering to. They are concerned the project is not delivering what…|
|`pmp-pr2-240`|pmp-mocks-1-10|single|predictive|2|You are in the planning stages of your project, where you need to find out exactly what the project customer needs so you can…|
|`pmp-pr2-241`|pmp-mocks-1-10|single|predictive|2|You are planning your project and have captured the requirements from the project customer. You ask your team to plan the project…|
|`pmp-pr2-242`|pmp-mocks-1-10|single|agile|1|You have created a high level view of the features your project will deliver, and prioritised them. The development team is ready…|
|`pmp-pr2-243`|pmp-mocks-1-10|single|agile|2|You are working on an Agile project, gathering requirements from the business stakeholders with the Product Owner. During the…|
|`pmp-pr2-244`|pmp-mocks-1-10|single|agile|2|The product owner has worked with the development team to list all work to be done, write up user stories for each feature and…|
|`pmp-pr2-245`|pmp-mocks-1-10|single|agile|2|You are the Agile project manager working through the product requirements for the product owner and executive project sponsor.…|
|`pmp-pr2-246`|pmp-mocks-1-10|single|agile|1|When working in an Agile team it is common to use prototypes, demonstrations and reviews, wireframes and storyboards to elicit…|
|`pmp-pr2-247`|pmp-mocks-1-10|single|predictive|2|You are working through the scope of a newly kicked-off project, and the senior user is unsure on how the requirements and scope…|
|`pmp-pr2-248`|pmp-mocks-1-10|single|predictive|2|Your project team have just finished defining the scope of the project. They have matched the scope to the original requirements…|
|`pmp-pr2-249`|pmp-mocks-1-10|single|predictive|3|You have just taken over a project that is approximately half-way through, and has used around 75% of its budget. A change to one…|
|`pmp-pr2-250`|pmp-mocks-1-10|single|predictive|2|You have used a project charter and statement of work to create a project management plan. You present the plan to key…|
|`pmp-pr2-251`|pmp-mocks-1-10|single|predictive|2|Your project charter has recently been approved, and you are gathering the customer requirements that will form the basis of your…|
|`pmp-pr2-252`|pmp-mocks-1-10|single|predictive|1|You have been planning your project using a waterfall methodology. The project team have collected the requirements from the…|
|`pmp-pr2-253`|pmp-mocks-1-10|single|agile|1|You are working with your Agile team on your first iteration. The feature has been chosen by the product owner and the team…|
|`pmp-pr2-254`|pmp-mocks-1-10|single|agile|1|You are working on the product backlog with your Agile project team, and creating user stories to be prioritised.|
|`pmp-pr2-255`|pmp-mocks-1-10|single|hybrid|1|You have been assigned to a project in your organization, and have been given a list of high-level features or deliverables that…|
