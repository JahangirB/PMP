# Question ledger — people-8 · Communication

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

### people-8 · Communication — 153 items

*Plan communication, keep it transparent, and report so sponsors can act.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p8-001`|core|multi|hybrid|3|Your weekly project report goes to forty-one people. You learn that the steering group reads only the first paragraph, the…|
|`pmp-p8-002`|core|single|predictive|2|You have just confirmed that a critical supplier will miss a milestone, which will delay your project by around a month. Your…|
|`pmp-p8-003`|core|multi|predictive|3|Your sponsor has twice made decisions in steering that contradict information sent in the weekly status report, most recently…|
|`pmp-p8-004`|core|single|agile|2|Your team works across three time zones with a two-hour overlap. Decisions taken during the overlap are not reaching the…|
|`pmp-p8-005`|core|single|predictive|5|A data migration you ran over the weekend has corrupted a subset of customer records. Your technical lead is confident it affects…|
|`pmp-p8-006`|core|fillblank|predictive|2|Your project team currently has 6 people who all need to coordinate directly with one another. The sponsor wants to add 4 more to…|
|`pmp-p8-015`|core|single|hybrid|3|Your hybrid programme is replacing the expense-claims system for a manufacturer with plants in five countries. Cutover is fixed…|
|`pmp-p8-008`|core|multi|agile|3|Your fortnightly written status report runs to five pages and takes half a day to produce. Analytics on the shared drive show two…|
|`pmp-p8-016`|core|single|predictive|4|You are building a flood defence wall along a market town's high street. The town council's chair has written to your sponsor…|
|`pmp-p8-017`|core|fillblank|predictive|2|A refinery turnaround employs 640 contractor staff, and a revised permit-to-work rule takes effect on Monday. The site safety…|
|`pmp-p8-018`|core|single|agile|3|Your agile team of seven used to sit together. Since an office move to bookable hot desks, the five developers sit on the third…|
|`pmp-p8-012`|core|multi|predictive|3|Your programme's business case assumes around forty roles will be removed once the new system is live. No announcement has been…|
|`pmp-p8-013`|core|single|hybrid|4|You submit your monthly programme report to the programme director, who consolidates it for the board. A board member mentions in…|
|`pmp-p8-014`|core|single|agile|2|Your delivery team of fourteen works across two sites, and for eleven of them English is a second language. You communicate…|
|`pmp-case1-009`|case-01|matching|predictive|2|Prompts: The environmental regulator, ahead of a… / The operators on each shift during a shu… / Martin, as sponsor, between board meetin…|
|`pmp-case2-007`|case-02|single|agile|2|Parking services managers have stopped attending sprint reviews and now hear about progress second-hand, sometimes inaccurately.…|
|`pmp-case5-003`|case-05|pulldown|hybrid|2|Prompts: To tell Calder advisers how the consulta… / To keep the steering committee informed… / To agree the cutover details for the sec…|
|`pmp-p8-101`|people-8|single|predictive|1|Your team is building an electricity substation next to a primary school. Over the past fortnight the head teacher has phoned the…|
|`pmp-p8-102`|people-8|single|agile|2|After your claims-processing team moved to hybrid working, its wall board was replaced by a board in an online tool. The…|
|`pmp-p8-103`|people-8|single|agile|3|Attendance at your mobile banking app's sprint reviews has fallen from twelve business stakeholders to three over the last four…|
|`pmp-p8-104`|people-8|single|predictive|4|You are managing the build of a council leisure centre. Your schedule risk analysis gives a 50% chance of completing by 3 March…|
|`pmp-p8-105`|people-8|multi|hybrid|3|On your ERP rollout, the six workstream leads each set their own red, amber or green status in the fortnightly report. Twice this…|
|`pmp-p8-106`|people-8|matching|hybrid|2|Prompts: Four heads of service with competing pri… / All 450 repairs staff must know that the… / Repairs operatives need step-by-step gui…|
|`pmp-p8-107`|people-8|single|hybrid|5|Your laboratory relocation programme reports to its sponsor monthly. Last month the validation of the cold-storage suite slipped…|
|`pmp-p8-108`|people-8|single|hybrid|3|Your bank's lead architect added a line to the quarterly board pack: 'Technical debt in the payments integration layer is…|
|`pmp-p8-109`|people-8|fillblank|agile|2|Five agile teams on a ticketing platform hold a cross-team coordination call every working day. It has grown to 16 attendees for…|
|`pmp-p8-110`|people-8|single|agile|2|A developer who is deaf has joined your distributed product team. The team holds its daily stand-up, refinement and retrospective…|
|`pmp-p8-111`|people-8|single|predictive|3|Your contractor is replacing a rail bridge, and the traffic management plan makes the project responsible for notifying affected…|
|`pmp-p8-112`|people-8|single|predictive|4|You manage the £48 million build of a new wing for a hospital trust. At 14:00 a local journalist phones you directly. She has a…|
|`pmp-p8-113`|people-8|multi|predictive|3|Halfway through a nuclear decommissioning project, the site's security team issues a new rule: technical documents may no longer…|
|`pmp-p8-114`|people-8|matching|predictive|3|Prompts: The structural engineer sent the port's… / The site supervisor briefed thirty opera… / The project manager emailed the steel su…|
|`pmp-p8-115`|people-8|pulldown|agile|3|Prompts: For the customer service team, who will… / For the finance director, who signs off… / For the warehouse returns team, who unpa…|
|`pmp-p8-116`|people-8|hotspot|predictive|3|On a hotel refurbishment, a ductwork subcontractor installed to a superseded drawing on Thursday, although the revised drawing…|
|`pmp-p8-117`|people-8|single|agile|4|Your team is building a claims portal for an insurance client under a time-and-materials contract. At the client's request, the…|
|`pmp-p8-118`|people-8|single|predictive|5|Your steering group meets in 30 minutes to make the go-live decision for a new warehouse management system. The pack you…|
|`pmp-p8-119`|people-8|single|hybrid|3|Your infusion pump project has a hardware workstream run to a stage-gated plan and a firmware workstream run in sprints. In the…|
|`pmp-p8-120`|people-8|multi|agile|3|Your agile team is building a student enrolment app for a university. Admissions and finance staff have started sending requests…|
|`pmp-p8-121`|people-8|single|predictive|1|You are managing the supply of a bottling line to a drinks manufacturer. On a phone call this morning, the client's project…|
|`pmp-p8-122`|people-8|single|agile|2|The head of engineering, who line-manages most of your agile team, has asked you to forward him the full notes from each…|
|`pmp-p8-123`|people-8|single|agile|3|Your agile team's chat workspace receives about 250 automated messages a day from the build server, the issue tracker and the…|
|`pmp-p8-124`|people-8|single|predictive|4|Your firm is delivering a €6 million building management system upgrade under a contract that requires a formal monthly progress…|
|`pmp-p8-125`|people-8|single|hybrid|3|Your hybrid programme depends on an overseas engineering supplier delivering a control module by 30 June. In this morning's video…|
|`pmp-p8-126`|people-8|single|hybrid|3|On your hybrid fibre-network rollout, the heads of field operations and of customer service disagree about when customers should…|
|`pmp-p8-127`|people-8|single|predictive|2|You are managing the relocation of a 1,800-person tax agency office to a new building. The communications plan relies on a…|
|`pmp-p8-128`|people-8|single|agile|3|Your agile team builds the customer app for an energy retailer. The PMO requires every project to submit a monthly report in its…|
|`pmp-p8-129`|people-8|single|agile|4|The sponsor of a charity's donation platform has started attending your agile team's daily stand-up. She asks each developer for…|
|`pmp-p8-130`|people-8|single|hybrid|5|Your hybrid programme relies on a specialist analytics supplier. Over dinner, a contact at another firm tells you in confidence…|
|`pmp-p8-131`|people-8|single|predictive|4|You manage the £120 million build of a desalination plant for a joint venture between a water utility and an energy company. Each…|
|`pmp-p8-132`|people-8|single|predictive|3|On a new university science building, this afternoon's change control board rejected the chemistry department's request for a…|
|`pmp-p8-133`|people-8|multi|predictive|3|On an airport baggage-handling replacement, two steering group members disputed at this month's meeting what had been decided…|
|`pmp-p8-134`|people-8|multi|agile|3|Your agile team builds farm management software. The product owner is the only person who talks to farmers; she writes detailed…|
|`pmp-p8-135`|people-8|multi|hybrid|4|Your hybrid project is replacing a publisher's editorial system. For a week, team members have been saying that the project is…|
|`pmp-p8-136`|people-8|matching|agile|3|Prompts: The commercial director asks whether eve… / The operations manager wants to see wher… / A revenue manager wants to know whether…|
|`pmp-p8-137`|people-8|matching|predictive|2|Prompts: Explaining to the client organisation's… / Telling the scaffolding crew, before tom… / Telling an individual engineer that her…|
|`pmp-p8-138`|people-8|pulldown|hybrid|3|Prompts: Notifying the cloud supplier that it has… / Checking with a developer, mid-sprint, w… / Presenting the case for a revised go-liv…|
|`pmp-p8-139`|people-8|fillblank|agile|2|An agile product has three teams. Team Manila works 01:00–09:30 UTC, team Lisbon 08:00–16:30 UTC, and team Denver 15:00–23:30…|
|`pmp-p8-140`|people-8|hotspot|predictive|3|You are building a vaccine fill-finish facility. The site's licence requires the regulator to be notified within five working…|
|`pmp-p8-1`|pmp-lectures-1-11|single|predictive|4|A project manager is working on a virtual project with a global team for the first time and encounters an issue with stakeholder…|
|`pmp-p8-2`|pmp-lectures-1-11|single|predictive|2|A company has launched a new strategic project. The goal and scope of the project were loosely defined at the executive committee…|
|`pmp-p8-3`|pmp-lectures-1-11|single|agile|2|A virtual team is midway through a sprint of a complex task and is ahead of schedule. During the daily standup, a new team member…|
|`pmp-p8-4`|pmp-lectures-1-11|single|predictive|3|The project manager discovers that a stakeholder provides inaccurate status reports to the steering committee. Moreover, the…|
|`pmp-p8-5`|pmp-lectures-1-11|single|predictive|3|In a large organization, a project is experiencing communication difficulties. Team members and functional leaders are expressing…|
|`pmp-p8-6`|pmp-lectures-1-11|single|agile|3|A team is working on an innovation project using an incremental approach. After every status meeting, some key stakeholders…|
|`pmp-p8-7`|pmp-lectures-1-11|single|predictive|4|A stakeholder is concerned that the project is falling behind schedule, based on unofficial information from the project team.…|
|`pmp-p8-8`|pmp-lectures-1-11|single|predictive|3|During the execution phase of a healthcare IT project, the customer's project manager resigns and has not been replaced. The…|
|`pmp-p8-9`|pmp-lectures-1-11|single|predictive|4|A key project sponsor requires monthly progress reports and assigns a contact person to communicate with the project manager.…|
|`pmp-p8-10`|pmp-lectures-1-11|single|predictive|3|A project manager realizes that an email intended for a specific team member has been mistakenly sent to the entire global…|
|`pmp-p8-11`|pmp-lectures-1-11|single|predictive|3|Although a project is performing within its time, scope, and budget constraints, the project manager learns that the project…|
|`pmp-p8-12`|pmp-lectures-1-11|single|agile|3|The night before a standup meeting, a project manager realizes that they will be unable to attend the daily standup meeting in…|
|`pmp-p8-13`|pmp-lectures-1-11|single|predictive|3|As agreed at the start of a predictive project, all outgoing information should be shared beforehand with the project manager.…|
|`pmp-p8-14`|pmp-lectures-1-11|single|predictive|3|A project is halfway complete, and despite a good schedule performance index (SPI), the customer has identified several minor…|
|`pmp-p8-15`|pmp-lectures-1-11|single|predictive|3|A project manager has created the stakeholder list and is deciding how to distribute information effectively to the customer.|
|`pmp-p8-201`|pmp-mocks-1-10|single|agile|3|A pilot project is underway using an agile approach to development. The project is delivering the planned increments on time, but…|
|`pmp-p8-202`|pmp-mocks-1-10|single|agile|3|An agile project manager based in country A is working with a development team and business analyst in country B, and a quality…|
|`pmp-p8-203`|pmp-mocks-1-10|single|predictive|2|A strategic project has been launched and a project manager has been assigned. The project is progressing, and the team is…|
|`pmp-p8-204`|pmp-mocks-1-10|single|agile|3|A large-scale project has been consistently progressing. However, the team is experiencing difficulties in completing some…|
|`pmp-p8-205`|pmp-mocks-1-10|single|agile|1|At an agile project meeting, a team member gives an update on the status of their important task. A new team member interrupts…|
|`pmp-p8-206`|pmp-mocks-1-10|single|agile|2|A new team is formed for an agile project, in which members are located in three different countries and five time zones.|
|`pmp-p8-207`|pmp-mocks-1-10|single|predictive|2|A project manager communicates with a key stakeholder via phone calls only, as indicated in the communication management plan.…|
|`pmp-p8-208`|pmp-mocks-1-10|single|agile|1|A project manager is working with remote team members who are acting independently.|
|`pmp-p8-209`|pmp-mocks-1-10|single|hybrid|2|In a review meeting, a project manager detects a misunderstanding about one critical deliverable.|
|`pmp-p8-210`|pmp-mocks-1-10|single|hybrid|2|A project manager has been assigned a project that involves a virtual, multicultural team including representatives from various…|
|`pmp-p8-211`|pmp-mocks-1-10|single|agile|1|A new project involving team members from two countries has just begun. Due to a time difference, one site finishes work an hour…|
|`pmp-p8-212`|pmp-mocks-1-10|single|hybrid|1|A global, cross-functional team has been assembled to deliver a multinational initiative.|
|`pmp-p8-213`|pmp-mocks-1-10|single|hybrid|3|A project manager works for a company involved in complex turnkey projects. During a team assessment, the project manager finds…|
|`pmp-p8-214`|pmp-mocks-1-10|single|hybrid|3|A new project manager is assigned to a project in the execution phase. The project just completed the deliverables for the first…|
|`pmp-p8-215`|pmp-mocks-1-10|single|hybrid|2|A project team using a hybrid approach is producing both predictive and incremental deliverables simultaneously. The…|
|`pmp-p8-216`|pmp-mocks-1-10|single|hybrid|3|A project management organization has a project that leverages waterfall for the requirements gathering phase and agile for…|
|`pmp-p8-217`|pmp-mocks-1-10|single|predictive|2|A project manager learned that the project deliverables will not meet the quality requirements unless a change is made to a…|
|`pmp-p8-218`|pmp-mocks-1-10|multi|hybrid|3|A project manager working in a global organization decides to use a low-cost social media platform to exchange information,…|
|`pmp-p8-219`|pmp-mocks-1-10|single|agile|2|A project team is actively engaged in the preparation of the last delivery before the final product is live in production.|
|`pmp-p8-220`|pmp-mocks-1-10|single|agile|2|A company begins to implement Scrum. About halfway through the first sprint, communication issues develop. Some project team…|
|`pmp-p8-221`|pmp-mocks-1-10|single|predictive|2|A project management team is developing a project management information system (PMIS) as a collaboration tool for internal and…|
|`pmp-p8-222`|pmp-mocks-1-10|single|hybrid|3|Stakeholders in a multinational project are experiencing cross-cultural misunderstandings. A seemingly small cultural…|
|`pmp-p8-223`|pmp-mocks-1-10|single|hybrid|3|In an industrial plant, the owner has decided to add more capacity to the plant by installing a new machine and updating the…|
|`pmp-p8-224`|pmp-mocks-1-10|single|predictive|2|In the company cafeteria, a project manager overhears an unengaged stakeholder mocking the style of language used in the project…|
|`pmp-p8-225`|pmp-mocks-1-10|single|hybrid|2|A project manager is leading a project that is being implemented simultaneously in two countries with an 11-hour time zone…|
|`pmp-p8-226`|pmp-mocks-1-10|single|predictive|2|A project manager has just started building the team for a huge and complex project. The project manager is highly interested in…|
|`pmp-p8-227`|pmp-mocks-1-10|single|predictive|3|What can a project manager do to communicate the formal project announcement and relevant information to stakeholders, and then…|
|`pmp-p8-228`|pmp-mocks-1-10|single|predictive|1|A project requires the services of a specialized vendor to install a very complex and expensive piece of equipment. The project…|
|`pmp-p8-229`|pmp-mocks-1-10|single|predictive|2|A stakeholder requests more frequent and detailed project status reports than a previous agreement. The stakeholder also…|
|`pmp-p8-230`|pmp-mocks-1-10|single|predictive|2|A complex manufacturing project has multiple stakeholders in multiple locations. The project is entering a high-activity phase…|
|`pmp-p8-231`|pmp-mocks-1-10|single|predictive|2|A project manager learns that a stakeholder thinks that only certain stakeholders are getting all the project updates and feels…|
|`pmp-p8-232`|pmp-mocks-1-10|single|predictive|2|The project sponsors for a multinational project have put a process in place that generates emails and reports at the end of each…|
|`pmp-p8-233`|pmp-mocks-1-10|single|predictive|2|During a project's execution phase, two stakeholders complain that they receive insufficient information on the project's status.…|
|`pmp-p8-234`|pmp-mocks-1-10|multi|agile|3|A project manager for a cross-functional team realizes that there is a lack of commitment from stakeholders to the project…|
|`pmp-p8-235`|pmp-mocks-1-10|single|hybrid|2|A project manager for a cross-functional team is finding it difficult to schedule regular meetings and communicate with all team…|
|`pmp-p8-236`|pmp-mocks-1-10|single|agile|2|A global organization that has always used predictive project management is now moving towards agile project management. In the…|
|`pmp-p8-237`|pmp-mocks-1-10|single|predictive|2|A construction project is in the execution phase when several new team members from different countries are assigned to the…|
|`pmp-p8-238`|pmp-mocks-1-10|single|predictive|2|As a result of a drastic cut to the budget, a project's steering committee is replaced with a single sponsor.|
|`pmp-p8-239`|pmp-mocks-1-10|single|predictive|2|A project manager must get approval for a complex project management plan from multiple stakeholders in various countries before…|
|`pmp-p8-240`|pmp-mocks-1-10|single|agile|2|A project manager in Location A works with a local team, while developers are in another remote location, and a quality assessor…|
|`pmp-p8-241`|pmp-mocks-1-10|single|hybrid|2|A project manager wants to ensure that virtual team meetings are effective.|
|`pmp-p8-242`|pmp-mocks-1-10|single|agile|3|An efficient, empowered agile innovation team does NOT understand why required approvals from the legal department are allowed to…|
|`pmp-p8-243`|pmp-mocks-1-10|single|predictive|1|A colocated project team is facing communication and collaboration issues. Some team members have roles that require them to work…|
|`pmp-p8-244`|pmp-mocks-1-10|single|agile|2|An agile project is in the first of six iterations. The team is located globally, so the meetings are in a virtual environment.…|
|`pmp-p8-245`|pmp-mocks-1-10|single|predictive|1|After emailing a recent project status report, the project manager learns that a number of recipients are no longer involved with…|
|`pmp-p8-246`|pmp-mocks-1-10|single|hybrid|2|A project manager is leading a new implementation project with a global project team. As team members are in different…|
|`pmp-p8-247`|pmp-mocks-1-10|single|agile|2|As part of the project sprint review, stakeholders appear uninformed and are unable to agree on how to move the project forward.|
|`pmp-p8-248`|pmp-mocks-1-10|single|predictive|2|A project’s single sponsor has been replaced by a steering committee due to a significant increase in project size.|
|`pmp-p8-249`|pmp-mocks-1-10|single|hybrid|2|A project team, distributed across three different countries, is having a difficult time arranging meetings. The team members are…|
|`pmp-p8-250`|pmp-mocks-1-10|single|hybrid|2|A company has decided to utilize a diverse international team that will work virtually. This will be the first time that the team…|
|`pmp-p8-251`|pmp-mocks-1-10|single|agile|2|An adaptative project team is globally distributed and the team members speak different primary languages. Daily meetings are…|
|`pmp-p8-252`|pmp-mocks-1-10|single|hybrid|2|A geographically distributed project team disagrees about status reporting. Some team members prefer detailed reports, while…|
|`pmp-p8-253`|pmp-mocks-1-10|single|predictive|1|During the rollout of a new IT system within a large corporation, the project manager observes that discussions in stakeholder…|
|`pmp-p8-254`|pmp-mocks-1-10|single|predictive|2|A project manager is leading a city park renovation project. During a status meeting with city officials, the project manager…|
|`pmp-p8-255`|pmp-mocks-1-10|single|agile|2|On a high-stakes software development agile project, the project manager notices a pattern where numerous team members are either…|
|`pmp-p8-256`|pmp-mocks-1-10|single|hybrid|3|In spearheading a groundbreaking environmental initiative within a large organization, the project manager faces the challenge of…|
|`pmp-p8-257`|pmp-mocks-1-10|single|predictive|2|During the execution phase of an urban development project, the project manager uses email updates to keep all stakeholders in…|
|`pmp-p8-258`|pmp-mocks-1-10|single|predictive|3|In the midst of a project's lifecycle, a project manager identifies that essential feedback from a vital operational division has…|
|`pmp-p8-259`|pmp-mocks-1-10|single|agile|1|As a project manager initiates a project with a fully remote team, ensuring effective communication becomes a challenge.|
|`pmp-p8-260`|pmp-mocks-1-10|single|agile|2|In an initiative to manage a software development team spread across multiple continents, a project manager seeks to enhance…|
|`pmp-p8-261`|pmp-mocks-1-10|single|predictive|2|In the process of deploying a new customer relationship management (CRM) system, the project manager realizes that the updates…|
|`pmp-p8-262`|pmp-mocks-1-10|single|hybrid|2|During the integration phase of an international IT project, the project manager notices frequent miscommunications between the…|
|`pmp-p8-263`|pmp-mocks-1-10|single|predictive|2|During the implementation phase of a citywide public transportation improvement project, a city council member requests a…|
|`pmp-p8-264`|pmp-mocks-1-10|single|agile|2|Leading a software development agile project for a multinational corporation, a project manager faces the challenge of managing a…|
|`pmp-p8-265`|pmp-mocks-1-10|single|predictive|1|To enhance project communication and address emerging issues, a project manager organizes bi-weekly meetings with the development…|
|`pmp-p8-266`|pmp-mocks-1-10|single|predictive|1|In the midst of a complex IT project, feedback from an executive sponsor indicates dissatisfaction with the detailed nature of…|
|`pmp-p8-267`|pmp-mocks-1-10|single|predictive|1|You are managing a project which communicates to the customer using mostly pull communication so far. When monitoring the…|
|`pmp-p8-268`|pmp-mocks-1-10|single|predictive|2|You have taken over on a project with a vendor where scope and requirements have been missed. You notice that miscommunication…|
|`pmp-p8-269`|pmp-mocks-1-10|single|predictive|2|You are putting together a project plan for a change that affects nearly ten thousand stakeholders in your organisation. You have…|
|`pmp-p8-270`|pmp-mocks-1-10|single|predictive|1|Part of your project management plan is a detailed communications plan, which has the stakeholders on the project, their…|
|`pmp-p8-271`|pmp-mocks-1-10|single|predictive|2|You are working as a project manager on a regulatory change for your organisation. You notice that the team are not communicating…|
|`pmp-p8-272`|pmp-mocks-1-10|single|agile|2|You are working as a project manager with an Agile project team. The project sponsor sits on another floor, and prefers to…|
|`pmp-p8-273`|pmp-mocks-1-10|single|agile|1|You are working as a project manager in a team that is moving to an Agile way of work. You have previously engaged the team in a…|
|`pmp-p8-274`|pmp-mocks-1-10|single|predictive|2|You are a project manager working in the initial phases of a project, and trying to put together a document for your stakeholders…|
|`pmp-p8-275`|pmp-mocks-1-10|single|predictive|2|The project manager on your project recently left, and you agreed to see the project through as it is nearly complete. The…|
|`pmp-p8-276`|pmp-mocks-1-10|single|predictive|2|Your project involves nearly a dozen teams who are dispersed around the country at different sites, with thousands of impacted…|
|`pmp-p8-277`|pmp-mocks-1-10|single|agile|1|You have joined a new project team and are moving to an Agile way of work. Part of this is co-locating the team to gain the…|
|`pmp-p8-278`|pmp-mocks-1-10|single|agile|1|You are working with an Agile team who are not co-located. They are in various locations around the country.|
|`pmp-p8-279`|pmp-mocks-1-10|single|agile|1|The twelve Agile clarifying principles also match up with the best advice for projects, leadership and communication.|
|`pmp-p8-280`|pmp-mocks-1-10|single|agile|1|You are working in an organization that is new to Agile, and have just built your initial team to start your project. The team is…|
|`pmp-p8-281`|pmp-mocks-1-10|single|agile|2|You have been working on a high-profile project with a large amount of business executives who will gain benefit from what you…|
