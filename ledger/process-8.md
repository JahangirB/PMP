# Question ledger — process-8 · Schedule

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

### process-8 · Schedule — 137 items

*Build, baseline and manage a schedule that fits the delivery approach.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr8-001`|core|fillblank|predictive|3|In the schedule model, activity D has an early start of day 12 and a late start of day 19. All other activities are unchanged.…|
|`pmp-pr8-002`|core|hotspot|predictive|2|The diagram shows the sequence this project follows to produce a schedule baseline. Select the step at which durations and…|
|`pmp-pr8-003`|core|single|agile|2|Your team has failed to complete everything it committed to in four of the last five sprints, each time by about 20 percent. The…|
|`pmp-pr8-014`|core|single|predictive|4|Your satellite ground station build has four independent paths — civil works, antenna procurement, power supply and control…|
|`pmp-pr8-005`|core|fillblank|predictive|4|A site handover has two independent chains of work that must both finish before commissioning. Chain one runs survey, four days,…|
|`pmp-pr8-006`|core|single|predictive|4|Your programme must recover six weeks. Two options are on the table. Overlapping design and construction on the critical path…|
|`pmp-pr8-007`|core|fillblank|predictive|3|Your lead engineer estimates a commissioning activity at 12 days if everything goes well, 30 days in the worst realistic case,…|
|`pmp-pr8-008`|core|single|predictive|4|Your critical path runs through design, then build, then commissioning, and you must recover four weeks. Design is complete.…|
|`pmp-pr8-015`|core|fillblank|predictive|3|A 12-month ferry terminal upgrade has just reached the end of month 6. Cumulative planned value was £1,200,000 at the end of…|
|`pmp-pr8-010`|core|hotspot|predictive|4|The diagram shows the five sequential stages of your delivery with their planned durations. You have been asked to shorten the…|
|`pmp-pr8-011`|core|fillblank|predictive|4|Your critical path runs through two activities that can be shortened by adding shifts. Activity A can be shortened by up to 3…|
|`pmp-pr8-012`|core|single|agile|3|Reviewing your team's estimates, you find each developer adds private safety to their own items so that they are almost never…|
|`pmp-pr8-013`|core|single|predictive|4|An approved change added twelve days to your commissioning chain, which previously carried eight days of float and was not…|
|`pmp-pr8-061`|core|graphic|predictive|3|The network diagram shows the five activities left on your plant installation, with their durations and predecessors. The finish…|
|`pmp-case1-010`|case-01|single|predictive|4|Martin asks how the fourth filter bank could still be in service before the winter peak. Its remaining activities are civil…|
|`pmp-case2-001`|case-02|fillblank|agile|3|Using Exhibit 2, work out the average velocity of the last three sprints, then the number of whole sprints needed at that pace to…|
|`pmp-case4-001`|case-04|single|predictive|3|The fabricator tells you the display cases will be delivered ten working days late.|
|`pmp-pr8-101`|process-8|single|predictive|2|You are scheduling a 40-home refurbishment programme. Kitchen fitting in block C and bathroom fitting in block D both need the…|
|`pmp-pr8-102`|process-8|single|agile|1|Your six-person team building an insurance claims app has delivered 42, 45 and 40 story points in its last three two-week…|
|`pmp-pr8-103`|process-8|single|hybrid|3|Your medical imaging programme combines a stage-gated hardware build with three Scrum teams writing the control software. The PMO…|
|`pmp-pr8-104`|process-8|single|predictive|3|Reviewing the schedule for a hospital's new MRI suite, you notice that the link between 'Shielding design approved' and…|
|`pmp-pr8-105`|process-8|single|agile|3|Three Scrum teams are building features for a bank's new savings app. The delivery director wants a combined release date and has…|
|`pmp-pr8-106`|process-8|single|predictive|5|At month seven of a £24 million railway signalling upgrade, the schedule performance index has fallen to 0.84 for the third month…|
|`pmp-pr8-107`|process-8|fillblank|agile|3|A game studio's team has 312 story points left in its release backlog. Its last six sprint velocities were 34, 41, 38, 29, 44 and…|
|`pmp-pr8-108`|process-8|single|hybrid|2|Your programme is replacing a retailer's warehouse management system: infrastructure is delivered in planned phases, and the…|
|`pmp-pr8-109`|process-8|matching|predictive|3|Prompts: Cable pull-in at a turbine cannot start… / The installation contractor prefers to f… / Array cable laying cannot begin until th…|
|`pmp-pr8-110`|process-8|single|agile|4|Your team's release burnup for a telecoms self-service portal shows completed work rising steadily at around 30 points per…|
|`pmp-pr8-111`|process-8|multi|predictive|3|Before a water treatment works upgrade is baselined, you run a quality check on the planner's 640-activity schedule. The summary…|
|`pmp-pr8-112`|process-8|single|agile|3|In sprint 6 of a ten-sprint release for a ticketing platform, your team learns that the payment provider's test environment will…|
|`pmp-pr8-113`|process-8|pulldown|hybrid|3|Prompts: Work for the next six months is understo… / A discovery workstream cannot be estimat… / Parliament has set the date by which the…|
|`pmp-pr8-114`|process-8|single|predictive|1|On a museum gallery refit, the display cases arrived two weeks late and three installation activities have slipped. Your planner…|
|`pmp-pr8-115`|process-8|hotspot|predictive|4|The network diagram shows part of an office fit-out schedule, with each activity's duration, total float and free float. A…|
|`pmp-pr8-116`|process-8|single|agile|5|A council's digital services team works in Kanban and has finished about eight items a week for months. To show every department…|
|`pmp-pr8-117`|process-8|multi|hybrid|3|Your hybrid programme is fitting out a distribution centre: conveyor hardware follows a predictive plan, and two Scrum teams…|
|`pmp-pr8-118`|process-8|single|predictive|4|Excavation for a new flood defence wall uncovers an uncharted Victorian culvert, which nobody had identified as a risk. Diverting…|
|`pmp-pr8-119`|process-8|multi|agile|2|A newly formed Scrum team at a logistics firm has finished its first two sprints. Stakeholders want a release date. You notice…|
|`pmp-pr8-120`|process-8|matching|hybrid|3|Prompts: Installing 180,000 meters across regions… / Building the customer energy app, whose… / Handling installation faults and custome…|
|`pmp-pr8-121`|process-8|single|predictive|1|You manage the construction of a vaccine filling plant, and the schedule holds 2,400 activities. The chair of the investment…|
|`pmp-pr8-122`|process-8|single|predictive|4|On a £14 million airport baggage-handling upgrade, the activity 'Write site acceptance test procedures' is on the critical path…|
|`pmp-pr8-123`|process-8|single|predictive|4|At this week's status update for a distribution warehouse, the planner reports 'Install pallet racking' as 40 percent complete,…|
|`pmp-pr8-124`|process-8|single|predictive|3|Your refinery's 40-day maintenance turnaround is scheduled using critical chain, with aggressive task estimates and a 12-day…|
|`pmp-pr8-125`|process-8|single|predictive|3|Your university's student-records migration includes a 40-day data cleansing activity with 4 days of total float. After 20…|
|`pmp-pr8-126`|process-8|single|agile|1|Your team is estimating stories for a charity's donations app using planning poker. For a story that adds Gift Aid declarations,…|
|`pmp-pr8-127`|process-8|single|agile|3|Your team at an online furniture retailer runs two-week sprints. In five of the last six sprints, one or two large stories were…|
|`pmp-pr8-128`|process-8|single|agile|3|Three teams build software for an electric vehicle charging network. The payments team runs one-week sprints starting Mondays,…|
|`pmp-pr8-129`|process-8|single|agile|5|Your team must ship a pensions provider's member dashboard before a regulatory deadline in four sprints. Velocity has been steady…|
|`pmp-pr8-130`|process-8|single|hybrid|4|Your hybrid programme is developing a smart home thermostat. The control-board chips have a 26-week lead time, and the supplier…|
|`pmp-pr8-131`|process-8|single|hybrid|3|A housing association is replacing its payroll system, and go-live is fixed at the start of the tax year on 6 April. The cutover,…|
|`pmp-pr8-132`|process-8|single|hybrid|2|Your hybrid programme for a building society runs a data-centre migration on a predictive plan, alongside a customer platform…|
|`pmp-pr8-133`|process-8|multi|predictive|2|You are reviewing the schedule for a multi-storey car park. The precast concrete supplier's factory closes for the first three…|
|`pmp-pr8-134`|process-8|multi|agile|3|Your team builds an insurance broker's quoting portal and also handles production support. Planned velocity is 40 points, but…|
|`pmp-pr8-135`|process-8|multi|hybrid|3|Your ERP rollout covers 12 countries in predictive deployment waves, with an agile team configuring local variants before each…|
|`pmp-pr8-136`|process-8|matching|hybrid|3|Prompts: At the feasibility stage, the only infor… / Electricians will install 3,200 metres o… / A first-of-its-kind suspended planetariu…|
|`pmp-pr8-137`|process-8|matching|agile|2|Prompts: A rule change means accessibility featur… / On day four of a sprint, a developer fin… / The marketing lead asks for a new promot…|
|`pmp-pr8-138`|process-8|pulldown|predictive|3|Prompts: Hundreds of short activities, such as in… / Resurfacing is laid lane-kilometre by la… / Pavement design lasts twelve weeks and p…|
|`pmp-pr8-139`|process-8|fillblank|predictive|3|A 1.2 km sewer replacement is planned with overlapping activities. Trenching takes 12 days and starts at the beginning of day 1.…|
|`pmp-pr8-140`|process-8|hotspot|agile|4|A publisher's digital team works in Kanban, and its delivery forecasts have slipped for six weeks. Each band of the cumulative…|
|`pmp-pr8-1`|pmp-lectures-1-11|single|agile|4|An agile project is midway through its ten iteration cycles. During the last iteration, the team had to work overtime to…|
|`pmp-pr8-2`|pmp-lectures-1-11|single|predictive|2|A project manager is planning for a complex project that must deliver a high-quality product within the agreed-upon budget. The…|
|`pmp-pr8-3`|pmp-lectures-1-11|single|agile|3|At a review meeting, the project manager learns about unclear requirements that may impact the project's scheduled completion.…|
|`pmp-pr8-4`|pmp-lectures-1-11|single|hybrid|4|A project manager works in an organization using a hybrid project delivery approach. The scope of the project is to implement a…|
|`pmp-pr8-5`|pmp-lectures-1-11|single|agile|3|An agile project team is building a website for a client. The client informs the product owner that their company has decided to…|
|`pmp-pr8-6`|pmp-lectures-1-11|single|predictive|3|A team member is unable to work for one week because of sick leave. The team member is currently assigned to a task on the…|
|`pmp-pr8-7`|pmp-lectures-1-11|single|predictive|3|A project manager has a new project. The work breakdown structure (WBS) and sequence of activities have been defined, but the…|
|`pmp-pr8-8`|pmp-lectures-1-11|single|agile|4|A company is expanding its product manufacturing to a new country. The date the company will start operating is set and broadly…|
|`pmp-pr8-9`|pmp-lectures-1-11|single|predictive|4|A project manager is required to obtain approval for project requirements before proceeding to the delivery stage. During the…|
|`pmp-pr8-10`|pmp-lectures-1-11|multi|predictive|4|A project manager is working in the fifth month of an implementation for a construction project. The planner reported that the…|
|`pmp-pr8-201`|pmp-mocks-1-10|single|predictive|3|During a project schedule review meeting, the project team identified that one of the critical path activities has slipped due to…|
|`pmp-pr8-202`|pmp-mocks-1-10|single|agile|2|A key deadline is approaching specialized team member has suddenly received news that they must leave the office for two weeks.…|
|`pmp-pr8-203`|pmp-mocks-1-10|single|hybrid|3|During a hybrid project, a project manager reviews the project team's performance and realizes that the team's velocity is not…|
|`pmp-pr8-204`|pmp-mocks-1-10|single|predictive|2|During the planning phase, a project manager is creating the project schedule. The designated project team is finishing an…|
|`pmp-pr8-205`|pmp-mocks-1-10|single|predictive|3|A project manager has identified that a task on the critical path is behind schedule. The project manager thinks that additional…|
|`pmp-pr8-206`|pmp-mocks-1-10|single|agile|3|An information technology (IT) project manager was assigned a new team for a software development project in an agile…|
|`pmp-pr8-207`|pmp-mocks-1-10|single|agile|3|A project is estimated to have a total of 250 story points. The team planned 40 story points for each iteration and delivered 40,…|
|`pmp-pr8-208`|pmp-mocks-1-10|single|predictive|3|A construction project has progressed for about 18 months, and the schedule performance index (SPI) is 0.68. The project manager…|
|`pmp-pr8-209`|pmp-mocks-1-10|single|agile|4|An intern is replacing a critical resource midway through a sprint. To help the intern get up to speed, the project manager…|
|`pmp-pr8-210`|pmp-mocks-1-10|multi|predictive|3|A project team is working on determining dependencies for approximately 350 activities for an ongoing project. (Choose 2).|
|`pmp-pr8-211`|pmp-mocks-1-10|single|agile|3|A project manager is leading an agile project that is in the execution phase. After a couple of sprints, the project manager…|
|`pmp-pr8-212`|pmp-mocks-1-10|single|predictive|3|An impact assessment reveals that a deliverable could be delayed by two weeks. A key resource has not been available to work on a…|
|`pmp-pr8-213`|pmp-mocks-1-10|single|agile|2|An agile team has been struggling to keep up with the planned pace, which is frequently resulting in sprint failures.|
|`pmp-pr8-214`|pmp-mocks-1-10|single|agile|2|A team has been working for two months and has consistently missed planned user stories.|
|`pmp-pr8-215`|pmp-mocks-1-10|single|predictive|2|A project team is working on determining the dependencies for activities for an ongoing project.|
|`pmp-pr8-216`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a team that is developing a new software product. The deadline for the product launch is…|
|`pmp-pr8-217`|pmp-mocks-1-10|single|predictive|3|A project's statement of work (SOW) requires that the project be delivered in nine months. The project manager believes that the…|
|`pmp-pr8-218`|pmp-mocks-1-10|single|predictive|2|After receiving the project charter, a project manager starts to prepare the project management plan. Although there is a limited…|
|`pmp-pr8-219`|pmp-mocks-1-10|single|predictive|2|A project is showing a negative schedule variance. The project manager sees that one team is causing the delays.|
|`pmp-pr8-220`|pmp-mocks-1-10|single|agile|3|A project manager is planning the next release for the software development team. The team has completed two releases to date,…|
|`pmp-pr8-221`|pmp-mocks-1-10|multi|predictive|3|A project manager is managing a long-term project that is behind schedule. The client is concerned that the project will be…|
|`pmp-pr8-222`|pmp-mocks-1-10|single|predictive|3|One day before starting a task, the technical lead proposed a new implementation approach that could reduce the schedule by one…|
|`pmp-pr8-223`|pmp-mocks-1-10|single|agile|3|An experienced project manager joins an ongoing project and wants to estimate the amount of time required to tackle several…|
|`pmp-pr8-224`|pmp-mocks-1-10|multi|agile|2|A project manager is preparing for a sprint planning session for a new feature in a healthcare management system. This meeting…|
|`pmp-pr8-225`|pmp-mocks-1-10|single|predictive|2|A project manager has begun a complex airport expansion project. Although the project is in the initiation phase, the sponsor…|
|`pmp-pr8-226`|pmp-mocks-1-10|single|predictive|2|On an infrastructure project to upgrade city water systems, the project manager is struggling to finalize the project schedule…|
|`pmp-pr8-227`|pmp-mocks-1-10|single|agile|2|In a software development project, as the team moves into the development phase, they decide to utilize time tracking to estimate…|
|`pmp-pr8-228`|pmp-mocks-1-10|single|predictive|2|During the deployment phase of a new telecommunications network, the project encounters significant delays.|
|`pmp-pr8-229`|pmp-mocks-1-10|single|predictive|2|A key stakeholder requests that the project manager deliver a critical infrastructure component two weeks ahead of schedule. The…|
|`pmp-pr8-230`|pmp-mocks-1-10|single|predictive|2|In the midst of implementing a new healthcare IT system, a project manager faces a challenge. The initial phase, involving the…|
|`pmp-pr8-231`|pmp-mocks-1-10|single|agile|2|As the project manager for an app development project, you are approached by a stakeholder with a request to add a feature that…|
|`pmp-pr8-232`|pmp-mocks-1-10|single|predictive|3|During the critical phase of a green energy plant construction project, it becomes evident that the project may not adhere to the…|
|`pmp-pr8-233`|pmp-mocks-1-10|single|hybrid|3|As the project manager overseeing the rollout of a new IT system, it's discovered halfway through the fiscal year that several…|
|`pmp-pr8-234`|pmp-mocks-1-10|multi|hybrid|3|In the early planning stages of a green building project, the project manager faces challenges with limited data available for…|
|`pmp-pr8-235`|pmp-mocks-1-10|single|predictive|1|During the critical testing phase of an enterprise software upgrade, the project manager identifies that testing is significantly…|
|`pmp-pr8-236`|pmp-mocks-1-10|single|predictive|3|After gathering your project requirements, project scope, and breaking it down into activities, you need to estimate the project…|
|`pmp-pr8-237`|pmp-mocks-1-10|single|agile|1|You have noted nine epics for delivery as part of your most recent project. Your team have broken down these epics into user…|
|`pmp-pr8-238`|pmp-mocks-1-10|single|predictive|2|You have taken over as Project Manager for a project that has had issues delivering on time. You review the Earned Value…|
|`pmp-pr8-239`|pmp-mocks-1-10|single|predictive|1|You are working on a project where you have been completing the deliverables in sequence, and the project work is starting to…|
|`pmp-pr8-240`|pmp-mocks-1-10|single|agile|2|Your team are new to agile, and had previously been working with Waterfall. Your key customer asks you for a project schedule.…|
|`pmp-pr8-241`|pmp-mocks-1-10|single|agile|2|You are working in an agile project team that has increased in size and complexity since the project began. In reviewing the…|
|`pmp-pr8-242`|pmp-mocks-1-10|single|agile|2|You are creating a business case and project charter with a small initial project team. You have a high level feature, decomposed…|
|`pmp-pr8-243`|pmp-mocks-1-10|single|predictive|2|You are adding an essential feature to your product roadmap. Your team believe they can complete the feature in six weeks,…|
|`pmp-pr8-244`|pmp-mocks-1-10|single|agile|1|You have a prioritised product backlog of features, and have decomposed the next feature to be delivered into user stories, small…|
|`pmp-pr8-245`|pmp-mocks-1-10|single|predictive|2|You are integrating a third party system as part of your project. The developers in your project team have not worked with this…|
|`pmp-pr8-246`|pmp-mocks-1-10|single|agile|1|You are a product owner on an agile project and have created a product backlog of features in a prioritised order for delivery.…|
|`pmp-pr8-247`|pmp-mocks-1-10|single|agile|1|You have worked with your project customer to create a list of features that will bring the most value to them. They would like…|
|`pmp-pr8-248`|pmp-mocks-1-10|single|agile|1|You are planning the next iteration in your Agile team, as the Agile lead. The team is going to select user stories from the…|
|`pmp-pr8-249`|pmp-mocks-1-10|single|agile|2|While on an Agile project team, you prioritise the backlog of features with the product owner, and agree what you will work on…|
|`pmp-pr8-250`|pmp-mocks-1-10|single|agile|1|You are working with your Agile project team to estimate each task and how long they might take. The team is finding it difficult…|
|`pmp-pr8-251`|pmp-mocks-1-10|single|agile|1|You are an Agile project manager and talking through the iteration with the product owner. They have created the list of items in…|
|`pmp-pr8-252`|pmp-mocks-1-10|single|predictive|1|You are working on a large multi-year project. You realise that the estimates from a year ago are extremely inaccurate, and the…|
|`pmp-pr8-253`|pmp-mocks-1-10|single|predictive|2|When reviewing the project performance with your team, you notice the following: 1) Activity A, on the critical path, is delayed…|
|`pmp-pr8-254`|pmp-mocks-1-10|single|predictive|2|You are working on a new waterfall project where the scope and cost have been planned up front. As the project progresses, you…|
|`pmp-pr8-255`|pmp-mocks-1-10|single|predictive|2|About a third of your project team have worked on a similar project in the past. They have gathered requirements and created a…|
|`pmp-pr8-256`|pmp-mocks-1-10|single|predictive|2|The people and resources involved in your project are currently costing a large amount of money, and your project sponsor…|
|`pmp-pr8-257`|pmp-mocks-1-10|single|agile|2|You are working through the backlog with the product owner and estimating the story cards with the development team. The team are…|
|`pmp-pr8-258`|pmp-mocks-1-10|single|agile|1|The business partner or product owner continuously refines, or grooms, the product backlog. This includes adding new stories,…|
|`pmp-pr8-259`|pmp-mocks-1-10|single|agile|2|You have been estimating story cards with your Agile project team, but the effort captured has not been matching the time it ends…|
|`pmp-pr8-260`|pmp-mocks-1-10|single|agile|1|You are delivering features through your Agile project and the project sponsor and product owner are happy with the progress.…|
|`pmp-pr8-261`|pmp-mocks-1-10|single|agile|1|Your Agile team is estimating story cards using planning poker. For one of the larger features, some of the team has estimated…|
|`pmp-pr8-262`|pmp-mocks-1-10|single|agile|1|You are working through a sprint planning meeting with your team. The team have sized up the cards and prioritised the backlog.|
|`pmp-pr8-263`|pmp-mocks-1-10|single|agile|2|You are working in a new Agile team and have reviewed the team velocity over the past three iterations. The maximum number of…|
|`pmp-pr8-264`|pmp-mocks-1-10|single|agile|1|You are running an Agile project and meet with the team for the next sprint planning meeting.|
|`pmp-pr8-265`|pmp-mocks-1-10|single|agile|1|The team are planning their first upcoming sprint on an Agile project. They have worked with the product owner who has gathered a…|
|`pmp-pr8-266`|pmp-mocks-1-10|single|hybrid|2|Your project team have been used to working in a Predictive or Waterfall way of work, planning everything up front. As your…|
|`pmp-pr8-267`|pmp-mocks-1-10|single|predictive|3|You are at the beginning of planning a new project, and have researched and gathered the requirements from the area, broken them…|
|`pmp-pr8-268`|pmp-mocks-1-10|single|agile|2|You are working on a project that has recently moved to a more agile way of work. The team have not completely taken on the Scrum…|
|`pmp-pr8-269`|pmp-mocks-1-10|single|hybrid|3|Your product owner wants to bring the next feature forward so that it is done in parallel to the current feature, but is still…|
|`pmp-pr8-270`|pmp-mocks-1-10|single|predictive|2|After reviewing your project schedule you notice you are behind by three weeks and the schedule will need to be compressed. Your…|
