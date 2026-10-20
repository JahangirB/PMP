# Question ledger — process-4 · Resources

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

### process-4 · Resources — 129 items

*Plan for the people and materials the work needs, then keep them available.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr4-001`|core|single|predictive|3|Your schedule shows a specialist welder required at 180 percent of availability across weeks 9 to 14. The client contract fixes…|
|`pmp-pr4-002`|core|single|agile|3|Four of your six developers are also assigned to two other projects, at nominally 40 percent each. Velocity is erratic, sprint…|
|`pmp-pr4-003`|core|pulldown|predictive|3|Prompts: Cabling twelve floors of an office the c… / A migration nobody in the organisation h… / Installing 400 identical access points,…|
|`pmp-pr4-004`|core|single|agile|2|A manager has asked you to raise your team's utilisation from about seventy per cent to ninety-five, arguing that the difference…|
|`pmp-pr4-005`|core|single|predictive|3|Your project is four weeks behind at the end of month five of twelve. The sponsor offers to fund three additional developers,…|
|`pmp-pr4-006`|core|fillblank|predictive|3|A test automation task is estimated at 96 person-hours. The only qualified engineer works a 40-hour week but is available to your…|
|`pmp-pr4-007`|core|single|agile|2|Your product team's most experienced developer is assigned to it at 50 per cent and to an on-call support rota for the rest of…|
|`pmp-pr4-008`|core|single|predictive|3|Your resource plan assumes two database specialists from a shared pool for the whole of the build phase. Six weeks before build…|
|`pmp-pr4-014`|core|single|predictive|3|You are refurbishing the departure lounge of an operating regional airport. A new national security rule requires every delivery…|
|`pmp-pr4-010`|core|pulldown|agile|3|Prompts: For the team that lost a developer and d… / For the team that kept its people and ha…|
|`pmp-pr4-011`|core|single|agile|2|Your team develops firmware for a handheld scanner and works in two-week iterations. The next three iterations need twelve…|
|`pmp-pr4-012`|core|single|predictive|3|Your schedule assumes six weeks in the company's environmental test chamber starting in week 22. You have just learned that…|
|`pmp-pr4-013`|core|multi|agile|3|Your product team of seven has committed to and missed its last five iteration goals. Tracking where time actually went, you find…|
|`pmp-case3-007`|case-03|single|agile|3|Kofi Mensah, the only interface specialist, is allocated half-time to an electronic prescribing project. The integration team's…|
|`pmp-case5-006`|case-05|single|hybrid|3|About 400 advisers need training before the second centre moves. Training rooms and trainers are limited, and the winter storm…|
|`pmp-pr4-101`|process-4|single|predictive|1|You are scheduling a town-centre resurfacing project. The scheduling tool applied its default calendar of seven days a week to…|
|`pmp-pr4-102`|process-4|single|predictive|3|Your plan for a hospital's patient-records migration estimated the data-mapping tasks at 60 days because Priya, a senior analyst…|
|`pmp-pr4-103`|process-4|single|hybrid|4|Your drug-formulation project and a colleague's stability-study project both need the research site's only high-resolution mass…|
|`pmp-pr4-104`|process-4|single|predictive|3|On a hospital wing rewiring project, the electrical subcontractor has installed 40% of the planned circuits but has drawn 58% of…|
|`pmp-pr4-105`|process-4|single|predictive|5|Your ERP replacement for a logistics firm needs a finance-systems specialist for nine months, starting with configuration…|
|`pmp-pr4-106`|process-4|single|agile|3|Your agile team building a claims portal waits an average of 40 minutes for each build on its single build server. Developers now…|
|`pmp-pr4-107`|process-4|single|agile|3|Your retail analytics team has one data engineer, Sven. Whenever he is away, data-pipeline stories stall and the iteration goal…|
|`pmp-pr4-108`|process-4|single|agile|4|You coach three stable agile teams in an insurer's digital unit. The engineering resource manager proposes moving one developer…|
|`pmp-pr4-109`|process-4|single|hybrid|3|On a medical-device programme, the agile software teams are on track but the predictive hardware workstream is waiting for a…|
|`pmp-pr4-110`|process-4|single|hybrid|2|A contract test analyst joins your hybrid benefits-system project next Monday to support the next three iterations. On the…|
|`pmp-pr4-111`|process-4|single|agile|2|You are the scrum master for a team building a payroll app for a water utility. Mid-iteration, the head of finance emails one of…|
|`pmp-pr4-112`|process-4|single|predictive|3|Your water-main tunnelling project relies on a single tunnel boring machine. The maintenance plan includes a ten-day overhaul at…|
|`pmp-pr4-113`|process-4|multi|agile|3|A logistics company has just moved to agile teams. The new development manager, who previously ran a predictive department, wants…|
|`pmp-pr4-114`|process-4|multi|predictive|4|You are planning a 14-month rail signalling upgrade. Its testing phase, months 10 to 13, needs six testers holding a specific…|
|`pmp-pr4-115`|process-4|multi|hybrid|3|On a hybrid programme replacing a retailer's ordering system, four agile teams and a predictive data-migration workstream share…|
|`pmp-pr4-116`|process-4|matching|predictive|2|Prompts: You need to see, week by week, where dem… / Two supervisors each believe they are th… / The cost estimator wants every labour, e…|
|`pmp-pr4-117`|process-4|matching|agile|3|Prompts: Two of the seven developers will be on a… / A story planned two iterations from now… / Halfway through the iteration, the team…|
|`pmp-pr4-118`|process-4|pulldown|predictive|3|Prompts: Concrete blocks are available from three… / The main transformer has a 30-week lead… / Scaffolding is needed for eight weeks on…|
|`pmp-pr4-119`|process-4|fillblank|hybrid|2|The data-labelling workstream of a hybrid machine-learning programme needs 3,600 person-hours of labelling within 20 working…|
|`pmp-pr4-120`|process-4|hotspot|agile|4|The board shows your agile team's workflow midway through an iteration. Throughput has fallen for two iterations, although every…|
|`pmp-pr4-121`|process-4|single|predictive|1|You manage the ground investigation for an onshore wind farm. A drilling rig and its two-person crew are hired at €2,100 a day…|
|`pmp-pr4-122`|process-4|single|predictive|4|You are building a two-storey extension at a secondary school, due to finish before the autumn term. The plan has been…|
|`pmp-pr4-123`|process-4|single|predictive|3|You are commissioning a new water treatment works. Ana, a process engineer seconded from the operations department, asks you to…|
|`pmp-pr4-124`|process-4|single|predictive|5|Your consultancy is designing a £14m pharmaceutical cold store. The client will hold weekly design reviews for the first four…|
|`pmp-pr4-125`|process-4|multi|predictive|4|To recover a delay on your data-centre fit-out, the electrical installation crew has worked twelve-hour shifts, six days a week,…|
|`pmp-pr4-126`|process-4|fillblank|predictive|2|On a 60-home housing development near Leipzig, the bricklaying gangs use 35 bags of mortar mix each working day. The merchant…|
|`pmp-pr4-127`|process-4|matching|predictive|2|Prompts: At concept stage you have only the total… / Your firm's records show commissioning t… / Detailed design is complete and the comm…|
|`pmp-pr4-128`|process-4|hotspot|predictive|3|Your bridge-strengthening project is scheduled using critical chain. The diagram shows the critical chain and one feeding chain.…|
|`pmp-pr4-129`|process-4|single|agile|3|Your team building a travel-booking app has four developers in Dublin and four in Manila, with one hour of overlapping working…|
|`pmp-pr4-130`|process-4|single|agile|3|Your team building a ticketing platform for a ferry operator includes three developers supplied by an outsourcing partner under a…|
|`pmp-pr4-131`|process-4|single|agile|2|Facilities has announced that from next month your claims-automation team's floor will become hot-desking, shared with two other…|
|`pmp-pr4-132`|process-4|single|agile|3|Your team builds a pharmacy chain's prescription-ordering app and tests on emulators. Across the last three releases, 11 of the…|
|`pmp-pr4-133`|process-4|single|agile|4|A retailer's sponsor must decide how many developers to fund for a new loyalty platform. The initial backlog is estimated at…|
|`pmp-pr4-134`|process-4|multi|agile|3|You are helping a resource manager form a new team to build a council's online parking-permit service. The work covers user…|
|`pmp-pr4-135`|process-4|single|hybrid|3|Your hybrid programme is replacing a building society's mortgage-servicing system. Five operations staff are seconded at 60% to…|
|`pmp-pr4-136`|process-4|single|hybrid|2|On a hybrid programme developing smart electricity meters, three firmware teams share twelve hardware debug probes that took six…|
|`pmp-pr4-137`|process-4|single|hybrid|4|Your hybrid programme is introducing a new point-of-sale system in 48 shops. The software is built iteratively; the rollout plan,…|
|`pmp-pr4-138`|process-4|multi|hybrid|3|Your programme for a regional bus operator has finished its predictive design phase. Delivery now moves to three agile software…|
|`pmp-pr4-139`|process-4|matching|hybrid|3|Prompts: Which developer on an agile team picks u… / Whether a seconded registry analyst's mi… / Which of two features waiting for the sh… / Whether to increase the programme budget…|
|`pmp-pr4-140`|process-4|pulldown|agile|3|Prompts: Accessibility checks are now needed on a… / The next two iterations carry about thre… / The team's mainframe specialist has had…|
|`pmp-pr4-1`|export-batch-1|single|predictive|2|A project manager noticed that one team member is frequently absent from project team meetings. Instead of working on the current…|
|`pmp-pr4-2`|export-batch-1|single|predictive|5|Due to budgetary constraints, the program manager has requested that project managers prioritize resources over schedule…|
|`pmp-pr4-3`|export-batch-1|single|predictive|3|An inexperienced project manager requires HR support for a project. The HR manager hired interns and sent them for training.…|
|`pmp-pr4-4`|export-batch-1|single|predictive|3|In the middle of a project, the project manager realizes that a specific set of skills is needed to complete project development.…|
|`pmp-pr4-5`|export-batch-1|single|agile|3|During a standup meeting, the project manager discovers that one of the software developers is struggling to complete work…|
|`pmp-pr4-6`|export-batch-1|single|agile|3|A key member of an agile team was replaced with a less experienced member. One of the stakeholders is concerned that the project…|
|`pmp-pr4-7`|export-batch-1|single|agile|4|A Scrum team noticed a trend of work spilling over for the last several sprints. The root cause of this has been identified as…|
|`pmp-pr4-8`|export-batch-1|single|predictive|3|During initial project planning, a project manager was informed that a team member had been exclusively allocated to the project.…|
|`pmp-pr4-9`|export-batch-1|single|predictive|3|A project manager is leading a team of software engineers in developing a new mobile app. The team consistently meets deadlines…|
|`pmp-pr4-10`|export-batch-1|single|hybrid|3|The project manager of a new hybrid project is trying to put together the project team. The best candidates for team members are…|
|`pmp-pr4-201`|export-batch-2|single|agile|2|During week two of a two-week sprint, one of the core team members has an emergency and is unable to work on the project for the…|
|`pmp-pr4-202`|export-batch-2|single|predictive|3|A key resource approaches the project manager after the kick-off meeting and expresses concern that they will be unable to…|
|`pmp-pr4-203`|export-batch-2|single|agile|3|During an iteration, a key project team member announces their resignation. The project manager realizes that one of the product…|
|`pmp-pr4-204`|export-batch-2|single|predictive|1|A project manager has just been assigned a new project team and needs to assign tasks for an upcoming project quickly and…|
|`pmp-pr4-205`|export-batch-2|single|predictive|3|A project is in a key phase of execution and everyone is dedicated to achieving success. A functional manager suddenly takes away…|
|`pmp-pr4-206`|export-batch-2|single|hybrid|4|Two project managers are working on resourcing their respective projects. Both project managers identified the same subject…|
|`pmp-pr4-207`|export-batch-2|single|predictive|3|A project is currently behind schedule when a new team member, known for their high performance, is assigned to the team. While…|
|`pmp-pr4-208`|export-batch-2|single|hybrid|3|A project manager is leading a hybrid project. Several team members recently exited the project, including the only individual…|
|`pmp-pr4-209`|export-batch-2|single|agile|3|A project team working on a high-priority, change-driven project has repeatedly missed release deadlines. The project manager…|
|`pmp-pr4-210`|export-batch-2|single|predictive|3|A project manager is assigned to a top innovation project that has high visibility with the company executives. Recently, one of…|
|`pmp-pr4-211`|export-batch-2|single|predictive|2|A project execution phase overlaps with a holiday in the country where the majority of team members are based. This phase is…|
|`pmp-pr4-212`|export-batch-2|single|agile|3|A project team in the middle of an incremental delivery project decided they need the help of a subject matter expert (SME) who…|
|`pmp-pr4-213`|export-batch-2|single|predictive|2|A key project team member is leaving the organization at the halfway point of the project.|
|`pmp-pr4-214`|export-batch-2|single|predictive|3|A project manager is working on a complex project with many stakeholders and conflicting priorities. Senior management asked the…|
|`pmp-pr4-215`|export-batch-2|single|agile|2|A customer requested a change in functionality for a product that will be delivered in the next sprint. The responsible…|
|`pmp-pr4-216`|export-batch-2|single|predictive|3|A project manager learns that a functional manager is assigning a two-week task in another project to a team member. Loss of this…|
|`pmp-pr4-217`|export-batch-2|single|predictive|3|A new project manager has been assigned to a global project team. In the next few days, the project team must create a resource…|
|`pmp-pr4-218`|export-batch-2|single|predictive|3|In the implementation phase of a project, the project manager receives news that a team lead will be leaving the organization.…|
|`pmp-pr4-219`|export-batch-2|single|predictive|2|A new warehouse facility is under construction. The project manager implements team development efforts such as training that…|
|`pmp-pr4-220`|export-batch-2|single|predictive|2|During project execution, a project manager learns that a team member's functional manager wants to remove them from a project…|
|`pmp-pr4-221`|export-batch-2|single|hybrid|2|A multinational company is locally developing a digital product using a new technology platform. The only expert on this platform…|
|`pmp-pr4-222`|export-batch-2|single|predictive|2|A project manager wants to recruit another team member to replace an architect who recently resigned.|
|`pmp-pr4-223`|export-batch-2|single|predictive|2|A project manager is facing a delay in a project due to a decision made by the functional manager to provide onsite support for…|
|`pmp-pr4-224`|export-batch-2|single|agile|2|A customer requested a change to a functionality that is being delivered in the next iteration. The specialist responsible for…|
|`pmp-pr4-225`|export-batch-2|single|predictive|2|A project team is arranging user testing to be completed for a project but does not have a strong enough server to do so. The…|
|`pmp-pr4-226`|export-batch-2|single|hybrid|2|During the planning of a project with incremental deliveries, the team realizes that they will need specific expertise for one of…|
|`pmp-pr4-227`|export-batch-2|single|predictive|2|A functional manager wants to use resources already allocated to a project.|
|`pmp-pr4-228`|export-batch-2|single|predictive|2|While developing the initial schedule baseline, the project manager learns that a functional manager reassigned two resources…|
|`pmp-pr4-229`|export-batch-2|single|predictive|3|A project manager has been assigned to a newly approved multi-national project. The successful implementation of the project…|
|`pmp-pr4-230`|export-batch-2|single|predictive|3|A project is currently halfway through its timeline when one of the external resources becomes unexpectedly unavailable for the…|
|`pmp-pr4-231`|export-batch-2|single|hybrid|2|A complex project is in the planning stage when the sponsor identifies a need for a specialist who is a citizen of a foreign…|
|`pmp-pr4-232`|export-batch-2|single|agile|2|A project has the following characteristics: The project manager is being asked to deliver a business result in a compressed…|
|`pmp-pr4-233`|export-batch-2|single|predictive|2|A team member is scheduled to visit an overseas client site next week. However, the project manager overheard the team member…|
|`pmp-pr4-234`|export-batch-2|single|predictive|2|A project manager is working on a small project that is critical for a larger program milestone. A new resource has been assigned…|
|`pmp-pr4-235`|export-batch-2|single|predictive|2|A project is following a strategic plan that includes key milestones that have been aligned with stakeholders. As the work is…|
|`pmp-pr4-236`|export-batch-2|single|predictive|2|Close to the end of a large project, some of the technical reports related to the delivered service were not ready. The project…|
|`pmp-pr4-237`|export-batch-2|single|predictive|2|In a multidisciplinary project, a project manager is delegating some project management activities to several team members.|
|`pmp-pr4-238`|export-batch-2|single|agile|3|An organization is adopting an agile approach to fulfill its customer's requirements. Prior to that, the organization worked with…|
|`pmp-pr4-239`|export-batch-2|single|predictive|3|During project execution, a project manager is informed that some resources will soon be removed from the project.|
|`pmp-pr4-240`|export-batch-2|single|predictive|2|A project manager works in a matrix organization. Following a reorganization, several key project resources are reassigned to…|
|`pmp-pr4-241`|export-batch-2|single|predictive|2|A project manager receives complaints from a project sponsor because of a delay in developing a prototype. The project manager…|
|`pmp-pr4-242`|export-batch-2|single|predictive|3|A project manager is experiencing issues with the functional manager of a project team member. The functional manager has given…|
|`pmp-pr4-243`|export-batch-2|multi|agile|3|For an agile project, some team members have other commitments or responsibilities. The project manager faces the challenge of…|
|`pmp-pr4-244`|export-batch-2|single|agile|3|A project team has been efficiently delivering results. The product upgrade they are working on must be released in three months…|
|`pmp-pr4-245`|export-batch-2|single|predictive|3|A project manager is overseeing a large-scale infrastructure project in a hierarchical organization. The project team members are…|
|`pmp-pr4-246`|export-batch-2|single|predictive|2|In the middle of an urban development project, the project manager faces a setback as a key engineer and an urban planner…|
|`pmp-pr4-247`|export-batch-2|single|hybrid|2|As a project manager overseeing a large-scale renewable energy project, you're confronted with the sudden need to transition your…|
|`pmp-pr4-248`|export-batch-2|single|predictive|2|A project manager is tasked with integrating advanced artificial intelligence (AI) features into an existing software product.…|
|`pmp-pr4-249`|export-batch-2|single|predictive|2|In the implementation stage of a critical network security project, it is revealed that a specialist responsible for key parts of…|
|`pmp-pr4-250`|export-batch-2|single|predictive|2|You are managing your team through difficult project, which has been made more difficult by many team members being new and…|
|`pmp-pr4-251`|export-batch-2|single|hybrid|1|You are working on a project where the project team is completely distributed around the country in multiple sites, including…|
|`pmp-pr4-252`|export-batch-2|single|agile|2|You are in the planning stages of an Agile project where the functional manager and senior users have provided the high level…|
|`pmp-pr4-253`|export-batch-2|single|predictive|3|You are putting together a project team to deliver an accounting system to offices around the country. The accounting system you…|
|`pmp-pr4-254`|export-batch-2|single|predictive|2|You put together a resource plan for a large amount of physical inventory to be purchased from overseas. You ordered the…|
|`pmp-pr4-255`|export-batch-2|single|agile|2|You have been working on an Agile project for some time and have been blocked by the need for expertise in another team. You need…|
|`pmp-pr4-256`|export-batch-2|single|predictive|1|You have finished project planning. All baseline documents are approved and the project team is ready to begin project execution…|
|`pmp-pr4-257`|export-batch-2|single|predictive|2|You are in the process of planning the resources required for your project. Work on your project has not started, as your team…|
|`pmp-pr4-258`|export-batch-2|single|predictive|2|You have been planning your project using a waterfall methodology, and need to estimate the resources required with your team.…|
|`pmp-pr4-259`|export-batch-2|single|predictive|2|You have been working on a project where your organisation wanted only onsite workers, while you believed the project needed…|
|`pmp-pr4-260`|export-batch-2|single|predictive|3|You have been assigned to a project that is behind schedule. Some of your team approach you and ask what they should be working…|
|`pmp-pr4-261`|export-batch-2|single|predictive|2|Team members from parts of the organisation have been assigned to your project at an agreed level of 50% availability. As the…|
|`pmp-pr4-262`|export-batch-2|single|agile|2|The executive management in your organisation has assembled a new team, and dictated that they will use Agile for their product…|
|`pmp-pr4-263`|export-batch-2|single|agile|2|You are a program manager overseeing multiple Agile projects within your organization. One of the projects is very important but…|
|`pmp-pr4-264`|export-batch-2|single|predictive|2|You are in the initiating stages of a project. The Project Charter has been approved, and initial funding released. You have been…|
