# Question ledger — process-9 · Status and artifacts

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

### process-9 · Status and artifacts — 109 items

*Measure progress, keep artifacts current, and communicate where things stand.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr9-001`|core|multi|agile|3|Your organisation measures its nine agile teams on velocity and publishes a quarterly league table comparing them. Velocity…|
|`pmp-pr9-002`|core|single|predictive|2|Your status report has shown the project as amber for eight consecutive weeks, with the same commentary each week. The steering…|
|`pmp-pr9-003`|core|single|hybrid|3|Your programme has three predictive workstreams reporting earned value and two agile teams reporting burn-up and cycle time. All…|
|`pmp-pr9-004`|core|multi|hybrid|3|Your programme reports monthly using a status pack assembled by hand from six workstream spreadsheets. Assembly takes three days,…|
|`pmp-pr9-005`|core|single|agile|2|Your sprint burndown has been flat for eight days and then dropped almost vertically on the last day of the sprint, for three…|
|`pmp-pr9-006`|core|pulldown|hybrid|3|Prompts: A workstream has been reported amber for… / A workstream reported green last week is…|
|`pmp-pr9-007`|core|hotspot|predictive|2|The diagram shows the sequence your project follows each month to produce its performance report. A new sponsor has asked which…|
|`pmp-pr9-014`|core|single|hybrid|4|Your hybrid programme is replacing a regional energy supplier's billing platform, with a predictive data-migration workstream and…|
|`pmp-pr9-009`|core|multi|agile|2|Your team maintains a wall of charts showing velocity, burndown, defect counts and cycle time, updated automatically. Nobody…|
|`pmp-pr9-010`|core|multi|hybrid|3|Your programme reports on the first working day of each month. Your board meets on the third Tuesday, and its members say the…|
|`pmp-pr9-011`|core|single|agile|2|Your team maintains a task board in the team room and a project management tool that the PMO reads. The board is accurate and…|
|`pmp-pr9-012`|core|pulldown|hybrid|3|Prompts: For the board, deciding whether interven… / For the delivery teams, deciding what to… / For the operations department, deciding…|
|`pmp-pr9-013`|core|single|agile|2|Your team reports progress at standup as a percentage complete per item. Reviewing the last three iterations, you find items sat…|
|`pmp-pr9-061`|core|graphic|agile|2|Your team committed to 80 points for a ten-day sprint. The burndown shows the points remaining at each two-day checkpoint. At the…|
|`pmp-case1-002`|case-01|single|predictive|4|Martin has asked you to keep the board report positive while the project recovers. The earned value figures in Exhibit 1 point to…|
|`pmp-case2-010`|case-02|matching|agile|2|Prompts: Is the team on course to deliver the ess… / What is being worked on right now, and w… / How much has the team completed in each…|
|`pmp-case5-007`|case-05|multi|hybrid|3|The steering committee wants a single red, amber or green status for the project. Configuration is on track, the first centre's…|
|`pmp-pr9-101`|process-9|single|predictive|1|You manage the refit of a passenger ferry at a shipyard. At the weekly coordination meeting the hull, electrical and outfitting…|
|`pmp-pr9-102`|process-9|single|agile|2|Your team is building an online course platform for a training provider. Over the last three sprints the product owner has added…|
|`pmp-pr9-103`|process-9|single|hybrid|3|Your programme is upgrading a regional mobile network. The predictive site-build workstream relies on a supplier whose monthly…|
|`pmp-pr9-104`|process-9|single|predictive|3|On a rail signalling upgrade, work packages run for 12 to 16 weeks. Engineers report earned value as their own estimate of…|
|`pmp-pr9-105`|process-9|single|agile|3|Your agile team is replacing a claims system for an insurer. The new sponsor, used to construction projects, asks you to send a…|
|`pmp-pr9-106`|process-9|single|predictive|4|Your water treatment upgrade reports to the steering committee monthly. The pack is due tomorrow for Thursday's meeting, where…|
|`pmp-pr9-107`|process-9|single|agile|2|Your team publishes a release burn-up on the company intranet, forecasting a mid-May launch for a retailer's new checkout.…|
|`pmp-pr9-108`|process-9|single|hybrid|3|Reviewing the risk register for a pharmaceutical packaging line, you notice that the risk of late validation of the new labelling…|
|`pmp-pr9-109`|process-9|single|predictive|5|At 60% complete on an airport baggage-hall expansion, earned value analysis shows a CPI of 1.06 and an SPI of 0.97. The sponsor…|
|`pmp-pr9-110`|process-9|single|agile|4|Two experienced developers have moved from your benefits-portal team to another product. Over the three sprints since, velocity…|
|`pmp-pr9-111`|process-9|single|hybrid|3|Your programme is building an automated warehouse for a food distributor. The racking workstream is predictive and reports earned…|
|`pmp-pr9-112`|process-9|single|predictive|2|You are building a secondary school for a local authority, with four workstream leads each rating their area red, amber or green…|
|`pmp-pr9-113`|process-9|multi|agile|3|Your team builds a mortgage application app for a building society. Over five weeks the cumulative flow diagram shows the…|
|`pmp-pr9-114`|process-9|multi|predictive|3|On a bridge refurbishment for a regional road authority, your monthly report to the sponsor runs to 38 pages of site diaries,…|
|`pmp-pr9-115`|process-9|multi|hybrid|4|Seven months into replacing a university's student-records system, a newly appointed enterprise architect challenges the…|
|`pmp-pr9-116`|process-9|matching|predictive|2|Prompts: The lighting contractor asks whether the… / The exhibition planner asks what the sch… / The sponsor asks which unresolved proble…|
|`pmp-pr9-117`|process-9|matching|agile|3|Prompts: The product owner wants to know whether… / The team lead suspects tickets are pilin… / The operations manager wants to know how…|
|`pmp-pr9-118`|process-9|pulldown|hybrid|3|Prompts: At a sprint review, the product owner de… / The change control board approves a thre…|
|`pmp-pr9-119`|process-9|fillblank|agile|3|A council's digital services team runs a Kanban board. Over eight stable weeks it has averaged 30 items in progress at any one…|
|`pmp-pr9-120`|process-9|hotspot|predictive|4|The milestone trend chart for an offshore wind substation shows how the forecast dates of four milestones have moved over six…|
|`pmp-pr9-121`|process-9|single|predictive|1|You manage the steelwork and cladding packages for a new stadium roof. Your weekly progress report is ready to send at 4 pm, and…|
|`pmp-pr9-122`|process-9|single|agile|2|Your team is building an appointment-booking app for a chain of veterinary practices. Since the first sprint, developers have…|
|`pmp-pr9-123`|process-9|single|predictive|3|Your programme is converting three bus depots to electric fleets, one depot per phase. Lessons from phase 1 were written into its…|
|`pmp-pr9-124`|process-9|single|hybrid|3|Your organisation's PMO requires every project to produce 22 standard artifacts, from a benefits realisation plan to a…|
|`pmp-pr9-125`|process-9|single|predictive|4|Your company is building a data centre for a client under a contract that defines the RAG criteria for the monthly report: red…|
|`pmp-pr9-126`|process-9|single|predictive|5|Your £8 million flood defence scheme is 40% complete. The board has delegated a cost tolerance of ±5% of budget at completion;…|
|`pmp-pr9-127`|process-9|single|hybrid|3|Your hybrid programme is moving an airline's engine-maintenance planning onto a new platform. The programme dashboard pulls…|
|`pmp-pr9-128`|process-9|single|hybrid|2|You are running a programme to merge the IT systems of two housing associations after their merger. Steering group actions are…|
|`pmp-pr9-129`|process-9|single|agile|2|Your team is building a ticketing app for a regional theatre group. For four sprints, developers have mentioned at stand-up that…|
|`pmp-pr9-130`|process-9|single|agile|3|Your team builds farm-management software. The product roadmap, an internal artifact, shows features by quarter for the next 18…|
|`pmp-pr9-131`|process-9|single|agile|3|Your team maintains the booking engine for a hotel group. The PMO's monthly report quotes the product backlog as 'work…|
|`pmp-pr9-132`|process-9|single|agile|4|Your Kanban team builds systems for a port authority. Its dashboard reports a median cycle time of four days, and the harbour…|
|`pmp-pr9-133`|process-9|multi|predictive|2|You are managing a five-week maintenance shutdown at an oil refinery. The daily status meeting runs for two hours: each of 14…|
|`pmp-pr9-134`|process-9|multi|agile|3|Your team is building an e-lending app for a regional library service. Its definition of done requires code review, automated…|
|`pmp-pr9-135`|process-9|multi|hybrid|3|Your hybrid programme is upgrading a toll road's payment systems. A formal notice about planned lane closures went to the…|
|`pmp-pr9-136`|process-9|matching|agile|3|Prompts: A team with twelve sprints of steady vel… / A live-operations team works a Kanban fl… / A team formed this month, with no delive…|
|`pmp-pr9-137`|process-9|matching|hybrid|3|Prompts: The issued-for-construction drawings of… / The architecture decision records in whi… / The software teams' working agreement co…|
|`pmp-pr9-138`|process-9|pulldown|predictive|4|Prompts: The electrical package overran by €80,00… / The joinery package has run at a CPI of… / The ventilation package's original estim…|
|`pmp-pr9-139`|process-9|fillblank|predictive|4|A ten-month motorway resurfacing contract has cumulative planned value, in £000, of 60, 150, 260, 380, 500, 610, 700, 780, 850…|
|`pmp-pr9-140`|process-9|hotspot|predictive|3|The tracking Gantt for a hospice extension shows five activities. Each label gives how far the activity's forecast finish has…|
|`pmp-pr9-1`|export-batch-1|single|hybrid|3|A hybrid project involving many stakeholders is underway. As requested by the project management office (PMO), the project status…|
|`pmp-pr9-2`|export-batch-1|single|predictive|4|A new program manager checks the status of a project. The forecast shows the project is slightly behind schedule. However, in…|
|`pmp-pr9-3`|export-batch-1|single|predictive|3|An entrepreneur has initiated a software development project. The project charter has been approved, and a project manager has…|
|`pmp-pr9-4`|export-batch-1|single|predictive|3|During a steering committee meeting, a senior executive raised a concern that although the key cost parameters are reported to be…|
|`pmp-pr9-5`|export-batch-1|single|predictive|3|A project has a budgeted cost of $120,000. According to the schedule, 15% of the project should have been completed after one…|
|`pmp-pr9-6`|export-batch-1|single|predictive|3|A project advisory team is working remotely from the core project team. There is a constant flow of information between these two…|
|`pmp-pr9-7`|export-batch-1|single|agile|3|The leadership team of a company asked a project manager for an ad-hoc status update on the latest iteration of a project. The…|
|`pmp-pr9-201`|export-batch-2|single|agile|2|An agile team is doing an excellent job of focusing on high-priority items and completing them in less time than required.|
|`pmp-pr9-202`|export-batch-2|single|predictive|3|A project sponsor is increasingly anxious about the value returned from the investment made in a project and wants to ensure that…|
|`pmp-pr9-203`|export-batch-2|single|agile|2|While conducting a retrospective meeting, team members were surprised from the performance results of the team shared by the…|
|`pmp-pr9-204`|export-batch-2|single|agile|2|During an agile retrospective session, several team members mentioned that they were confused about the iteration's progress.|
|`pmp-pr9-205`|export-batch-2|single|agile|3|A company is developing a breakthrough product in a highly competitive and rapidly evolving market. The project manager…|
|`pmp-pr9-206`|export-batch-2|single|predictive|2|While preparing a project's third progress report, the project manager identifies that the project will experience delays due to…|
|`pmp-pr9-207`|export-batch-2|single|agile|2|A project manager is leading an agile project. The product owner complains that the project team is constantly completing fewer…|
|`pmp-pr9-208`|export-batch-2|single|agile|2|A project manager conducts a lessons learned meeting and project team members mention that they are not always aware of the…|
|`pmp-pr9-209`|export-batch-2|single|predictive|2|A project manager leads a project team on a project. This project's goal is to enhance a product's capability in the market.|
|`pmp-pr9-210`|export-batch-2|single|agile|3|A newly launched product prototype is receiving a number of customer complaints. To reduce negative feedback, the team comes up…|
|`pmp-pr9-211`|export-batch-2|multi|hybrid|3|The project teams are having difficulties coordinating their tasks due to several constraints and dependencies. The project…|
|`pmp-pr9-212`|export-batch-2|multi|predictive|2|In an ongoing project, performance is continuously evaluated to determine benefits. (Choose two)|
|`pmp-pr9-213`|export-batch-2|single|predictive|2|A project team has complete access to all project-related data, including visual data and information like the Gantt chart,…|
|`pmp-pr9-214`|export-batch-2|multi|agile|3|(Choose two)|
|`pmp-pr9-215`|export-batch-2|single|predictive|2|At the outset of a software development project, the project manager is tasked with devising a strategy for assessing the…|
|`pmp-pr9-216`|export-batch-2|single|predictive|2|During a complex software development project, a project manager notices significant variances between the planned and actual…|
|`pmp-pr9-217`|export-batch-2|single|predictive|3|A project manager is working on a large infrastructure project that is facing schedule delays. The project manager wants to use…|
|`pmp-pr9-218`|export-batch-2|single|predictive|2|A software development project is midway through its timeline, and the project manager notices that the actual progress is…|
|`pmp-pr9-219`|export-batch-2|single|predictive|2|In the midst of a major software development project, the project manager is preparing for an upcoming executive in-depth review.…|
|`pmp-pr9-220`|export-batch-2|single|predictive|2|During the final stages of a significant infrastructure project, it was discovered that comprehensive reports on critical…|
|`pmp-pr9-221`|export-batch-2|single|agile|2|You are working as a project manager for a medium-sized software project. The project sponsor believes that the team have not…|
|`pmp-pr9-222`|export-batch-2|single|hybrid|2|In a hybrid project environment, stakeholders are experiencing challenges in accessing the most recent version of the project…|
|`pmp-pr9-223`|export-batch-2|single|hybrid|3|As a new software development project kicks off, when is it most appropriate for the project manager to conduct the first project…|
|`pmp-pr9-224`|export-batch-2|single|agile|1|Following a directive to enhance operational efficiencies within a multinational corporation, the executive team has requested an…|
|`pmp-pr9-225`|export-batch-2|single|agile|2|You are working in an agile team where executives in the team you are delivering to are asking for a large display of reports to…|
|`pmp-pr9-226`|export-batch-2|single|agile|3|Multiple features have been released, but they are not having the effect the product owner believes they should. Your team are…|
|`pmp-pr9-227`|export-batch-2|single|agile|2|You are working on an agile project where the project customer wants a reliable way to measure the project performance, and you…|
|`pmp-pr9-228`|export-batch-2|single|agile|2|You are working on an agile project where the project sponsor and executive managers require lengthy reports about the project…|
|`pmp-pr9-229`|export-batch-2|single|hybrid|2|You are working on a project that has planned as much as they can up-front. You would like them to move to a more agile way of…|
|`pmp-pr9-230`|export-batch-2|single|agile|2|You are working as a product owner in an agile team, where the main measure of project success is the number of features…|
|`pmp-pr9-231`|export-batch-2|single|hybrid|2|You have been working on a project for the past eight months. Your project team have been approached by multiple stakeholders for…|
|`pmp-pr9-232`|export-batch-2|single|predictive|2|You are managing a large construction project. Due to some delays, the project schedule has been pushed back by one week. As a…|
|`pmp-pr9-233`|export-batch-2|single|agile|1|You have planned and begun the current sprint, where the project team have committed to delivering and releasing a particular…|
|`pmp-pr9-234`|export-batch-2|single|agile|2|You are a program manager on a large scale agile program, where each project team has its own scrum master. You need to know the…|
|`pmp-pr9-235`|export-batch-2|single|predictive|1|Your project has been executing for the past five months, and due to an organisational change a new project sponsor has come on…|
|`pmp-pr9-236`|export-batch-2|single|agile|1|You are managing a project as an Agile project manager and a business representative for the product you are delivering asks you…|
|`pmp-pr9-237`|export-batch-2|single|agile|2|You are the project manager on an Agile project that is half way through. The project sponsor is starting to raise concerns that…|
|`pmp-pr9-238`|export-batch-2|single|hybrid|2|You are working on a traditional waterfall project and the organisation wants to move to an Agile way of work. The functional…|
|`pmp-pr9-239`|export-batch-2|single|predictive|2|The project customer approaches you as they are worried that the project is not delivering fast enough. You review the project…|
|`pmp-pr9-240`|export-batch-2|single|agile|1|You are showing the project sponsor around the team area for your Agile project and see the burndown chart next to the team…|
|`pmp-pr9-241`|export-batch-2|single|agile|1|You are working through the feature backlog with the functional manager of the organisation, who is the product owner for your…|
|`pmp-pr9-242`|export-batch-2|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values. Identify…|
|`pmp-pr9-243`|export-batch-2|single|agile|1|You are working on a complex software project but see that the team uses a whiteboard, and writes tasks on cards or sticky notes.…|
|`pmp-pr9-244`|export-batch-2|single|predictive|2|You have just initiated a project, and the steering committee asks you to keep them updated on the main project activities. You…|
|`pmp-pr9-245`|export-batch-2|single|agile|2|You are working on a project where a competitor is also working on a similar feature, and being first to the market will have a…|
