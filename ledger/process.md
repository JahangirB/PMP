# Question ledger — Process

1182 questions across the 10 Process tasks.

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

### process-1 · Integrated planning — 127 items

*Size the work, choose a delivery approach, and hold one coherent plan together.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr1-001`|core|multi|hybrid|3|You are planning a programme that replaces a core banking ledger and, on top of it, builds a customer-facing mobile app. The…|
|`pmp-pr1-002`|core|single|predictive|3|A project will install identical point-of-sale hardware in 340 retail sites over eighteen months. The technical solution is…|
|`pmp-pr1-003`|core|single|hybrid|4|Your organisation's methodology requires eleven planning documents before execution starts. Your project is a six-week…|
|`pmp-pr1-004`|core|multi|predictive|3|Three weeks into planning a hospital systems replacement, your scope lead, schedule lead and procurement lead have each produced…|
|`pmp-pr1-005`|core|single|hybrid|4|You are planning a payments platform replacement. The core ledger must satisfy a regulator whose requirements are published,…|
|`pmp-pr1-006`|core|single|hybrid|5|You are planning a hospital records migration. The regulatory reporting rules are fixed and published, the migration sequence is…|
|`pmp-pr1-007`|core|pulldown|predictive|3|Prompts: Three workstream leads have each produce… / The approved plan is eight months old, f…|
|`pmp-pr1-015`|core|single|hybrid|4|You are integrating the plan for a city council programme with two workstreams. A contractor will replace 1,100 on-street parking…|
|`pmp-pr1-016`|core|multi|predictive|3|You are six weeks from the sponsor's baseline approval meeting for a £7.8 million ice rink at a council leisure centre, due to…|
|`pmp-pr1-010`|core|matching|agile|3|Prompts: A regulator has published a rule change… / A team is building a customer-facing fea… / A hardware product must be manufactured…|
|`pmp-pr1-011`|core|single|predictive|3|You are planning a five-year defence infrastructure programme. Years one and two are well understood; years three to five depend…|
|`pmp-pr1-012`|core|single|hybrid|3|Your infrastructure workstream must book a data centre migration window with an external provider six months ahead, and the…|
|`pmp-pr1-013`|core|single|predictive|3|You inherit the plan for a laboratory information system replacement. It is detailed, internally consistent and complete. Reading…|
|`pmp-pr1-014`|core|matching|hybrid|4|Prompts: The commercial lead's supplier, whose sc… / The regulator's liaison, on rules that m… / The operations director, on which capabi…|
|`pmp-case3-002`|case-03|single|hybrid|3|The vendor plans by milestones and the integration team plans sprint by sprint. Twice in the last month an interface needed for a…|
|`pmp-pr1-101`|process-1|single|predictive|1|You have completed the integrated project management plan for a £6.2 million dam spillway upgrade. The schedule, cost, resource…|
|`pmp-pr1-102`|process-1|single|agile|2|Your team is building a donations app for a children's charity. The next release depends on integrating with a payment provider…|
|`pmp-pr1-103`|process-1|single|agile|4|You are delivery lead for a contactless ticketing app for a regional bus operator. The operator's steering committee has asked…|
|`pmp-pr1-104`|process-1|single|agile|3|At a two-day joint planning event for an online grocer, three product teams have drafted their plans for the next ten weeks. The…|
|`pmp-pr1-105`|process-1|single|hybrid|3|Your hybrid programme is replacing a council's housing repairs system. Configuration is delivered in two-week sprints, and…|
|`pmp-pr1-106`|process-1|single|predictive|4|You are planning a signalling upgrade on a rail line. Your schedule depends on a 54-hour weekend track closure in March, the only…|
|`pmp-pr1-107`|process-1|single|predictive|5|You are preparing the cost estimate for a ferry pier replacement. The quantitative risk analysis gives a P50 of €24.1 million and…|
|`pmp-pr1-108`|process-1|single|hybrid|3|Your hybrid programme for an insurer combines a predictive migration of 1.3 million claims records with two agile teams building…|
|`pmp-pr1-109`|process-1|single|predictive|2|You are building the work breakdown structure for a new packaging line at a pharmaceutical plant. The commissioning lead has…|
|`pmp-pr1-110`|process-1|single|agile|4|You support four agile teams building a university's new student portal. Six months ago the head of IT began publishing each…|
|`pmp-pr1-111`|process-1|single|agile|3|Your team is building the ticketing site for a music festival. The release plan assumed ticket sales would open in mid-April. The…|
|`pmp-pr1-112`|process-1|single|predictive|2|You are integrating the plan for a rural road widening scheme. The planning consent, issued last month, includes a condition that…|
|`pmp-pr1-113`|process-1|multi|hybrid|3|You are building the integrated master schedule for a hybrid programme that fits out a new regional hospital. Construction and…|
|`pmp-pr1-114`|process-1|multi|predictive|3|You are about to baseline the plan for replacing the chillers that cool an operating-theatre block. The assumption log has forty…|
|`pmp-pr1-115`|process-1|multi|agile|4|You lead delivery for a team building a citizen-facing benefits application for a local authority. The release forecast counts a…|
|`pmp-pr1-116`|process-1|matching|predictive|2|Prompts: A sponsor needs a rough figure by Friday… / A highway authority must estimate resurf… / The work breakdown structure for an offi…|
|`pmp-pr1-117`|process-1|matching|agile|3|Prompts: The library network's director asks whic… / The marketing manager needs to know whic… / The team must decide which refined stori…|
|`pmp-pr1-118`|process-1|pulldown|hybrid|3|Prompts: The hardware workstream confirms its tes… / The software team's forecast shows a fea… / The software team finds that a required…|
|`pmp-pr1-119`|process-1|fillblank|predictive|3|You are assembling the budget for a visitor centre at a nature reserve. The work package estimates total 1,840,000. The risk…|
|`pmp-pr1-120`|process-1|hotspot|hybrid|3|The diagram shows five workstreams feeding an integration test that must start in week 15 of a hybrid programme replacing the…|
|`pmp-pr1-121`|process-1|single|predictive|1|You are managing the €3.4 million mid-life refit of a university's coastal research vessel. The sponsor approved the integrated…|
|`pmp-pr1-122`|process-1|single|agile|2|A housing association has formed a new five-person team to build a tenant repairs app. Before the first sprint, the head of…|
|`pmp-pr1-123`|process-1|single|predictive|3|You are planning a £3.8 million flood defence scheme for a riverside town. The sponsor wants the embankment finished before the…|
|`pmp-pr1-124`|process-1|single|hybrid|3|You are planning a hybrid programme for an airport's new baggage reconciliation system. Conveyor and scanner installation is…|
|`pmp-pr1-125`|process-1|single|agile|2|Three agile teams are building an online estate agency's new property listings platform. Each team has its own product owner and…|
|`pmp-pr1-126`|process-1|single|predictive|4|You are integrating estimates for a university laboratory fit-out. Comparing the six work-package estimates with records from…|
|`pmp-pr1-127`|process-1|single|hybrid|4|A parcel courier is replacing its depot handheld app. An agile team is refining the scanning screens sprint by sprint, while a…|
|`pmp-pr1-128`|process-1|single|hybrid|3|Your hybrid programme for a tractor manufacturer integrates new telemetry software into prototype vehicles at a test track. Track…|
|`pmp-pr1-129`|process-1|single|agile|4|Your team is building a member portal for a pension provider. For six sprints the release burn-up has shown the team completing…|
|`pmp-pr1-130`|process-1|single|hybrid|3|On a hybrid programme for smart electricity meters, the agile firmware team releases a build every two weeks, and a predictive…|
|`pmp-pr1-131`|process-1|single|predictive|5|You are about to baseline the plan for a new water treatment works. Commissioning can start only when four parallel workstreams…|
|`pmp-pr1-132`|process-1|single|predictive|4|You are planning a £4.6 million gallery redevelopment at a regional museum, funded by a heritage grant that releases at most £1.5…|
|`pmp-pr1-133`|process-1|multi|agile|2|A regional newspaper has funded a new team to build a subscription app, and the first sprint starts in a week. The…|
|`pmp-pr1-134`|process-1|multi|agile|3|A social care provider's product team has a flat backlog of 180 user stories for an app that schedules home-care visits. After…|
|`pmp-pr1-135`|process-1|multi|predictive|3|You are planning an insurer's move of 600 staff into a refurbished six-floor office. The draft schedule finishes three weeks…|
|`pmp-pr1-136`|process-1|matching|agile|3|Prompts: Online repeat prescriptions and video co… / With two people on leave, someone must d… / After the quarterly review, someone must…|
|`pmp-pr1-137`|process-1|matching|predictive|3|Prompts: The groundworks schedule is built on the… / The council may restrict working hours w… / The temporary access road flooded on Mon…|
|`pmp-pr1-138`|process-1|pulldown|hybrid|3|Prompts: Asked when the packing hall will be read… / Asked when retailers will be able to pla… / Asked what the programme as a whole will…|
|`pmp-pr1-139`|process-1|fillblank|agile|2|A language-learning app team has 260 points left in its release backlog. Over its last six two-week sprints, velocity ranged from…|
|`pmp-pr1-140`|process-1|hotspot|predictive|3|Your £2.1 million extension to a secondary school is scheduled to finish one week after the autumn term starts, and the governors…|
|`pmp-pr1-1`|pmp-lectures-1-11|single|predictive|3|An urban hotel design project has an international, multidisciplinary, and cross-cultural team. The project manager is in the…|
|`pmp-pr1-2`|pmp-lectures-1-11|single|hybrid|4|A project team is in the planning phase of a complex project. The project has an end-date constraint to launch a new product with…|
|`pmp-pr1-3`|pmp-lectures-1-11|multi|predictive|4|A project manager has just taken over an ongoing project from another project manager. During a review of the project management…|
|`pmp-pr1-4`|pmp-lectures-1-11|single|hybrid|3|During the design phase, a project manager realizes that the project will benefit from using adaptive tools. The effectiveness of…|
|`pmp-pr1-201`|pmp-mocks-1-10|single|agile|2|In an innovative project, the sponsor wants to deliver a creative and large data processing platform to better serve the…|
|`pmp-pr1-202`|pmp-mocks-1-10|single|predictive|3|A project manager is managing several interconnected projects. Some of these projects rely on specific deliverables from other…|
|`pmp-pr1-203`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to a long-term project with a duration of three years.|
|`pmp-pr1-204`|pmp-mocks-1-10|single|hybrid|3|A company in a heavily regulated industry is planning to release a new product not previously available in the market. The CEO…|
|`pmp-pr1-205`|pmp-mocks-1-10|single|hybrid|3|A project manager is managing a project that has a component using agile. The project sponsor asks the project manager to add a…|
|`pmp-pr1-206`|pmp-mocks-1-10|single|hybrid|3|The local government regulator has issued a notification to all companies regarding a new project aimed at facilitating…|
|`pmp-pr1-207`|pmp-mocks-1-10|single|hybrid|4|A project is developing a product where the time to market is critical. During the execution phase, the client submits a change…|
|`pmp-pr1-208`|pmp-mocks-1-10|single|predictive|3|A project schedule requires that two teams work simultaneously on separate parts of a complex project. The two parts of the…|
|`pmp-pr1-209`|pmp-mocks-1-10|single|predictive|2|A project manager is currently overseeing a project from its inception and is aware that there are several assumptions and…|
|`pmp-pr1-210`|pmp-mocks-1-10|single|hybrid|3|The project manager's last IT transformation project was canceled due to a misalignment of a business process with the accounting…|
|`pmp-pr1-211`|pmp-mocks-1-10|single|hybrid|2|A team is working on a complex and fast-changing new product development project. After a recent process evaluation, the team…|
|`pmp-pr1-212`|pmp-mocks-1-10|single|hybrid|3|During a team meeting, team members asked the project manager to advise them on project dependencies.|
|`pmp-pr1-213`|pmp-mocks-1-10|single|hybrid|3|A company is using a predictive approach for the development of a particular component as defined in the scope management plan.…|
|`pmp-pr1-214`|pmp-mocks-1-10|single|predictive|1|After preparing the project management plan, a project manager schedules a meeting with key stakeholders to finalize the planning…|
|`pmp-pr1-215`|pmp-mocks-1-10|single|predictive|3|A complex project, involving multiple stakeholders with diverse priorities, requires long and challenging negotiations to…|
|`pmp-pr1-216`|pmp-mocks-1-10|single|predictive|3|A project manager planning a strategic predictive project has suddenly fallen ill. The project baseline must be submitted to the…|
|`pmp-pr1-217`|pmp-mocks-1-10|single|predictive|2|An organization is under a business transformation. The project manager has been assigned to a project related to one of the new…|
|`pmp-pr1-218`|pmp-mocks-1-10|single|agile|2|A project manager recently joined a new organization. The project sponsor is a subject matter expert and wants to manage scope by…|
|`pmp-pr1-219`|pmp-mocks-1-10|single|agile|1|A project manager is managing an agile project. A new team member, who has never worked in an agile team, joins the project. The…|
|`pmp-pr1-220`|pmp-mocks-1-10|single|hybrid|3|A project management office (PMO) normally runs projects using the waterfall approach. A project currently in development is…|
|`pmp-pr1-221`|pmp-mocks-1-10|single|hybrid|2|A project team is brainstorming the best project management methodology to recommend to senior management for adoption. The…|
|`pmp-pr1-222`|pmp-mocks-1-10|single|agile|1|A project team has adopted a new project management methodology which entails holding regular review and retrospective meetings…|
|`pmp-pr1-223`|pmp-mocks-1-10|single|hybrid|3|The project manager's company won a digital transformation project request for proposal (RFP) with clear scope, time and cost. As…|
|`pmp-pr1-224`|pmp-mocks-1-10|single|hybrid|3|A project manager is planning a worldwide product launch. Manufacturing the product is subject to rigid time constraints related…|
|`pmp-pr1-225`|pmp-mocks-1-10|single|hybrid|2|A company that is heavily focused on delivering projects using predictive approaches on-boards a new project manager who uses…|
|`pmp-pr1-226`|pmp-mocks-1-10|single|agile|1|A project manager is managing a project where requirements are not fully known and a high level of uncertainty exists.|
|`pmp-pr1-227`|pmp-mocks-1-10|single|hybrid|3|Project team A is launching a complex program in collaboration with project teams B, C, and D.|
|`pmp-pr1-228`|pmp-mocks-1-10|single|hybrid|3|A project manager is beginning a new financial software project in a very competitive environment. The regulations for compliance…|
|`pmp-pr1-229`|pmp-mocks-1-10|single|hybrid|2|A product development project has run into design issues. As a result, the team has decided to change the approach of the product…|
|`pmp-pr1-230`|pmp-mocks-1-10|single|hybrid|2|Embarking on an enterprise-wide software upgrade project, distinctively different from the organization's usual IT initiatives,…|
|`pmp-pr1-231`|pmp-mocks-1-10|single|hybrid|2|As a project unfolds for a global health initiative, the project manager identifies that the current online collaboration tools…|
|`pmp-pr1-232`|pmp-mocks-1-10|single|hybrid|2|In a software development project, the project manager notices that the various project teams have differing priorities and…|
|`pmp-pr1-233`|pmp-mocks-1-10|single|predictive|2|Just assigned to lead a new technology integration project, the project manager learns that no foundational project documentation…|
|`pmp-pr1-234`|pmp-mocks-1-10|single|hybrid|2|You are managing a project where the majority of the product has been planned up-front, but an external system is needed from a…|
|`pmp-pr1-235`|pmp-mocks-1-10|single|agile|2|You are working on a project where the requirements are clear but the solution ideas are uncertain. The product owner and the…|
|`pmp-pr1-236`|pmp-mocks-1-10|single|agile|2|You are working on a new product for your organisation where the requirements are complex and are likely to change a few times as…|
|`pmp-pr1-237`|pmp-mocks-1-10|single|predictive|2|A new project has been kicked off in your organisation due to a recent government inquiry into your industry, which found…|
|`pmp-pr1-238`|pmp-mocks-1-10|single|agile|2|Your organisation is moving to an Agile way of work. Your project team are strongly opposed to using Agile as they have been…|
|`pmp-pr1-239`|pmp-mocks-1-10|single|hybrid|2|Your team have not worked on projects before, and don't know about waterfall, agile, or any of the different models. You project…|
|`pmp-pr1-240`|pmp-mocks-1-10|single|predictive|2|You a project manager in an organisation with a directive Project Management Office, and are currently in between projects. The…|
|`pmp-pr1-241`|pmp-mocks-1-10|single|hybrid|2|Your Project Management Office has no prescribed way of work, so are working with a vendor who supplies an eXtreme Programming…|
|`pmp-pr1-242`|pmp-mocks-1-10|single|hybrid|2|Your Project Management Office is in the middle of a company-wide "ways of working" transformation. They have taken a popular…|
|`pmp-pr1-243`|pmp-mocks-1-10|single|hybrid|2|You are a manager of a PMO in a large organisation, which has recently undertaken a company- wide way of working transformation.…|
|`pmp-pr1-244`|pmp-mocks-1-10|single|hybrid|2|You have been planning a project for the last two months, gathering requirements and scope and assigning tasks to a schedule. As…|
|`pmp-pr1-245`|pmp-mocks-1-10|single|agile|1|You have been asked to make a process improvement in the accounting area of your organisation. You have been assigned a small…|
|`pmp-pr1-246`|pmp-mocks-1-10|single|agile|2|You have been asked to initiate a project to deliver a new system. The requirements are not clear, and it will take some work to…|
|`pmp-pr1-247`|pmp-mocks-1-10|single|predictive|1|You have been planning a project to deliver a process change for the past four weeks. The requirements and scope have been signed…|
|`pmp-pr1-248`|pmp-mocks-1-10|single|agile|1|You are working on a project as an Agile Project Manager. The executive manager who will own the product you are delivering would…|
|`pmp-pr1-249`|pmp-mocks-1-10|single|agile|1|You are working on a project and the team have recently moved to an Agile way of work. One of the team members is confused about…|
|`pmp-pr1-250`|pmp-mocks-1-10|single|hybrid|2|You have been assigned to take over a project in your organisation that has gone off track, with scope creep causing the schedule…|
|`pmp-pr1-251`|pmp-mocks-1-10|single|predictive|2|You have joined a car manufacturing company as a new project manager. Senior Management have asked you to take the necessary…|
|`pmp-pr1-252`|pmp-mocks-1-10|single|hybrid|2|You are working on a new project where the design and project work, including all the scope and cost have been planned up front.…|
|`pmp-pr1-253`|pmp-mocks-1-10|single|predictive|2|You have just created your project management plan and are reviewing it with your stakeholders. It includes the subsidiary plans…|
|`pmp-pr1-254`|pmp-mocks-1-10|single|agile|1|In 2001, a group of individuals, representing the most widely used lightweight software development methodologies, agreed on a…|
|`pmp-pr1-255`|pmp-mocks-1-10|single|hybrid|1|You are working with your project team in a way that ensures you Analyse the requirements, then Design, Build and Test for those…|
|`pmp-pr1-256`|pmp-mocks-1-10|single|predictive|1|You are working on a project with clear procedures based on past projects. The product is clear and the risks are fairly well…|
|`pmp-pr1-257`|pmp-mocks-1-10|single|agile|2|You are an Agile project manager leading a project team who is used to a Waterfall way of work. The project sponsor wants to plan…|
|`pmp-pr1-258`|pmp-mocks-1-10|single|hybrid|1|You have begun a new project in a new organisation, and the project team mentions you are working in an iterative project…|
|`pmp-pr1-259`|pmp-mocks-1-10|single|agile|1|In 2001, a group of individuals, representing the most widely used lightweight software development methodologies, agreed on a…|
|`pmp-pr1-260`|pmp-mocks-1-10|single|agile|1|You are working on a project where the organisation wants to move towards an Agile way of work.|
|`pmp-pr1-261`|pmp-mocks-1-10|single|predictive|1|You have come on board a new project team and have been advised that the project works with a predictive style project lifecycle.|
|`pmp-pr1-262`|pmp-mocks-1-10|single|hybrid|2|You are in a new project team and working through which project lifecycle or way of work to use in order to deliver the products…|
|`pmp-pr1-263`|pmp-mocks-1-10|single|hybrid|2|You are in the forming stages of a new project team, and deciding on the way of work to use when operating as a team.|
|`pmp-pr1-264`|pmp-mocks-1-10|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values.|
|`pmp-pr1-265`|pmp-mocks-1-10|single|agile|1|You are working with a team that is new to Agile and are using Scrum to manage the project. The business representative is…|
|`pmp-pr1-266`|pmp-mocks-1-10|single|agile|1|There are multiple project life-cycles you can choose from, depending on the type of project, environment, deliverable and…|
|`pmp-pr1-267`|pmp-mocks-1-10|single|predictive|2|You are in the planning phase of your project, having finished the various project plans required to begin. Your Project Sponsor…|
|`pmp-pr1-268`|pmp-mocks-1-10|single|agile|1|Your organization has recently completed a new system for the sales department. New features still need to be added as the…|

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

### process-3 · Value delivery — 149 items

*Prioritise by value and keep checking the work is still worth doing.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr3-001`|core|fillblank|agile|3|A Kanban team currently has 24 work items in progress and finishes an average of 6 items per week. Work down the average cycle…|
|`pmp-pr3-002`|core|single|agile|3|Your product backlog has 240 items. The product owner prioritises by asking each stakeholder to rank their own requests, then…|
|`pmp-pr3-003`|core|single|agile|3|Your backlog is ordered by a scoring model the product owner built eighteen months ago. Two features near the top serve a…|
|`pmp-pr3-004`|core|single|hybrid|3|Your programme was funded to save two million a year by automating claims handling. Nine months in, a competitor's acquisition…|
|`pmp-pr3-005`|core|fillblank|agile|3|Your team can start only one of two features next. Finance estimates that delaying the express checkout feature costs the…|
|`pmp-pr3-006`|core|single|agile|3|Your team could release a working version of the booking feature now, covering the two journeys that account for most bookings,…|
|`pmp-pr3-007`|core|fillblank|predictive|3|A warehouse automation project will cost £480,000 to deliver and is forecast to reduce operating costs by £160,000 in each full…|
|`pmp-pr3-008`|core|single|agile|3|Usage data six months after launch shows that four of the eleven features your team built are used by fewer than two per cent of…|
|`pmp-pr3-014`|core|multi|hybrid|4|A national museum's hybrid programme is refurbishing six galleries to a fixed plan while an agile team builds the visitor app.…|
|`pmp-pr3-010`|core|fillblank|predictive|3|Two proposals compete for the same funding. Yours requires an investment of £1,800,000 and is forecast to return net benefits of…|
|`pmp-pr3-011`|core|multi|agile|3|Your team completes and demonstrates working increments every two weeks and has done so for eight months. Nothing reaches…|
|`pmp-pr3-012`|core|single|hybrid|3|Your product owner prioritises the backlog strictly by customer value and has deferred a mandatory accessibility conformance…|
|`pmp-pr3-013`|core|fillblank|agile|3|Your product owner has five remaining backlog items and has estimated the annual benefit of each: £540,000, £360,000, £180,000,…|
|`pmp-pr3-061`|core|graphic|agile|3|Your team's average lead time has doubled over the past month. The board shows the items in each column against its…|
|`pmp-case2-005`|case-02|multi|agile|4|The old permit database contract ends in five months and cannot be extended. Malik asks which of the remaining items should come…|
|`pmp-case5-010`|case-05|single|agile|4|The agile team's next monthly release could include either the shared call-routing features that let either centre answer any…|
|`pmp-pr3-101`|process-3|single|predictive|1|Your project is rolling out electronic prescribing across a hospital trust's four sites and closes next month. The business case…|
|`pmp-pr3-102`|process-3|single|agile|3|You lead an agile team building a scheduling app for home-care agencies. The sales director has asked the product owner to move…|
|`pmp-pr3-103`|process-3|single|predictive|3|Your project replaces a water company's billing platform, with go-live in three weeks. One benefit in the business case is a 25…|
|`pmp-pr3-104`|process-3|single|agile|3|Your team at a retail bank completes features every two weeks, but the bank's release board approves production releases once a…|
|`pmp-pr3-105`|process-3|multi|predictive|3|A third-party logistics firm goes live with a new warehouse management system in six weeks. The forecast benefit is an 18 per…|
|`pmp-pr3-106`|process-3|single|hybrid|4|In a hybrid programme for a hotel group, check-in kiosks are being installed in 40 hotels to a fixed plan, and an agile team…|
|`pmp-pr3-107`|process-3|matching|agile|3|Prompts: Answers from a rider survey need sorting… / Six councils that fund the service each… / A release tied to a fixed timetable-chan…|
|`pmp-pr3-108`|process-3|single|predictive|3|Your project is implementing a repairs-scheduling system for a city council's housing department. The benefit owner named in the…|
|`pmp-pr3-109`|process-3|single|predictive|5|Your project is moving a university's written examinations onto a digital platform. The business case counts two benefits:…|
|`pmp-pr3-110`|process-3|fillblank|predictive|3|A lift-maintenance company rolled out a mobile job app to its field engineers three months ago. The business case assumed 600…|
|`pmp-pr3-111`|process-3|single|agile|1|Your team is building a donation app for a national charity. At the sprint review, pilot donors say the monthly-giving screen…|
|`pmp-pr3-112`|process-3|single|predictive|2|You are installing solar panels across 30 schools in a multi-academy trust, planned in five waves of six schools in alphabetical…|
|`pmp-pr3-113`|process-3|multi|agile|3|An agile team at a motor insurer has been building a self-service claims portal for nine months. The sponsor asks for a dashboard…|
|`pmp-pr3-114`|process-3|single|hybrid|5|A parcel courier's programme is building a bespoke route-planning system in sprints, with depot rollouts scheduled in fixed…|
|`pmp-pr3-115`|process-3|pulldown|hybrid|2|Prompts: The gate installation at the two largest… / A rival operator's free rebooking offer…|
|`pmp-pr3-116`|process-3|hotspot|agile|2|An agile team at an online pharmacy has mapped how a change travels from an agreed idea to customers. Average lead time is 41…|
|`pmp-pr3-117`|process-3|matching|hybrid|3|Prompts: The tenant portal went live in all fourt… / Sixty-two per cent of repair requests ar… / Contact-centre running costs have fallen…|
|`pmp-pr3-118`|process-3|single|predictive|4|Your project is delivering a case-management system for a county council's children's services. The business case rests on each…|
|`pmp-pr3-119`|process-3|single|agile|4|You are the agile lead for a fintech team in the third day of a two-week sprint. The sprint goal is to complete an open-banking…|
|`pmp-pr3-120`|process-3|multi|hybrid|3|A hospital is building a new outpatient wing to a fixed design through a contractor, while an internal agile team builds the…|
|`pmp-pr3-121`|process-3|single|predictive|1|You are managing delivery of a berth-booking system for a port authority against an approved scope baseline. At a progress check…|
|`pmp-pr3-122`|process-3|single|agile|3|A gym chain's agile team plans to build a class-recommendation engine that suggests sessions to members, on the hypothesis that…|
|`pmp-pr3-123`|process-3|single|predictive|3|Your project is building a six-gate extension to a regional airport terminal, due for completion in October. The first three…|
|`pmp-pr3-124`|process-3|single|hybrid|4|A pensions administrator's hybrid programme is moving members from a legacy platform to a new one. The business case rests on…|
|`pmp-pr3-125`|process-3|single|agile|3|An online grocer's team spent three sprints redesigning checkout. The new design was released behind a feature toggle to half of…|
|`pmp-pr3-126`|process-3|single|agile|3|A physiotherapy clinic chain's agile team is building a booking app whose business case depends on submitting claims directly to…|
|`pmp-pr3-127`|process-3|single|predictive|4|You manage a predictive project replacing a rail operator's procurement system. Its business case claims £900,000 a year from…|
|`pmp-pr3-128`|process-3|single|predictive|2|A fire and rescue service's project is replacing paper rostering with a self-service system. Two benefits are listed: lower…|
|`pmp-pr3-129`|process-3|single|predictive|3|A mining company has contracted a supplier to replace its maintenance management system. The approved requirement, set by the…|
|`pmp-pr3-130`|process-3|single|agile|3|A veterinary practice group wants to cut missed appointments, which cost it about £18,000 a month. The product owner has asked…|
|`pmp-pr3-131`|process-3|single|hybrid|5|An insurer's hybrid programme is replacing its claims platform. The agile team's next increment auto-approves low-value home…|
|`pmp-pr3-132`|process-3|single|predictive|4|Your project has piloted a new appointment-booking process at one of a dental group's 28 practices for twelve weeks. Missed…|
|`pmp-pr3-133`|process-3|multi|agile|2|An agile team at a pharmacy chain has spent five months building a stock-ordering tool for branch pharmacists. Every sprint…|
|`pmp-pr3-134`|process-3|multi|hybrid|3|A building society's hybrid programme is replacing its branch teller system. The agile team releases new teller features to all…|
|`pmp-pr3-135`|process-3|multi|predictive|4|Your project retrofitted insulation and heat pumps in 120 council-owned buildings, with a business case promising a 25 per cent…|
|`pmp-pr3-136`|process-3|matching|predictive|2|Prompts: Keeping the benefits register current du… / Changing depot working practices so that… / Deciding at a stage gate whether the for…|
|`pmp-pr3-137`|process-3|matching|agile|3|Prompts: A new hotel-ranking algorithm could lowe… / A rebuilt search service must cope with… / Two wordings for the booking button are…|
|`pmp-pr3-138`|process-3|pulldown|hybrid|2|Prompts: Route-planning software lets the existin… / An online repairs service lets the counc… / Finance officers each save four hours a…|
|`pmp-pr3-139`|process-3|fillblank|agile|3|A meal-kit company's product owner is weighing a family-plan feature that costs £120,000 to build. If families take it up, it…|
|`pmp-pr3-140`|process-3|hotspot|hybrid|3|A hybrid programme is building a new central library to a fixed opening date. The agile team building its online room-booking…|
|`pmp-pr3-1`|pmp-lectures-1-11|single|predictive|2|A project is nearing completion and a stakeholder wants to know how the project outcomes will impact the organization.|
|`pmp-pr3-2`|pmp-lectures-1-11|single|predictive|3|At the beginning of a project, a stakeholder is concerned that the initiative may not deliver value to the organization.|
|`pmp-pr3-3`|pmp-lectures-1-11|single|predictive|2|A lengthy upgrade project is being delivered in a series of phased outputs. The end users will face some level of disruption…|
|`pmp-pr3-4`|pmp-lectures-1-11|single|predictive|3|During the final stage of a project, one of the key stakeholders asks the project manager to brief the executive board about the…|
|`pmp-pr3-5`|pmp-lectures-1-11|single|predictive|2|A project manager has completed the project deliverable and has passed the acceptance criteria.|
|`pmp-pr3-6`|pmp-lectures-1-11|single|hybrid|4|As a project is nearing completion, a struggle between the product owner and the project sponsor regarding who owns the benefits…|
|`pmp-pr3-7`|pmp-lectures-1-11|single|hybrid|3|A company is transitioning from using a predictive approach to a hybrid predictive-agile project implementation. The leadership…|
|`pmp-pr3-8`|pmp-lectures-1-11|single|predictive|3|A newly hired project manager is assigned to a school construction project. The school serves students from surrounding…|
|`pmp-pr3-9`|pmp-lectures-1-11|single|agile|3|An agile project is in its first of ten iterations. After the iteration review, the team receives stakeholder feedback that the…|
|`pmp-pr3-10`|pmp-lectures-1-11|single|hybrid|3|During the final sprint review, the product owner stated that the product does not deliver any value. The project manager…|
|`pmp-pr3-11`|pmp-lectures-1-11|single|agile|4|During sprint planning, an agile team and product owner identified that the team should be able to finish five user stories.…|
|`pmp-pr3-12`|pmp-lectures-1-11|single|agile|3|An organization makes an acquisition of a similar product halfway through the development cycle of a potential product. This…|
|`pmp-pr3-201`|pmp-mocks-1-10|single|hybrid|3|A scrum team is using a hybrid approach with 2-week sprints. The minimum viable product (MVP) release is at the end of the…|
|`pmp-pr3-202`|pmp-mocks-1-10|single|agile|3|A business manager and product owner are requesting a more flexible way to incorporate changes into the product backlog. They…|
|`pmp-pr3-203`|pmp-mocks-1-10|single|agile|2|An agile team is receiving multiple unplanned requests for product enhancements in an iteration. This impediment is causing the…|
|`pmp-pr3-204`|pmp-mocks-1-10|single|predictive|3|At the end of a large project, the stakeholders realize that the achieved benefits are not the same as the expected benefits.|
|`pmp-pr3-205`|pmp-mocks-1-10|single|agile|3|An agile project team has been successfully releasing updated deliverables each sprint that consistently provide an increased…|
|`pmp-pr3-206`|pmp-mocks-1-10|single|agile|2|During a project planning session, the project manager is trying to define a release plan to achieve consistent value delivery…|
|`pmp-pr3-207`|pmp-mocks-1-10|single|agile|3|A project manager is working on launching a minimum viable product (MVP) for a new-to-market product. The team has successfully…|
|`pmp-pr3-208`|pmp-mocks-1-10|single|hybrid|3|During product development, two functional departments indicate that they are short on resources and cannot build and deliver all…|
|`pmp-pr3-209`|pmp-mocks-1-10|single|agile|3|A project manager has developed a project management plan based on agile methodologies in an effort to deliver the product…|
|`pmp-pr3-210`|pmp-mocks-1-10|multi|predictive|4|A project manager is leading a project to implement a process improvement software system that was developed in-house. However,…|
|`pmp-pr3-211`|pmp-mocks-1-10|single|agile|3|In an agile project, the project status indicates that the project is 95% completed. During an iteration review, the stakeholders…|
|`pmp-pr3-212`|pmp-mocks-1-10|single|predictive|3|A project is close to completion, and the customer wants to celebrate the success by sharing a business case about the benefits…|
|`pmp-pr3-213`|pmp-mocks-1-10|single|agile|3|A project manager is overseeing a project aimed at providing customers with real-time shipment tracking capabilities. The project…|
|`pmp-pr3-214`|pmp-mocks-1-10|single|predictive|3|During sign-off for a strategic project that resulted in additional costs, a project manager was unable to justify the additional…|
|`pmp-pr3-215`|pmp-mocks-1-10|single|hybrid|2|A marketing team is developing ideas in order to revamp an existing product. The marketing executive reaches out to the project…|
|`pmp-pr3-216`|pmp-mocks-1-10|single|agile|2|An agile project is in its last iteration. During the daily standup, the team realizes that they will not be able to complete all…|
|`pmp-pr3-217`|pmp-mocks-1-10|single|predictive|3|A project manager has been assigned to a project to implement an asset health management system. The executive business sponsor…|
|`pmp-pr3-218`|pmp-mocks-1-10|single|predictive|3|During a new technology project, confusion arises due to the lack of a clearly defined and communicated future state of the…|
|`pmp-pr3-219`|pmp-mocks-1-10|single|agile|2|A team member suggests using a new product to speed up feature development, but this will increase the project budget. The…|
|`pmp-pr3-220`|pmp-mocks-1-10|single|agile|3|A new system is being developed to replace an existing manual process. During a sprint demo, business stakeholders expressed the…|
|`pmp-pr3-221`|pmp-mocks-1-10|single|agile|3|Due to a downturn in the business environment, a customer is only able to make a project's payment in part, rather than the fully…|
|`pmp-pr3-222`|pmp-mocks-1-10|single|agile|3|A startup in its early stages has been acquired by a major company. In light of this new scenario, the project manager has been…|
|`pmp-pr3-223`|pmp-mocks-1-10|single|predictive|3|A project manager works in a matrix organization and manages an internal software development project to improve the company's…|
|`pmp-pr3-224`|pmp-mocks-1-10|single|predictive|3|A large-scale, multiyear transformation project was completed as per the defined requirements and within the approved budget and…|
|`pmp-pr3-225`|pmp-mocks-1-10|single|agile|2|A project manager is working on a software development project. Several team members have become overly focused on delivering…|
|`pmp-pr3-226`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a business transformation project at a company facing significant financial challenges due to a…|
|`pmp-pr3-227`|pmp-mocks-1-10|single|predictive|3|A project manager is assigned to a complex IT project that is expected to deliver significant benefits to the organization. The…|
|`pmp-pr3-228`|pmp-mocks-1-10|single|agile|3|A product owner has been attending daily standups but seems focused only on the budget and costs of each iteration. When…|
|`pmp-pr3-229`|pmp-mocks-1-10|single|hybrid|3|In the middle of a three-year-long project, the company committee overseeing the project becomes uncertain about whether the…|
|`pmp-pr3-230`|pmp-mocks-1-10|single|predictive|2|A project manager is overseeing the development and launch of a new product. In order to comply with the Project Management…|
|`pmp-pr3-231`|pmp-mocks-1-10|single|agile|3|A project manager has started a new project with a 24 month timeline. Soon after the kickoff, the sponsor informs the project…|
|`pmp-pr3-232`|pmp-mocks-1-10|single|agile|2|During a complex project, a stakeholder informs the project manager about a requirement and proposes to call a meeting to inform…|
|`pmp-pr3-233`|pmp-mocks-1-10|single|hybrid|3|A project manager is overseeing a project with an extremely tight schedule. During a meeting, the project stakeholders express…|
|`pmp-pr3-234`|pmp-mocks-1-10|single|predictive|2|An organization is redesigning and relaunching a key product. The stakeholders are all company executives and want to ensure that…|
|`pmp-pr3-235`|pmp-mocks-1-10|single|predictive|1|A project manager is part of a project management strategy focus group that is brainstorming the ways projects will be used to…|
|`pmp-pr3-236`|pmp-mocks-1-10|single|agile|1|An agile team is determining which activities to complete first during an upcoming project iteration for a new product…|
|`pmp-pr3-237`|pmp-mocks-1-10|single|predictive|2|A project manager is midway through project delivery. The project manager decides to hold a stakeholder engagement meeting with…|
|`pmp-pr3-238`|pmp-mocks-1-10|single|predictive|3|Due to regulatory changes, a government agency is now a key stakeholder in a complex project. An agency representative has asked…|
|`pmp-pr3-239`|pmp-mocks-1-10|single|agile|2|During a standup, a project team member suggests modifying some features to benefit from a new opportunity. Another team member…|
|`pmp-pr3-240`|pmp-mocks-1-10|single|agile|3|A project manager is leading a development project for a high-profile client with strict deadlines. During a sprint, the project…|
|`pmp-pr3-241`|pmp-mocks-1-10|single|agile|3|A product company is transforming the way it develops and releases products in the market. Executives believe that this is a…|
|`pmp-pr3-242`|pmp-mocks-1-10|single|hybrid|3|A customer indicates that they would like to add functionality. Although this request is made during the execution phase of the…|
|`pmp-pr3-243`|pmp-mocks-1-10|single|predictive|2|Before a project evaluation meeting, a key stakeholder complains that the project does not deliver the expected business…|
|`pmp-pr3-244`|pmp-mocks-1-10|single|agile|3|During an agile iteration, Task 1 cannot be completed on time due to unexpected challenges. Another team within the project is…|
|`pmp-pr3-245`|pmp-mocks-1-10|single|agile|2|During a daily standup, some team members realized they were not all prioritizing the requirements in the same way. This could…|
|`pmp-pr3-246`|pmp-mocks-1-10|single|agile|2|In the most recent sprint review meeting, the client announced that they need to implement two of the five modules committed for…|
|`pmp-pr3-247`|pmp-mocks-1-10|single|agile|2|A project manager is assigned to a new project. The requirements are still evolving, but there is a need to deliver business…|
|`pmp-pr3-248`|pmp-mocks-1-10|single|agile|2|An agile development team changes a design element in a project. Testing results improve dramatically over the next two…|
|`pmp-pr3-249`|pmp-mocks-1-10|single|agile|2|During a critical phase of a software development project, a key developer notifies the project manager that they will need to…|
|`pmp-pr3-250`|pmp-mocks-1-10|single|hybrid|2|A project manager is overseeing the development of an enterprise resource planning (ERP) system and has decided that a…|
|`pmp-pr3-251`|pmp-mocks-1-10|single|predictive|2|Your project is nearing completion. You have monitored and controlled quality through testing, and risks with mitigations…|
|`pmp-pr3-252`|pmp-mocks-1-10|single|agile|2|In the development stage of an innovative mobile application, the agile project team discovers the need for an advanced software…|
|`pmp-pr3-253`|pmp-mocks-1-10|single|agile|2|During a mobile application development project, the project manager faces significant uncertainty regarding user preferences and…|
|`pmp-pr3-254`|pmp-mocks-1-10|single|hybrid|2|Amid the rollout of a new software platform designed to enhance customer engagement for a retail chain, the lead role transitions…|
|`pmp-pr3-255`|pmp-mocks-1-10|single|agile|2|A technology firm is transitioning its software development practices to agile methodologies to improve its product development…|
|`pmp-pr3-256`|pmp-mocks-1-10|single|agile|2|As a project manager overseeing a pivotal update to a client's software system, you've noticed a significant lack of engagement…|
|`pmp-pr3-257`|pmp-mocks-1-10|single|hybrid|3|In a project designed to deploy new technology across several departments, a project manager notices a shift in stakeholder…|
|`pmp-pr3-258`|pmp-mocks-1-10|single|agile|2|Facing the challenge of rolling out a critical disaster response application, a project manager leads a multidisciplinary team…|
|`pmp-pr3-259`|pmp-mocks-1-10|single|agile|2|A project manager is overseeing a project designed to deliver iterative enhancements to an existing service and has an…|
|`pmp-pr3-260`|pmp-mocks-1-10|single|predictive|2|As a project manager overseeing the rollout of a new online banking platform, ensuring that customers can seamlessly transition…|
|`pmp-pr3-261`|pmp-mocks-1-10|single|agile|3|You have recently completed a project for a new product and have been asked to manage it after its release to keep it relevant in…|
|`pmp-pr3-262`|pmp-mocks-1-10|single|predictive|3|You are initiating a project to a sales team where the process will change significantly, even though the outcome of a completed…|
|`pmp-pr3-263`|pmp-mocks-1-10|single|agile|2|The product owner has worked with a team of senior users to gather feature ideas. They now have a list of more than fifty ideas…|
|`pmp-pr3-264`|pmp-mocks-1-10|single|predictive|2|You have been given authority to manage the upcoming features your team will release. You do not have a high enough level of…|
|`pmp-pr3-265`|pmp-mocks-1-10|single|agile|1|Your agile project has recently gained some new stakeholders after a department restructure. They complain to you that they are…|
|`pmp-pr3-266`|pmp-mocks-1-10|single|agile|1|Your agile project has delivered three features so far and is halfway through delivering the latest improvement. During a…|
|`pmp-pr3-267`|pmp-mocks-1-10|single|agile|1|You are a project manager working on an Agile project, and want your team to maximise the value they bring to the customer.|
|`pmp-pr3-268`|pmp-mocks-1-10|single|agile|1|You are working on an Agile project and the project team have just finished an iteration. All tasks for the iteration have been…|
|`pmp-pr3-269`|pmp-mocks-1-10|single|agile|1|Feedback in many different forms is one of the most important parts of a successful Agile product delivery.|
|`pmp-pr3-270`|pmp-mocks-1-10|single|predictive|2|You are in the process of creating a project charter for your part of the organisation, to try and gain resources for an…|
|`pmp-pr3-271`|pmp-mocks-1-10|single|predictive|3|You have been planning a project with the manager of a functional area who needs to increase their profit by 57% in the next…|
|`pmp-pr3-272`|pmp-mocks-1-10|single|agile|2|You are the Scrum Master and about to begin a new iteration in your Agile project. New features have been added to the backlog,…|
|`pmp-pr3-273`|pmp-mocks-1-10|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values.|
|`pmp-pr3-274`|pmp-mocks-1-10|single|agile|2|You are the Agile lead or Scrum Master for your Agile team and planning for the next iteration is about to begin.|
|`pmp-pr3-275`|pmp-mocks-1-10|single|agile|1|The Agile team for which you are the project manager has determined several key features to deliver to the customer.|
|`pmp-pr3-276`|pmp-mocks-1-10|single|agile|1|You are working on a project as an Agile project manager. You look at the product backlog and ensure several features, or minimum…|
|`pmp-pr3-277`|pmp-mocks-1-10|single|agile|2|A team new to Agile has worked closely with the product owner and has identified the highest value features of the project.|
|`pmp-pr3-278`|pmp-mocks-1-10|single|agile|2|You have begun planning a large project with the input from your Project Sponsor and the help of your project team. The project…|
|`pmp-pr3-279`|pmp-mocks-1-10|single|predictive|2|Your project is around half-way complete and has had a great deal of issues around resources and funding. Other executives from…|
|`pmp-pr3-280`|pmp-mocks-1-10|single|agile|2|You are an agile project manager and your team have delivered three features to improve a customer app over the last three…|
|`pmp-pr3-281`|pmp-mocks-1-10|single|agile|3|You are working on an agile project and are planning your product backlog for the next three to six months. You have some high…|

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
|`pmp-pr4-1`|pmp-lectures-1-11|single|predictive|2|A project manager noticed that one team member is frequently absent from project team meetings. Instead of working on the current…|
|`pmp-pr4-2`|pmp-lectures-1-11|single|predictive|5|Due to budgetary constraints, the program manager has requested that project managers prioritize resources over schedule…|
|`pmp-pr4-3`|pmp-lectures-1-11|single|predictive|3|An inexperienced project manager requires HR support for a project. The HR manager hired interns and sent them for training.…|
|`pmp-pr4-4`|pmp-lectures-1-11|single|predictive|3|In the middle of a project, the project manager realizes that a specific set of skills is needed to complete project development.…|
|`pmp-pr4-5`|pmp-lectures-1-11|single|agile|3|During a standup meeting, the project manager discovers that one of the software developers is struggling to complete work…|
|`pmp-pr4-6`|pmp-lectures-1-11|single|agile|3|A key member of an agile team was replaced with a less experienced member. One of the stakeholders is concerned that the project…|
|`pmp-pr4-7`|pmp-lectures-1-11|single|agile|4|A Scrum team noticed a trend of work spilling over for the last several sprints. The root cause of this has been identified as…|
|`pmp-pr4-8`|pmp-lectures-1-11|single|predictive|3|During initial project planning, a project manager was informed that a team member had been exclusively allocated to the project.…|
|`pmp-pr4-9`|pmp-lectures-1-11|single|predictive|3|A project manager is leading a team of software engineers in developing a new mobile app. The team consistently meets deadlines…|
|`pmp-pr4-10`|pmp-lectures-1-11|single|hybrid|3|The project manager of a new hybrid project is trying to put together the project team. The best candidates for team members are…|
|`pmp-pr4-201`|pmp-mocks-1-10|single|agile|2|During week two of a two-week sprint, one of the core team members has an emergency and is unable to work on the project for the…|
|`pmp-pr4-202`|pmp-mocks-1-10|single|predictive|3|A key resource approaches the project manager after the kick-off meeting and expresses concern that they will be unable to…|
|`pmp-pr4-203`|pmp-mocks-1-10|single|agile|3|During an iteration, a key project team member announces their resignation. The project manager realizes that one of the product…|
|`pmp-pr4-204`|pmp-mocks-1-10|single|predictive|1|A project manager has just been assigned a new project team and needs to assign tasks for an upcoming project quickly and…|
|`pmp-pr4-205`|pmp-mocks-1-10|single|predictive|3|A project is in a key phase of execution and everyone is dedicated to achieving success. A functional manager suddenly takes away…|
|`pmp-pr4-206`|pmp-mocks-1-10|single|hybrid|4|Two project managers are working on resourcing their respective projects. Both project managers identified the same subject…|
|`pmp-pr4-207`|pmp-mocks-1-10|single|predictive|3|A project is currently behind schedule when a new team member, known for their high performance, is assigned to the team. While…|
|`pmp-pr4-208`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a hybrid project. Several team members recently exited the project, including the only individual…|
|`pmp-pr4-209`|pmp-mocks-1-10|single|agile|3|A project team working on a high-priority, change-driven project has repeatedly missed release deadlines. The project manager…|
|`pmp-pr4-210`|pmp-mocks-1-10|single|predictive|3|A project manager is assigned to a top innovation project that has high visibility with the company executives. Recently, one of…|
|`pmp-pr4-211`|pmp-mocks-1-10|single|predictive|2|A project execution phase overlaps with a holiday in the country where the majority of team members are based. This phase is…|
|`pmp-pr4-212`|pmp-mocks-1-10|single|agile|3|A project team in the middle of an incremental delivery project decided they need the help of a subject matter expert (SME) who…|
|`pmp-pr4-213`|pmp-mocks-1-10|single|predictive|2|A key project team member is leaving the organization at the halfway point of the project.|
|`pmp-pr4-214`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a complex project with many stakeholders and conflicting priorities. Senior management asked the…|
|`pmp-pr4-215`|pmp-mocks-1-10|single|agile|2|A customer requested a change in functionality for a product that will be delivered in the next sprint. The responsible…|
|`pmp-pr4-216`|pmp-mocks-1-10|single|predictive|3|A project manager learns that a functional manager is assigning a two-week task in another project to a team member. Loss of this…|
|`pmp-pr4-217`|pmp-mocks-1-10|single|predictive|3|A new project manager has been assigned to a global project team. In the next few days, the project team must create a resource…|
|`pmp-pr4-218`|pmp-mocks-1-10|single|predictive|3|In the implementation phase of a project, the project manager receives news that a team lead will be leaving the organization.…|
|`pmp-pr4-219`|pmp-mocks-1-10|single|predictive|2|A new warehouse facility is under construction. The project manager implements team development efforts such as training that…|
|`pmp-pr4-220`|pmp-mocks-1-10|single|predictive|2|During project execution, a project manager learns that a team member's functional manager wants to remove them from a project…|
|`pmp-pr4-221`|pmp-mocks-1-10|single|hybrid|2|A multinational company is locally developing a digital product using a new technology platform. The only expert on this platform…|
|`pmp-pr4-222`|pmp-mocks-1-10|single|predictive|2|A project manager wants to recruit another team member to replace an architect who recently resigned.|
|`pmp-pr4-223`|pmp-mocks-1-10|single|predictive|2|A project manager is facing a delay in a project due to a decision made by the functional manager to provide onsite support for…|
|`pmp-pr4-224`|pmp-mocks-1-10|single|agile|2|A customer requested a change to a functionality that is being delivered in the next iteration. The specialist responsible for…|
|`pmp-pr4-225`|pmp-mocks-1-10|single|predictive|2|A project team is arranging user testing to be completed for a project but does not have a strong enough server to do so. The…|
|`pmp-pr4-226`|pmp-mocks-1-10|single|hybrid|2|During the planning of a project with incremental deliveries, the team realizes that they will need specific expertise for one of…|
|`pmp-pr4-227`|pmp-mocks-1-10|single|predictive|2|A functional manager wants to use resources already allocated to a project.|
|`pmp-pr4-228`|pmp-mocks-1-10|single|predictive|2|While developing the initial schedule baseline, the project manager learns that a functional manager reassigned two resources…|
|`pmp-pr4-229`|pmp-mocks-1-10|single|predictive|3|A project manager has been assigned to a newly approved multi-national project. The successful implementation of the project…|
|`pmp-pr4-230`|pmp-mocks-1-10|single|predictive|3|A project is currently halfway through its timeline when one of the external resources becomes unexpectedly unavailable for the…|
|`pmp-pr4-231`|pmp-mocks-1-10|single|hybrid|2|A complex project is in the planning stage when the sponsor identifies a need for a specialist who is a citizen of a foreign…|
|`pmp-pr4-232`|pmp-mocks-1-10|single|agile|2|A project has the following characteristics: The project manager is being asked to deliver a business result in a compressed…|
|`pmp-pr4-233`|pmp-mocks-1-10|single|predictive|2|A team member is scheduled to visit an overseas client site next week. However, the project manager overheard the team member…|
|`pmp-pr4-234`|pmp-mocks-1-10|single|predictive|2|A project manager is working on a small project that is critical for a larger program milestone. A new resource has been assigned…|
|`pmp-pr4-235`|pmp-mocks-1-10|single|predictive|2|A project is following a strategic plan that includes key milestones that have been aligned with stakeholders. As the work is…|
|`pmp-pr4-236`|pmp-mocks-1-10|single|predictive|2|Close to the end of a large project, some of the technical reports related to the delivered service were not ready. The project…|
|`pmp-pr4-237`|pmp-mocks-1-10|single|predictive|2|In a multidisciplinary project, a project manager is delegating some project management activities to several team members.|
|`pmp-pr4-238`|pmp-mocks-1-10|single|agile|3|An organization is adopting an agile approach to fulfill its customer's requirements. Prior to that, the organization worked with…|
|`pmp-pr4-239`|pmp-mocks-1-10|single|predictive|3|During project execution, a project manager is informed that some resources will soon be removed from the project.|
|`pmp-pr4-240`|pmp-mocks-1-10|single|predictive|2|A project manager works in a matrix organization. Following a reorganization, several key project resources are reassigned to…|
|`pmp-pr4-241`|pmp-mocks-1-10|single|predictive|2|A project manager receives complaints from a project sponsor because of a delay in developing a prototype. The project manager…|
|`pmp-pr4-242`|pmp-mocks-1-10|single|predictive|3|A project manager is experiencing issues with the functional manager of a project team member. The functional manager has given…|
|`pmp-pr4-243`|pmp-mocks-1-10|multi|agile|3|For an agile project, some team members have other commitments or responsibilities. The project manager faces the challenge of…|
|`pmp-pr4-244`|pmp-mocks-1-10|single|agile|3|A project team has been efficiently delivering results. The product upgrade they are working on must be released in three months…|
|`pmp-pr4-245`|pmp-mocks-1-10|single|predictive|3|A project manager is overseeing a large-scale infrastructure project in a hierarchical organization. The project team members are…|
|`pmp-pr4-246`|pmp-mocks-1-10|single|predictive|2|In the middle of an urban development project, the project manager faces a setback as a key engineer and an urban planner…|
|`pmp-pr4-247`|pmp-mocks-1-10|single|hybrid|2|As a project manager overseeing a large-scale renewable energy project, you're confronted with the sudden need to transition your…|
|`pmp-pr4-248`|pmp-mocks-1-10|single|predictive|2|A project manager is tasked with integrating advanced artificial intelligence (AI) features into an existing software product.…|
|`pmp-pr4-249`|pmp-mocks-1-10|single|predictive|2|In the implementation stage of a critical network security project, it is revealed that a specialist responsible for key parts of…|
|`pmp-pr4-250`|pmp-mocks-1-10|single|predictive|2|You are managing your team through difficult project, which has been made more difficult by many team members being new and…|
|`pmp-pr4-251`|pmp-mocks-1-10|single|hybrid|1|You are working on a project where the project team is completely distributed around the country in multiple sites, including…|
|`pmp-pr4-252`|pmp-mocks-1-10|single|agile|2|You are in the planning stages of an Agile project where the functional manager and senior users have provided the high level…|
|`pmp-pr4-253`|pmp-mocks-1-10|single|predictive|3|You are putting together a project team to deliver an accounting system to offices around the country. The accounting system you…|
|`pmp-pr4-254`|pmp-mocks-1-10|single|predictive|2|You put together a resource plan for a large amount of physical inventory to be purchased from overseas. You ordered the…|
|`pmp-pr4-255`|pmp-mocks-1-10|single|agile|2|You have been working on an Agile project for some time and have been blocked by the need for expertise in another team. You need…|
|`pmp-pr4-256`|pmp-mocks-1-10|single|predictive|1|You have finished project planning. All baseline documents are approved and the project team is ready to begin project execution…|
|`pmp-pr4-257`|pmp-mocks-1-10|single|predictive|2|You are in the process of planning the resources required for your project. Work on your project has not started, as your team…|
|`pmp-pr4-258`|pmp-mocks-1-10|single|predictive|2|You have been planning your project using a waterfall methodology, and need to estimate the resources required with your team.…|
|`pmp-pr4-259`|pmp-mocks-1-10|single|predictive|2|You have been working on a project where your organisation wanted only onsite workers, while you believed the project needed…|
|`pmp-pr4-260`|pmp-mocks-1-10|single|predictive|3|You have been assigned to a project that is behind schedule. Some of your team approach you and ask what they should be working…|
|`pmp-pr4-261`|pmp-mocks-1-10|single|predictive|2|Team members from parts of the organisation have been assigned to your project at an agreed level of 50% availability. As the…|
|`pmp-pr4-262`|pmp-mocks-1-10|single|agile|2|The executive management in your organisation has assembled a new team, and dictated that they will use Agile for their product…|
|`pmp-pr4-263`|pmp-mocks-1-10|single|agile|2|You are a program manager overseeing multiple Agile projects within your organization. One of the projects is very important but…|
|`pmp-pr4-264`|pmp-mocks-1-10|single|predictive|2|You are in the initiating stages of a project. The Project Charter has been approved, and initial funding released. You have been…|

### process-5 · Procurement — 124 items

*Plan buying, pick contract types, negotiate, and manage suppliers.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr5-001`|core|multi|predictive|4|You are buying a bespoke control system for a paper mill. The functional requirements are firm and signed off, but the…|
|`pmp-pr5-002`|core|single|hybrid|4|A supplier has missed two consecutive delivery milestones. Their account manager assures you the third will be met. Your contract…|
|`pmp-pr5-003`|core|matching|predictive|3|Prompts: Supply and install 60 identical charging… / Ground remediation where the extent of c… / A specialist signalling upgrade where th…|
|`pmp-pr5-014`|core|single|predictive|3|A county library service is replacing its self-service kiosks. Phase one, 14 branches, has been installed and accepted without…|
|`pmp-pr5-005`|core|single|predictive|4|You are letting a package for the fit-out of a research laboratory. The building's structural condition is unknown until existing…|
|`pmp-pr5-006`|core|matching|predictive|3|Prompts: A supplier is meeting every contractual… / Filed accounts show a supplier's cash po… / A supplier has missed two milestones. Th…|
|`pmp-pr5-007`|core|single|hybrid|3|Your organisation has contracted a development partner on a fixed scope, fixed price basis, but your own delivery runs in…|
|`pmp-pr5-008`|core|single|predictive|4|You are buying the integration of your product with an unfamiliar third-party system. Neither you nor any bidder can specify the…|
|`pmp-pr5-009`|core|fillblank|predictive|5|Your incentive contract sets a target cost of £800,000, a target fee of £100,000, a target price of £900,000 and a ceiling price…|
|`pmp-pr5-010`|core|matching|predictive|3|Prompts: The supplier is delivering competently,… / The supplier was the cheapest compliant… / The supplier and your team disagree mont…|
|`pmp-pr5-011`|core|fillblank|predictive|4|You are deciding whether to build or buy a document rendering component. Buying it costs a licence fee of 50,000 plus 30 for each…|
|`pmp-pr5-012`|core|fillblank|predictive|4|A supplier has offered your migration package on two bases: time and materials at £850 per day, or a firm fixed price of £102,000…|
|`pmp-pr5-013`|core|single|predictive|3|Your systems integrator is four weeks late on a firm fixed-price package. Investigating, you find the delay sits entirely with a…|
|`pmp-case1-007`|case-01|single|predictive|4|Hours billed by the mechanical installer under its time-and-materials contract have risen by 40 per cent over two months, while…|
|`pmp-case4-002`|case-04|single|predictive|4|A trade contact tells you the display case fabricator is struggling to pay its own suppliers. The cases are under a fixed-price…|
|`pmp-pr5-101`|process-5|single|predictive|1|You are running the tender for a £600,000 replacement of a hospital's central kitchen equipment. Four suppliers attended last…|
|`pmp-pr5-102`|process-5|single|predictive|3|Your council is replacing the roof of a leisure centre. The engineer's independent estimate is €1.9 million. Three bids sit…|
|`pmp-pr5-103`|process-5|single|agile|3|You are product owner for an insurer's claims portal, built by a vendor on a capacity basis: a fixed team for twelve two-week…|
|`pmp-pr5-104`|process-5|single|predictive|3|You manage the electrical fit-out of a ferry terminal under a fixed-price contract. The contract names your organisation's…|
|`pmp-pr5-105`|process-5|single|predictive|4|You are negotiating with the preferred contractor for a £3.4 million extension to a secondary school that must open for the…|
|`pmp-pr5-106`|process-5|single|predictive|4|A contractor laying a 4 km water main has claimed £140,000 for rock excavation. The contract leaves ground conditions at the…|
|`pmp-pr5-107`|process-5|single|agile|3|A vendor is building a route-planning app for your logistics company in two-week sprints. The contract assigns all intellectual…|
|`pmp-pr5-108`|process-5|single|hybrid|3|A vendor is delivering a council's housing-repairs service, designing the citizen screens iteratively and the back-office…|
|`pmp-pr5-109`|process-5|single|hybrid|4|A specialist manufacturer is building bespoke switchgear for your data centre, and 60% of the €2.2 million price has been paid in…|
|`pmp-pr5-110`|process-5|single|agile|2|Your bank is choosing a development partner to build a new mobile product with its own Scrum team, and you sit on the five-person…|
|`pmp-pr5-111`|process-5|single|agile|5|A vendor supplies two Scrum teams for your retailer's checkout rebuild under a capacity contract paid monthly. Combined velocity…|
|`pmp-pr5-112`|process-5|single|hybrid|2|A design agency has finished the interpretation panels for a museum's new gallery, which you ran with a fixed build plan and…|
|`pmp-pr5-113`|process-5|multi|predictive|3|A supplier installed 24 fume cupboards in a university's chemistry building under a fixed-price contract. The cupboards have been…|
|`pmp-pr5-114`|process-5|multi|agile|3|A housing association is selecting a vendor to supply a cross-functional team that will build its tenant app in two-week sprints…|
|`pmp-pr5-115`|process-5|multi|hybrid|4|Your hosting contract supporting a hybrid ERP programme expires in five months. The incumbent provider proposes an 18% increase…|
|`pmp-pr5-116`|process-5|matching|predictive|3|Prompts: Replacement bearings for a bridge whose… / 3,000 high-visibility jackets to a publi… / A conservation engineer for a listed via…|
|`pmp-pr5-117`|process-5|matching|hybrid|2|Prompts: The firm does not yet know which supplie… / The firm needs prices and delivery dates… / The firm wants suppliers to propose how…|
|`pmp-pr5-118`|process-5|pulldown|agile|3|Prompts: The vendor asks whether a story that is… / The vendor proposes replacing its lead a… / Your marketing director wants the vendor…|
|`pmp-pr5-119`|process-5|fillblank|predictive|3|A port authority is evaluating two bids for dredging. Published weights: technical 50%, price 30%, programme 20%. Price is scored…|
|`pmp-pr5-120`|process-5|hotspot|agile|2|A charity is choosing a vendor team to build its donor app in sprints. Bidders must pass two mandatory gates — current…|
|`pmp-pr5-121`|process-5|single|predictive|1|A water utility is tendering a £1.4 million contract to replace domestic meters. The invitation to tender states that bids…|
|`pmp-pr5-122`|process-5|single|predictive|3|A regional bus operator is about to tender for telematics units across 300 buses, worth about €750,000. Reviewing the draft…|
|`pmp-pr5-123`|process-5|single|predictive|3|You are the contract administrator for a £5 million flood-defence scheme, named in the contract as the person who certifies…|
|`pmp-pr5-124`|process-5|single|predictive|5|A contractor is building a hotel extension for your client under a €6.8 million fixed-price contract that has no…|
|`pmp-pr5-125`|process-5|single|agile|2|Your pension administrator has selected a vendor to supply a Scrum team for its member portal. One of the four unsuccessful…|
|`pmp-pr5-126`|process-5|single|predictive|3|Eight months into a 12-month warranty, a high-pressure pump supplied for your brewery's new bottling line fails. The supplier…|
|`pmp-pr5-127`|process-5|single|hybrid|3|A consultancy is migrating your pharmaceutical company's laboratory data on a time-and-materials basis, with the data mapping…|
|`pmp-pr5-128`|process-5|single|hybrid|4|A packaging firm makes custom cartons for your food company's new product range under a two-year fixed-price contract with no…|
|`pmp-pr5-129`|process-5|single|agile|2|Your charity's product team builds a fundraising app in two-week sprints. To unblock a sprint goal, a developer bought a cloud…|
|`pmp-pr5-130`|process-5|single|agile|4|Your airline's loyalty app is built in sprints by two vendors on separate contracts: one builds the mobile front end, the other…|
|`pmp-pr5-131`|process-5|single|agile|3|A vendor is building your university's e-learning platform in two-week sprints, invoicing for each accepted increment. The…|
|`pmp-pr5-132`|process-5|single|agile|3|Over 18 months, a vendor built the first release of a city's public-transport ticketing app in sprints. The contract gives the…|
|`pmp-pr5-133`|process-5|multi|agile|4|A vendor team is building a membership platform for a sports federation under a time-and-materials contract with a not-to-exceed…|
|`pmp-pr5-134`|process-5|multi|predictive|4|A supplier of offshore wind-turbine towers tells you that delivery of six towers will be five weeks late because a strike at its…|
|`pmp-pr5-135`|process-5|multi|hybrid|3|Your hospital trust has chosen a contractor to build a modular operating theatre, with the structure delivered to a fixed design…|
|`pmp-pr5-136`|process-5|matching|hybrid|3|Prompts: If the vendor fails financially, the com… / A planned merger may make the outsourced… / When the contract ends, customer data an…|
|`pmp-pr5-137`|process-5|matching|agile|3|Prompts: With the day rate agreed, the vendor's l… / The vendor's negotiator says he can agre… / The vendor says its rate is available on…|
|`pmp-pr5-138`|process-5|pulldown|predictive|2|Prompts: Brake pads are bought in large volumes f… / A £30 door-seal kit is made by only one… / Cleaning materials are cheap, standard a…|
|`pmp-pr5-139`|process-5|fillblank|predictive|3|Your stadium project buys structural steel for £2,400,000, a price set at signature. An economic price adjustment clause says 70%…|
|`pmp-pr5-140`|process-5|hotspot|hybrid|2|A water treatment plant is buying filtration equipment for €1.6 million; the civil works follow a fixed plan while the control…|
|`pmp-pr5-1`|pmp-lectures-1-11|single|predictive|3|Production of a specific machine in a factory was contracted through a foreign supplier. Prior to delivery of the final two…|
|`pmp-pr5-2`|pmp-lectures-1-11|single|predictive|3|A company is expanding, and the project manager is contemplating whether to hire new resources or outsource resources from an…|
|`pmp-pr5-3`|pmp-lectures-1-11|single|agile|3|An agile project to implement a new automation process in a factory is underway. The project manager needs a vendor to help with…|
|`pmp-pr5-4`|pmp-lectures-1-11|single|predictive|3|A two-year project was awarded to a company six months ago, but the procurement process has not yet been initiated.|
|`pmp-pr5-5`|pmp-lectures-1-11|single|predictive|4|A project manager is meeting with a critical third-party supplier that provides discounted services. During the meeting, the…|
|`pmp-pr5-6`|pmp-lectures-1-11|single|predictive|3|A member of the project team who was hired for six months to complete a specific task has decided to leave the project in the…|
|`pmp-pr5-7`|pmp-lectures-1-11|single|predictive|4|A vendor for a large and complex project has been performing more work than what was originally anticipated in the time and…|
|`pmp-pr5-8`|pmp-lectures-1-11|single|predictive|3|The project manager is responsible for a major project that involves working with many vendors. A key project concern is related…|
|`pmp-pr5-9`|pmp-lectures-1-11|single|predictive|4|During project closure, the project sponsor requests that a deliverable, developed by a contractor, be transferred to a nonprofit…|
|`pmp-pr5-10`|pmp-lectures-1-11|single|predictive|4|A complex, multimillion-dollar, multi-year product development project requiring unique knowledge is 70% complete. Contract…|
|`pmp-pr5-11`|pmp-lectures-1-11|single|predictive|2|A project manager walked into a project office and found a team member and a supplier representative engaged in a disagreement…|
|`pmp-pr5-12`|pmp-lectures-1-11|single|predictive|3|In order to reduce procurement time during a bidding process, a project manager decided to contract the additional work to a…|
|`pmp-pr5-13`|pmp-lectures-1-11|single|predictive|4|During the execution of repair work, the root cause of the damage was discovered to be failures that occurred during the…|
|`pmp-pr5-14`|pmp-lectures-1-11|single|predictive|4|During the testing phase of a technical project, the project manager discovers some issues with the final product and…|
|`pmp-pr5-15`|pmp-lectures-1-11|single|predictive|3|A project manager is overseeing a procurement project for a company that has a dedicated procurement management team that handles…|
|`pmp-pr5-16`|pmp-lectures-1-11|single|predictive|3|The development of a final project schedule is delayed due to the inability to negotiate an agreement with one of the vendors…|
|`pmp-pr5-17`|pmp-lectures-1-11|single|predictive|3|A project manager has received quotes from two vendors for the same parts. Vendor A has informed the project manager that they…|
|`pmp-pr5-201`|pmp-mocks-1-10|single|predictive|3|A new project manager has taken over an ongoing project. Progress reports show that the project is within the allocated budget…|
|`pmp-pr5-202`|pmp-mocks-1-10|single|hybrid|4|The service provider and their client agree on about 80% of the scope of a project contract they are working on for the delivery…|
|`pmp-pr5-203`|pmp-mocks-1-10|single|predictive|3|The construction of a building is well-defined through plans and written specifications. The service is to be delivered in two…|
|`pmp-pr5-204`|pmp-mocks-1-10|single|predictive|4|A project manager has just taken over an ongoing project. The previous project manager resigned from the organization. During a…|
|`pmp-pr5-205`|pmp-mocks-1-10|single|predictive|3|A project manager for a delivery project has engaged a vendor to provide testing services.|
|`pmp-pr5-206`|pmp-mocks-1-10|single|predictive|2|Halfway through a year-long project, a hardware supplier sends a project manager notice of an intent to increase prices due to…|
|`pmp-pr5-207`|pmp-mocks-1-10|single|predictive|2|A project is in the testing phase, and the quality control team informs the project manager that the quality of the deliverables…|
|`pmp-pr5-208`|pmp-mocks-1-10|single|predictive|2|Project completion will be delayed because an essential component will not be delivered on time.|
|`pmp-pr5-209`|pmp-mocks-1-10|single|predictive|2|A project has components that are outsourced and the project manager is in the control phase of the procurement process.|
|`pmp-pr5-210`|pmp-mocks-1-10|single|predictive|2|A project has begun to develop a new software application that will be used by the company's sales team. The project is expected…|
|`pmp-pr5-211`|pmp-mocks-1-10|single|predictive|3|A project integrates with several systems developed by various vendors and one of these vendors has gone out of business.|
|`pmp-pr5-212`|pmp-mocks-1-10|single|predictive|3|A project manager is managing a project with a very aggressive delivery schedule and a firm completion date. The project requires…|
|`pmp-pr5-213`|pmp-mocks-1-10|single|predictive|3|A project manager oversees a complex project in which a vendor is responsible for delivering several essential components. The…|
|`pmp-pr5-214`|pmp-mocks-1-10|single|predictive|2|A project manager learns that the project team is involved in an ongoing conflict with an overseas supplier team. The conflict is…|
|`pmp-pr5-215`|pmp-mocks-1-10|multi|predictive|3|A project steering group has made a decision to change the vendor that will deliver a component of a project. (Choose 2).|
|`pmp-pr5-216`|pmp-mocks-1-10|single|predictive|3|A project manager is on a project in collaboration with a vendor. The vendor responded to all the Request for Proposal (RFP)…|
|`pmp-pr5-217`|pmp-mocks-1-10|single|predictive|2|A supplier has been awarded a firm fixed price (FFP) contract to provide various materials for a construction project. The…|
|`pmp-pr5-218`|pmp-mocks-1-10|single|predictive|3|The production of a key component in a construction project is outsourced to a third-party vendor. The vendor informs the project…|
|`pmp-pr5-219`|pmp-mocks-1-10|single|predictive|2|A project manager needs to contract with a provider for a large government project. Project guidelines mandate that all potential…|
|`pmp-pr5-220`|pmp-mocks-1-10|multi|predictive|3|In two months, there will be a delivery of project machines from a factory. The project team is in the process of performing a…|
|`pmp-pr5-221`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a project where a vendor is engaged. The project team has a dependency on the vendor to complete…|
|`pmp-pr5-222`|pmp-mocks-1-10|single|predictive|2|After consultation with executive stakeholders, it was decided that the project manager would replace a vendor while the project…|
|`pmp-pr5-223`|pmp-mocks-1-10|single|predictive|3|A project vendor has informed a project manager that an outsourced project deliverable will be available 6 months later than the…|
|`pmp-pr5-224`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a project with a scope partially delivered by a vendor. The vendor's processes and tools differ from…|
|`pmp-pr5-225`|pmp-mocks-1-10|single|predictive|3|Due to increased price volatility, a vendor asks the project manager of a large development project to make a price change that…|
|`pmp-pr5-226`|pmp-mocks-1-10|single|predictive|2|A team receives outsourced deliverables and determines that they do not meet the agreed-upon acceptance criteria.|
|`pmp-pr5-227`|pmp-mocks-1-10|single|predictive|1|A project manager is managing a project involving a few vendors. The project sponsor asks how the project will acquire goods and…|
|`pmp-pr5-228`|pmp-mocks-1-10|single|predictive|2|A project manager needs to hire suppliers for an international project that has a performance period of several years. The…|
|`pmp-pr5-229`|pmp-mocks-1-10|single|predictive|2|During project execution, the team finds that changes must be made to a component sourced from an outside supplier. The component…|
|`pmp-pr5-230`|pmp-mocks-1-10|single|predictive|3|A project manager has been awarded a firm-fixed-price (FFP) contract to develop a new mobile app for a client. The contract scope…|
|`pmp-pr5-231`|pmp-mocks-1-10|single|predictive|3|The supplier of a tool for a manufacturing project informs the project manager that delivery of the tool will be late. The…|
|`pmp-pr5-232`|pmp-mocks-1-10|single|predictive|3|A contractor is late with a critical project deliverable due to resource challenges. There is a 22% buffer in the budget for cost…|
|`pmp-pr5-233`|pmp-mocks-1-10|multi|hybrid|4|The government of a country intends to construct a multibillion-dollar railway across three major cities and hires a contractor…|
|`pmp-pr5-234`|pmp-mocks-1-10|single|predictive|2|A member of a development project team informs the project manager that the operating system is not going to be installed on time…|
|`pmp-pr5-235`|pmp-mocks-1-10|single|predictive|3|For the implementation of a sophisticated security software across multiple departments, the selected external provider asserts…|
|`pmp-pr5-236`|pmp-mocks-1-10|single|predictive|3|For an upcoming critical initiative, a project manager must engage a vendor notorious for not meeting deadlines, yet uniquely…|
|`pmp-pr5-237`|pmp-mocks-1-10|single|predictive|2|At the beginning of a green building project, the project manager is tasked by the lead architect to engage a key supplier known…|
|`pmp-pr5-238`|pmp-mocks-1-10|single|predictive|2|You are working on a high value construction project to include smart-home components within multi-million dollar buildings.…|
|`pmp-pr5-239`|pmp-mocks-1-10|single|predictive|2|A missing item has been uncovered in the project scope that your project team cannot deliver. The item is not complex and seems…|
|`pmp-pr5-240`|pmp-mocks-1-10|single|hybrid|3|You are working on a project where part of the work is planned carefully upfront, and another part is subject to change. You…|
|`pmp-pr5-241`|pmp-mocks-1-10|single|predictive|2|You have completed a make-or-buy analysis in your project and found that part of your deliverable is best created by an external…|
|`pmp-pr5-242`|pmp-mocks-1-10|single|agile|2|Your agile project needs a third party vendor to help complete the work. The product owner has multiple features on their product…|
|`pmp-pr5-243`|pmp-mocks-1-10|single|predictive|2|You have completed a make-or-buy analysis and you agree to hire a third-party vendor to complete part of your product. You create…|
|`pmp-pr5-244`|pmp-mocks-1-10|single|predictive|2|You are the project manager on a high profile software project that involves automation technology not used before in your…|
|`pmp-pr5-245`|pmp-mocks-1-10|single|predictive|2|You are working on a project charter for the initiating stage of a new project that will deliver a new data system. You approach…|
|`pmp-pr5-246`|pmp-mocks-1-10|single|predictive|2|You have planned, executed and monitored a project to completion. The customer has just received the finished product and you…|
|`pmp-pr5-247`|pmp-mocks-1-10|single|predictive|2|You are working on a complex, high-value project to replace the telecommunications system across your organisation. You have…|
|`pmp-pr5-248`|pmp-mocks-1-10|single|predictive|2|Part of your project work needs to be outsourced to a supplier. You are asked to ensure that cost for the outsourced portion of…|
|`pmp-pr5-249`|pmp-mocks-1-10|single|agile|2|You are a project manager on an Agile project that requires an external third party to develop software for one of your features.…|
|`pmp-pr5-250`|pmp-mocks-1-10|single|predictive|2|You are working on a project where a vendor is performing a large amount of the work. Lately they have not been performing well,…|
|`pmp-pr5-251`|pmp-mocks-1-10|single|predictive|2|A recent news article spread a rumour about a supplier who is providing services for the project you are working on. They are…|
|`pmp-pr5-252`|pmp-mocks-1-10|single|predictive|1|You are working on a project and it has become apparent that part of the product your company was creating they won't be able to…|

### process-6 · Project finance — 94 items

*Forecast, track and control money, including contingency and reserves.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr6-014`|core|fillblank|predictive|2|The estimate for a county records office's archive digitisation project has been rolled up from four work packages: scanning…|
|`pmp-pr6-002`|core|single|predictive|3|A risk you identified and quantified during planning has occurred, and responding to it will cost 60,000. Your contingency…|
|`pmp-pr6-003`|core|single|agile|3|Finance asks for a cost forecast for an agile product team funded quarterly. The team is stable at eight people, the run rate is…|
|`pmp-pr6-015`|core|fillblank|predictive|3|You are the client-side project manager for a school's sports hall extension. The contractor is valued monthly for work done, and…|
|`pmp-pr6-005`|core|fillblank|predictive|2|Your project holds a contingency reserve of £250,000 for identified risks, within a total budget that also includes a separate…|
|`pmp-pr6-006`|core|single|predictive|4|Your cost forecast shows the project finishing 12 per cent over budget. The overspend is driven by a supplier claim that is…|
|`pmp-pr6-016`|core|single|agile|3|Your customer-identity verification team of seven is funded 60 per cent by the retail banking division and 40 per cent by the…|
|`pmp-pr6-008`|core|single|predictive|3|At the end of month seven your project reports planned value of £600,000, earned value of £660,000 and actual cost of £750,000.…|
|`pmp-pr6-009`|core|fillblank|predictive|4|Your project has a budget at completion of £2,400,000. At the reporting date, earned value is £1,080,000 and actual cost is…|
|`pmp-pr6-017`|core|single|hybrid|4|A port is replacing its gate-entry system. Barrier and camera installation is planned predictively and carries £180,000 of…|
|`pmp-pr6-011`|core|fillblank|predictive|4|A rail systems project has a budget at completion of 5,000,000. At the reporting date the earned value is 2,000,000 and the…|
|`pmp-pr6-012`|core|single|predictive|3|Your project's cost baseline totals 4,200,000 across three years and the forecast remains within it. Your finance business…|
|`pmp-pr6-018`|core|fillblank|predictive|3|A hydro-electric turbine refurbishment has a budget at completion of £2,750,000. At month nine the actual cost is £1,180,000, and…|
|`pmp-pr6-061`|core|graphic|predictive|3|The chart summarises cumulative planned value, earned value and actual cost, in thousands, at the end of each of the first five…|
|`pmp-case1-001`|case-01|fillblank|predictive|3|Using Exhibit 1, work out the cost performance index at the end of month 6, then the estimate at completion in thousands,…|
|`pmp-case4-004`|case-04|single|predictive|4|Contingency reserve stands at 90,000 of an original 300,000, with three months of building works left. The risks still open on…|
|`pmp-pr6-101`|process-6|single|predictive|1|A district council asks you to scope the refurbishment of a 1970s leisure centre. So far you have a condition survey and a…|
|`pmp-pr6-102`|process-6|single|agile|3|Your travel-booking app team is funded for eight more sprints at €52,000 each, and the sponsor has said that is the full budget…|
|`pmp-pr6-103`|process-6|single|hybrid|3|Halfway through an ERP rollout for a logistics group, the executive committee cuts your budget from €2.0m to €1.7m as part of…|
|`pmp-pr6-104`|process-6|single|predictive|3|On a hospital wing fit-out, the monthly report shows a project cost performance index of 1.01 and the sponsor describes the…|
|`pmp-pr6-105`|process-6|multi|agile|3|Your organisation's finance director asks the three Scrum teams building a new claims product to report monthly 'percentage…|
|`pmp-pr6-106`|process-6|single|agile|2|Your organisation lets each agile team spend up to 5,000 a quarter on tools and services without further approval, provided…|
|`pmp-pr6-107`|process-6|single|predictive|2|At the end of month five of a water-treatment plant upgrade, the finance report shows actual spend to date of 1,240,000 against…|
|`pmp-pr6-108`|process-6|matching|predictive|2|Prompts: A board wants an early figure for a dist… / Data cabling must be installed in 340 re… / The design for a laboratory fit-out is c…|
|`pmp-pr6-109`|process-6|single|hybrid|3|A housing association capitalises development of new systems but expenses maintenance of live ones. Your hybrid team splits its…|
|`pmp-pr6-110`|process-6|single|predictive|4|Your budget for a food-packaging line is held in sterling, but the main equipment contract, worth €2.4m, is priced in euros and…|
|`pmp-pr6-111`|process-6|fillblank|predictive|4|A coastal flood-defence scheme sizes its contingency reserve from expected monetary value. The register holds piling refusal (30…|
|`pmp-pr6-112`|process-6|hotspot|predictive|3|The cost management plan for a highway resurfacing contract requires a formal variance report to the sponsor for any control…|
|`pmp-pr6-113`|process-6|pulldown|agile|3|Prompts: A new savings feature rests on an untest… / A long-lived payments platform team of s… / A one-off data-centre migration has a we…|
|`pmp-pr6-114`|process-6|matching|hybrid|4|Prompts: Cooling: a one-off crane hire after a ch… / Electrical: the electricians' negotiated… / Fit-out: work is over cost and behind sc…|
|`pmp-pr6-115`|process-6|single|agile|4|Finance proposes saving €540,000 by releasing one of the two teams building a language-learning app for the remaining nine months…|
|`pmp-pr6-116`|process-6|single|predictive|5|A regional airport's baggage-system replacement has spent £3.1m of its £4.0m budget. A bottom-up re-estimate, reviewed by the…|
|`pmp-pr6-117`|process-6|single|hybrid|1|Reviewing the cost report for a hybrid warehouse-automation project, you notice that a 48,000 invoice for conveyor spares…|
|`pmp-pr6-118`|process-6|multi|agile|3|Your genomics data-platform team holds its own monthly cloud budget of €12,000. On day ten of the month the cost dashboard shows…|
|`pmp-pr6-119`|process-6|multi|hybrid|3|On a broadcaster's studio refit, the in-house fit-out runs in sprints while a contractor installs the lighting rig to a fixed…|
|`pmp-pr6-120`|process-6|single|agile|3|Three weeks before quarter end, your insurance-pricing product team is forecast to finish €90,000 under its €600,000 quarterly…|
|`pmp-pr6-121`|process-6|single|predictive|3|You are preparing the cost baseline for a four-year canal lock refurbishment. The estimating team's bottom-up figure of £38m is…|
|`pmp-pr6-122`|process-6|single|predictive|3|Three months ago the change control board approved adding a second loading bay to a distribution centre build, worth £420,000 of…|
|`pmp-pr6-123`|process-6|single|predictive|4|On a museum's gallery redevelopment, the structural steel package closed £180,000 under its control account budget after a…|
|`pmp-pr6-124`|process-6|single|predictive|5|A water company is installing 12,000 smart meters with a budget at completion of £3.6m, earned at £300 per installed meter. The…|
|`pmp-pr6-125`|process-6|single|hybrid|3|A university's research building is delivered as a hybrid programme, and the groundworks are let under a firm fixed-price…|
|`pmp-pr6-126`|process-6|single|hybrid|2|On a hybrid laboratory-equipment upgrade, a team lead asks you to approve a €48,000 order for a spectrometer. He points to the…|
|`pmp-pr6-127`|process-6|single|agile|1|You are the delivery lead for an agile team that includes two contractors supplied on a time-and-materials basis. The supplier's…|
|`pmp-pr6-128`|process-6|single|agile|3|Over three quarters your payments-app team's cost has held at about €95,000 a sprint while its velocity has risen from 40 to 62…|
|`pmp-pr6-129`|process-6|single|agile|3|Your team at a car insurer is about to spend an estimated €260,000 building a telematics pricing feature. The product owner…|
|`pmp-pr6-130`|process-6|single|agile|2|Your release forecast to the sponsor says nine more sprints at €48,000 each, a remaining cost of €432,000. From this month, the…|
|`pmp-pr6-131`|process-6|single|agile|3|Your organisation funds value streams, and each epic is approved by the portfolio board with a cost guardrail: if spend reaches…|
|`pmp-pr6-132`|process-6|single|hybrid|4|A charity's digital team is building a volunteer-matching service, part-funded by a £250,000 government grant restricted to…|
|`pmp-pr6-133`|process-6|multi|predictive|3|Midway through a two-year flood-warning systems project, the organisation's finance function raises the overhead rate charged on…|
|`pmp-pr6-134`|process-6|multi|hybrid|3|Consolidating estimates for a hybrid retail-systems programme, you find that each of the five workstream leads has quietly added…|
|`pmp-pr6-135`|process-6|multi|agile|4|Your product team's quarterly funding covers its eight people and a test environment. Load testing shows that when the new…|
|`pmp-pr6-136`|process-6|matching|agile|3|Prompts: Whether to move the only engineer who un… / Whether to pay €15,000 for an extra fort… / Whether a €20,000 fee to expedite a thir…|
|`pmp-pr6-137`|process-6|matching|hybrid|4|Prompts: Before estimating a bridge refurbishment… / Every package in a signalling upgrade wa… / A business unit submitted a low estimate…|
|`pmp-pr6-138`|process-6|pulldown|predictive|2|Prompts: A two-day independent safety inspection… / A nine-month tunnelling package with six… / Project controls support that runs for t…|
|`pmp-pr6-139`|process-6|fillblank|predictive|3|A theatre's air-handling upgrade has three work packages with three-point cost estimates (optimistic / most likely / pessimistic,…|
|`pmp-pr6-140`|process-6|hotspot|predictive|2|A Monte Carlo simulation of a ferry terminal refurbishment's total cost, against a base estimate of £4.6m, produced the five…|
|`pmp-pr6-1`|pmp-lectures-1-11|single|predictive|4|After the transition and completion of a large equipment installation project, the project manager receives a request from the…|
|`pmp-pr6-2`|pmp-lectures-1-11|single|predictive|4|During the development phase of a construction project, key stakeholders approved a project budget. The team identified risks and…|
|`pmp-pr6-3`|pmp-lectures-1-11|single|agile|5|An innovative project is leveraging artificial intelligence (AI) to identify chemical compounds for the development of new…|
|`pmp-pr6-4`|pmp-lectures-1-11|single|predictive|2|A project manager is appointed to design and develop a strategy for a government organization. The project manager recognizes…|
|`pmp-pr6-5`|pmp-lectures-1-11|single|predictive|3|A team is working on a new technology deployment project. This product has never been implemented before so there is no past data…|
|`pmp-pr6-6`|pmp-lectures-1-11|single|predictive|4|A project manager has been assigned to a new, multimillion-dollar project that is similar to a project the company executed three…|
|`pmp-pr6-7`|pmp-lectures-1-11|single|predictive|4|During a progress meeting, the project team learns that an identified risk materialized. The project manager decides to execute…|
|`pmp-pr6-8`|pmp-lectures-1-11|single|predictive|3|A construction company is implementing a new strategic project with a fixed budget. While monitoring the project, it was…|
|`pmp-pr6-9`|pmp-lectures-1-11|single|predictive|3|At project initiation, a quote for the required services was requested from a vendor, and the work was estimated at US$100,000.…|
|`pmp-pr6-201`|pmp-mocks-1-10|single|hybrid|4|A team is working on a new product development project using a hybrid approach. After the fourth sprint, they learn that the…|
|`pmp-pr6-202`|pmp-mocks-1-10|single|predictive|4|A project manager is developing a project budget. After estimating the cost of each activity, the manager adds a contingency…|
|`pmp-pr6-203`|pmp-mocks-1-10|single|agile|3|A team is working on a new project and the sponsor refuses to approve a budget increase needed for the current iteration.|
|`pmp-pr6-204`|pmp-mocks-1-10|single|hybrid|2|A project manager is managing a hybrid project with a fixed time and fixed price agreement. The project manager is having…|
|`pmp-pr6-205`|pmp-mocks-1-10|single|agile|2|A project manager is managing an agile project. In order to make sure there is enough funding available for the project, the…|
|`pmp-pr6-206`|pmp-mocks-1-10|single|predictive|2|A new project has high uncertainty in the cost required for completion.|
|`pmp-pr6-207`|pmp-mocks-1-10|single|predictive|3|A project is set to install three MRI units in a hospital; one unit has passed acceptance testing and two are under construction.…|
|`pmp-pr6-208`|pmp-mocks-1-10|single|predictive|1|A project manager is assigned to join a mega industrial project already underway. The latest performance indices are CPI=0.42 and…|
|`pmp-pr6-209`|pmp-mocks-1-10|single|agile|3|During an iteration the team realizes that the project will overrun the budget. This is due to the support resources being…|
|`pmp-pr6-210`|pmp-mocks-1-10|single|agile|3|A project manager secures project approval with a constrained budget due to fiscal limitations. The sponsor emphasizes no…|
|`pmp-pr6-211`|pmp-mocks-1-10|single|predictive|2|An approved change request adds an incentive to a lump sum contract if the contractor is able to maintain the current baseline…|
|`pmp-pr6-212`|pmp-mocks-1-10|single|predictive|3|A project manager discovers that recent legislative updates necessitate additional security enhancements to the project. Although…|
|`pmp-pr6-213`|pmp-mocks-1-10|single|agile|3|A project manager needs to prioritize multiple projects within a portfolio and requires accurate budget estimates. The process…|
|`pmp-pr6-214`|pmp-mocks-1-10|single|predictive|2|A project manager is overseeing the construction of a new office building. The project is running over budget due to the high…|
|`pmp-pr6-215`|pmp-mocks-1-10|single|predictive|2|In a construction project, the project manager decides to implement Value Engineering (VE) to enhance the project's value. The…|
|`pmp-pr6-216`|pmp-mocks-1-10|single|predictive|2|A project manager is conducting a Value Engineering (VE) workshop to identify ways to improve a project's value. During the…|
|`pmp-pr6-217`|pmp-mocks-1-10|single|predictive|2|During the construction of a new office building, the project manager learns that unforeseen geological issues will significantly…|
|`pmp-pr6-218`|pmp-mocks-1-10|single|predictive|2|Faced with a constrained budget and a tight deadline for launching a new product development project, the project manager…|
|`pmp-pr6-219`|pmp-mocks-1-10|single|predictive|2|You are working on a business case where you know that the industry is undergoing a significant change at the moment, and there…|
|`pmp-pr6-220`|pmp-mocks-1-10|single|predictive|3|When reviewing the current progress with your project sponsor, they notice that the system solution is missing a critical piece…|
|`pmp-pr6-221`|pmp-mocks-1-10|single|predictive|2|The project has faced serious impacts in getting materials, with supply chains being disrupted and a global pandemic, and is…|
|`pmp-pr6-222`|pmp-mocks-1-10|single|predictive|2|You are planning a project where supply chains have been impacted, and the requirements are complex and intertwined with other…|
|`pmp-pr6-223`|pmp-mocks-1-10|single|predictive|2|You are working as a Project Manager and your team is about to deliver the completed system to the customer. During the final…|
|`pmp-pr6-224`|pmp-mocks-1-10|single|predictive|3|You are the project manager on a waterfall project, and you have planned the requirements, scope and schedule with your team. You…|
|`pmp-pr6-225`|pmp-mocks-1-10|single|predictive|2|The project you are managing has defined the scope and created the Scope Baseline with the Scope Statement, WBS and WBS…|
|`pmp-pr6-226`|pmp-mocks-1-10|single|predictive|2|Multiple unforeseen events have happened that have set your project behind schedule, and in trying to bring it back on track you…|
|`pmp-pr6-227`|pmp-mocks-1-10|single|predictive|2|Your project plan includes an estimate of how long the project will take (via a project schedule), how much it will cost and the…|
|`pmp-pr6-228`|pmp-mocks-1-10|single|predictive|2|You are planning your project and finalizing the project costs. You have broken down the scope into work packages and estimated…|
|`pmp-pr6-229`|pmp-mocks-1-10|single|predictive|2|You are working as a project manager and created the cost estimate to bid on a government contract, where the scope was set by…|

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
|`pmp-pr7-1`|pmp-lectures-1-11|single|agile|3|A project team member informs the project manager that all items are complete and the team is ready to move on to the next…|
|`pmp-pr7-2`|pmp-lectures-1-11|single|agile|3|A company is developing a new product that needs to implement nonfunctional security requirements. The product owner is concerned…|
|`pmp-pr7-3`|pmp-lectures-1-11|single|agile|3|A functional manager involved in a project complains about the latest quality metrics reports associated with all previous…|
|`pmp-pr7-4`|pmp-lectures-1-11|single|predictive|3|A customer has rejected an interim project deliverable.|
|`pmp-pr7-5`|pmp-lectures-1-11|single|predictive|3|While closing a project, the client finds some defects in the deliverable.|
|`pmp-pr7-6`|pmp-lectures-1-11|single|predictive|3|During a final inspection of a new product, the project team discovers a significant defect in one of the key deliverables. This…|
|`pmp-pr7-201`|pmp-mocks-1-10|single|agile|2|The first iteration of an agile project consists of a minimum viable product (MVP) that is now successfully completed. The team…|
|`pmp-pr7-202`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a project to deliver a new product in a heavily regulated industry and must prioritize quality…|
|`pmp-pr7-203`|pmp-mocks-1-10|single|predictive|3|An organization has a policy requiring all project managers to conduct a standardized audit of another project manager's project…|
|`pmp-pr7-204`|pmp-mocks-1-10|single|predictive|3|A project manager is newly assigned to an ongoing project and determines that the quality has been consistent. There is a…|
|`pmp-pr7-205`|pmp-mocks-1-10|single|predictive|3|A project manager assigned to an existing project finds that the deliverable quality has been inconsistent. The project manager…|
|`pmp-pr7-206`|pmp-mocks-1-10|single|predictive|3|A project manager in a manufacturing company was notified of a failure of the delivered machinery during the initial tests. The…|
|`pmp-pr7-207`|pmp-mocks-1-10|single|predictive|3|A new product was accepted by the customer, but after two weeks the project manager received a complaint from the customer saying…|
|`pmp-pr7-208`|pmp-mocks-1-10|single|predictive|2|A serious quality issue has been identified during the implementation of a major public infrastructure project. The issue is…|
|`pmp-pr7-209`|pmp-mocks-1-10|single|hybrid|2|A company is working on a hybrid project in which the deliverables have quality issues at the end of one of the sprints.|
|`pmp-pr7-210`|pmp-mocks-1-10|single|predictive|3|A project manager working in a matrix organization is managing a project that is scheduled to be completed in one month. At…|
|`pmp-pr7-211`|pmp-mocks-1-10|single|predictive|3|A project for an accounts receivable system that was recently launched into production received a fine from a regulatory body…|
|`pmp-pr7-212`|pmp-mocks-1-10|single|predictive|2|A project is in the execution phase, and product testing is almost complete. A team member proposes that the testing can be…|
|`pmp-pr7-213`|pmp-mocks-1-10|single|predictive|2|The client agreed to the standards that would be used to assess final deliverables prior to hand-off, but is now asking for…|
|`pmp-pr7-214`|pmp-mocks-1-10|single|predictive|2|A major project deliverable is completed.|
|`pmp-pr7-215`|pmp-mocks-1-10|single|predictive|3|A project team has not been performing user acceptance tests on a deliverable because the product has been working as expected in…|
|`pmp-pr7-216`|pmp-mocks-1-10|single|predictive|3|Due to a budget issue, the project team decides to use low-cost equipment with higher testing uncertainty to reduce cost.…|
|`pmp-pr7-217`|pmp-mocks-1-10|single|predictive|3|During final inspection of a deliverable, the quality team observes that there is a deviation on a particular component. The…|
|`pmp-pr7-218`|pmp-mocks-1-10|single|agile|3|An agile project team is implementing a supporting technology platform across departments. They're planning a readiness review…|
|`pmp-pr7-219`|pmp-mocks-1-10|single|predictive|3|All quality controls have passed, and a project deliverable is ready to be released to the customer. However, a change request to…|
|`pmp-pr7-220`|pmp-mocks-1-10|single|agile|3|During a retrospective meeting, the project team confirmed that all deliverables were completed according to the specifications…|
|`pmp-pr7-221`|pmp-mocks-1-10|single|agile|3|A project manager is creating a software project's quality management plan. While reviewing lessons learned from past, similar…|
|`pmp-pr7-222`|pmp-mocks-1-10|single|agile|3|A project manager is responsible for a new agile project that is global in nature. After a few sprints have been completed, the…|
|`pmp-pr7-223`|pmp-mocks-1-10|single|predictive|2|While reviewing the quality of deliverables with the team, the project manager determines that several quality issues could have…|
|`pmp-pr7-224`|pmp-mocks-1-10|single|predictive|2|A new project manager joins a project overseen by a senior project manager. The sponsor asks the new project manager to develop…|
|`pmp-pr7-225`|pmp-mocks-1-10|single|predictive|2|A customer has informed the project manager that they do not accept the project deliverables.|
|`pmp-pr7-226`|pmp-mocks-1-10|single|predictive|3|A customer identifies performance issues on a newly implemented product.|
|`pmp-pr7-227`|pmp-mocks-1-10|multi|agile|3|One of the retrospective action items that has recurred for the past two iterations is related to the verification and validation…|
|`pmp-pr7-228`|pmp-mocks-1-10|single|predictive|2|For quality assurance, a quality audit was conducted on the processes being used in the project execution plan. One of the…|
|`pmp-pr7-229`|pmp-mocks-1-10|multi|predictive|3|A project is challenged by a multitude of problems resulting in defects and stakeholder complaints. The project manager has been…|
|`pmp-pr7-230`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to take over an ongoing project. During the quality review, the team discovered that 40% of the…|
|`pmp-pr7-231`|pmp-mocks-1-10|multi|predictive|3|A project team is unexpectedly audited. Several nonconformances were identified during the audit. (Choose three)|
|`pmp-pr7-232`|pmp-mocks-1-10|single|agile|2|During product demonstration, a stakeholder suggests that the product does not meet expectations.|
|`pmp-pr7-233`|pmp-mocks-1-10|single|predictive|2|A stakeholder complains that a product component is not easy to use.|
|`pmp-pr7-234`|pmp-mocks-1-10|single|agile|1|A developer has completed the deliverable for a feature. The product owner is not sure how to assess the quality of the…|
|`pmp-pr7-235`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading an intricate biotechnology research project characterized by significant ambiguity and…|
|`pmp-pr7-236`|pmp-mocks-1-10|single|predictive|3|Just before the final review of a new software development project, the project manager is informed by the quality assurance team…|
|`pmp-pr7-237`|pmp-mocks-1-10|single|predictive|2|A project manager is preparing for a quality audit of a new software development project. The audit team needs to ensure the…|
|`pmp-pr7-238`|pmp-mocks-1-10|single|predictive|2|During the execution phase of a manufacturing project, an internal quality audit reveals several non conformances in the…|
|`pmp-pr7-239`|pmp-mocks-1-10|single|predictive|2|In the middle of a large infrastructure project, the project manager receives a report from an external quality auditor…|
|`pmp-pr7-240`|pmp-mocks-1-10|single|predictive|2|In the initial stages of launching a pioneering software tool aimed at small businesses, the project manager is focused on…|
|`pmp-pr7-241`|pmp-mocks-1-10|single|hybrid|2|Towards the end of a large-scale infrastructure development project, the project manager detects a slowdown in progress with a…|
|`pmp-pr7-242`|pmp-mocks-1-10|single|agile|2|You are working on a project where senior users have been testing the product as parts are released. They have found some missing…|
|`pmp-pr7-243`|pmp-mocks-1-10|single|agile|1|Your project team are trialling an agile way of work. The Product Owner has a clear vision for the product. There is confusion in…|
|`pmp-pr7-244`|pmp-mocks-1-10|single|predictive|2|You have been working closely with the quality manager of your project, planning the budget for the project phase that is due to…|
|`pmp-pr7-245`|pmp-mocks-1-10|single|agile|3|Your agile project has recently had multiple quality issues. Project features have been prioritised by the Product Owner, and…|
|`pmp-pr7-246`|pmp-mocks-1-10|single|agile|2|The product owner has prioritised the product backlog of features, and the team creates user stories with estimates and places…|
|`pmp-pr7-247`|pmp-mocks-1-10|single|predictive|2|You are working on a new software application with a lot of complex interdependencies. The senior user providing and testing most…|
|`pmp-pr7-248`|pmp-mocks-1-10|single|agile|2|You are working with the team on the team charter and have outlined the project vision, mission, agreed meeting times and…|
|`pmp-pr7-249`|pmp-mocks-1-10|single|predictive|2|You have been asked to work on a process improvement within your team. Your manager has secured a developer to assist you with…|
|`pmp-pr7-250`|pmp-mocks-1-10|single|predictive|2|As your project begins, it becomes clear the most of the business analysts, developers and testers are not familiar with system…|
|`pmp-pr7-251`|pmp-mocks-1-10|single|predictive|2|You have been asked to monitor the changes to a product before completing your next project phase. You review the control chart.…|
|`pmp-pr7-252`|pmp-mocks-1-10|single|predictive|2|A quality assurance manager has been hired externally to test the project and manage defects. She executed the process in the…|
|`pmp-pr7-253`|pmp-mocks-1-10|single|predictive|3|You are in the process of measuring the outcome of one of your deliverables on the business. The data shows that there seems to…|
|`pmp-pr7-254`|pmp-mocks-1-10|single|predictive|2|You take the completed scope to your project sponsor for them to accept. The Project Sponsor advises she cannot accept the…|
|`pmp-pr7-255`|pmp-mocks-1-10|single|agile|2|You are working as a project manager in an Agile team.|
|`pmp-pr7-256`|pmp-mocks-1-10|single|agile|1|You are working with your agile team, and they are confused about when the best time is to determine the acceptance test criteria…|
|`pmp-pr7-257`|pmp-mocks-1-10|single|hybrid|2|You have been working with a project team, delivering new items very quickly. The team were so focused on delivering items…|

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
|`pmp-pr9-1`|pmp-lectures-1-11|single|hybrid|3|A hybrid project involving many stakeholders is underway. As requested by the project management office (PMO), the project status…|
|`pmp-pr9-2`|pmp-lectures-1-11|single|predictive|4|A new program manager checks the status of a project. The forecast shows the project is slightly behind schedule. However, in…|
|`pmp-pr9-3`|pmp-lectures-1-11|single|predictive|3|An entrepreneur has initiated a software development project. The project charter has been approved, and a project manager has…|
|`pmp-pr9-4`|pmp-lectures-1-11|single|predictive|3|During a steering committee meeting, a senior executive raised a concern that although the key cost parameters are reported to be…|
|`pmp-pr9-5`|pmp-lectures-1-11|single|predictive|3|A project has a budgeted cost of $120,000. According to the schedule, 15% of the project should have been completed after one…|
|`pmp-pr9-6`|pmp-lectures-1-11|single|predictive|3|A project advisory team is working remotely from the core project team. There is a constant flow of information between these two…|
|`pmp-pr9-7`|pmp-lectures-1-11|single|agile|3|The leadership team of a company asked a project manager for an ad-hoc status update on the latest iteration of a project. The…|
|`pmp-pr9-201`|pmp-mocks-1-10|single|agile|2|An agile team is doing an excellent job of focusing on high-priority items and completing them in less time than required.|
|`pmp-pr9-202`|pmp-mocks-1-10|single|predictive|3|A project sponsor is increasingly anxious about the value returned from the investment made in a project and wants to ensure that…|
|`pmp-pr9-203`|pmp-mocks-1-10|single|agile|2|While conducting a retrospective meeting, team members were surprised from the performance results of the team shared by the…|
|`pmp-pr9-204`|pmp-mocks-1-10|single|agile|2|During an agile retrospective session, several team members mentioned that they were confused about the iteration's progress.|
|`pmp-pr9-205`|pmp-mocks-1-10|single|agile|3|A company is developing a breakthrough product in a highly competitive and rapidly evolving market. The project manager…|
|`pmp-pr9-206`|pmp-mocks-1-10|single|predictive|2|While preparing a project's third progress report, the project manager identifies that the project will experience delays due to…|
|`pmp-pr9-207`|pmp-mocks-1-10|single|agile|2|A project manager is leading an agile project. The product owner complains that the project team is constantly completing fewer…|
|`pmp-pr9-208`|pmp-mocks-1-10|single|agile|2|A project manager conducts a lessons learned meeting and project team members mention that they are not always aware of the…|
|`pmp-pr9-209`|pmp-mocks-1-10|single|predictive|2|A project manager leads a project team on a project. This project's goal is to enhance a product's capability in the market.|
|`pmp-pr9-210`|pmp-mocks-1-10|single|agile|3|A newly launched product prototype is receiving a number of customer complaints. To reduce negative feedback, the team comes up…|
|`pmp-pr9-211`|pmp-mocks-1-10|multi|hybrid|3|The project teams are having difficulties coordinating their tasks due to several constraints and dependencies. The project…|
|`pmp-pr9-212`|pmp-mocks-1-10|multi|predictive|2|In an ongoing project, performance is continuously evaluated to determine benefits. (Choose two)|
|`pmp-pr9-213`|pmp-mocks-1-10|single|predictive|2|A project team has complete access to all project-related data, including visual data and information like the Gantt chart,…|
|`pmp-pr9-214`|pmp-mocks-1-10|multi|agile|3|(Choose two)|
|`pmp-pr9-215`|pmp-mocks-1-10|single|predictive|2|At the outset of a software development project, the project manager is tasked with devising a strategy for assessing the…|
|`pmp-pr9-216`|pmp-mocks-1-10|single|predictive|2|During a complex software development project, a project manager notices significant variances between the planned and actual…|
|`pmp-pr9-217`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a large infrastructure project that is facing schedule delays. The project manager wants to use…|
|`pmp-pr9-218`|pmp-mocks-1-10|single|predictive|2|A software development project is midway through its timeline, and the project manager notices that the actual progress is…|
|`pmp-pr9-219`|pmp-mocks-1-10|single|predictive|2|In the midst of a major software development project, the project manager is preparing for an upcoming executive in-depth review.…|
|`pmp-pr9-220`|pmp-mocks-1-10|single|predictive|2|During the final stages of a significant infrastructure project, it was discovered that comprehensive reports on critical…|
|`pmp-pr9-221`|pmp-mocks-1-10|single|agile|2|You are working as a project manager for a medium-sized software project. The project sponsor believes that the team have not…|
|`pmp-pr9-222`|pmp-mocks-1-10|single|hybrid|2|In a hybrid project environment, stakeholders are experiencing challenges in accessing the most recent version of the project…|
|`pmp-pr9-223`|pmp-mocks-1-10|single|hybrid|3|As a new software development project kicks off, when is it most appropriate for the project manager to conduct the first project…|
|`pmp-pr9-224`|pmp-mocks-1-10|single|agile|1|Following a directive to enhance operational efficiencies within a multinational corporation, the executive team has requested an…|
|`pmp-pr9-225`|pmp-mocks-1-10|single|agile|2|You are working in an agile team where executives in the team you are delivering to are asking for a large display of reports to…|
|`pmp-pr9-226`|pmp-mocks-1-10|single|agile|3|Multiple features have been released, but they are not having the effect the product owner believes they should. Your team are…|
|`pmp-pr9-227`|pmp-mocks-1-10|single|agile|2|You are working on an agile project where the project customer wants a reliable way to measure the project performance, and you…|
|`pmp-pr9-228`|pmp-mocks-1-10|single|agile|2|You are working on an agile project where the project sponsor and executive managers require lengthy reports about the project…|
|`pmp-pr9-229`|pmp-mocks-1-10|single|hybrid|2|You are working on a project that has planned as much as they can up-front. You would like them to move to a more agile way of…|
|`pmp-pr9-230`|pmp-mocks-1-10|single|agile|2|You are working as a product owner in an agile team, where the main measure of project success is the number of features…|
|`pmp-pr9-231`|pmp-mocks-1-10|single|hybrid|2|You have been working on a project for the past eight months. Your project team have been approached by multiple stakeholders for…|
|`pmp-pr9-232`|pmp-mocks-1-10|single|predictive|2|You are managing a large construction project. Due to some delays, the project schedule has been pushed back by one week. As a…|
|`pmp-pr9-233`|pmp-mocks-1-10|single|agile|1|You have planned and begun the current sprint, where the project team have committed to delivering and releasing a particular…|
|`pmp-pr9-234`|pmp-mocks-1-10|single|agile|2|You are a program manager on a large scale agile program, where each project team has its own scrum master. You need to know the…|
|`pmp-pr9-235`|pmp-mocks-1-10|single|predictive|1|Your project has been executing for the past five months, and due to an organisational change a new project sponsor has come on…|
|`pmp-pr9-236`|pmp-mocks-1-10|single|agile|1|You are managing a project as an Agile project manager and a business representative for the product you are delivering asks you…|
|`pmp-pr9-237`|pmp-mocks-1-10|single|agile|2|You are the project manager on an Agile project that is half way through. The project sponsor is starting to raise concerns that…|
|`pmp-pr9-238`|pmp-mocks-1-10|single|hybrid|2|You are working on a traditional waterfall project and the organisation wants to move to an Agile way of work. The functional…|
|`pmp-pr9-239`|pmp-mocks-1-10|single|predictive|2|The project customer approaches you as they are worried that the project is not delivering fast enough. You review the project…|
|`pmp-pr9-240`|pmp-mocks-1-10|single|agile|1|You are showing the project sponsor around the team area for your Agile project and see the burndown chart next to the team…|
|`pmp-pr9-241`|pmp-mocks-1-10|single|agile|1|You are working through the feature backlog with the functional manager of the organisation, who is the product owner for your…|
|`pmp-pr9-242`|pmp-mocks-1-10|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values. Identify…|
|`pmp-pr9-243`|pmp-mocks-1-10|single|agile|1|You are working on a complex software project but see that the team uses a whiteboard, and writes tasks on cards or sticky notes.…|
|`pmp-pr9-244`|pmp-mocks-1-10|single|predictive|2|You have just initiated a project, and the steering committee asks you to keep them updated on the main project activities. You…|
|`pmp-pr9-245`|pmp-mocks-1-10|single|agile|2|You are working on a project where a competitor is also working on a similar feature, and being first to the market will have a…|

### process-10 · Closure — 77 items

*Confirm completion, hand over cleanly, and close the phase or project out.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr10-001`|core|multi|predictive|3|Your project has delivered a warehouse management system that has been live for three weeks and is working. The sponsor wants the…|
|`pmp-pr10-002`|core|single|agile|3|An agile team has delivered the features that account for the bulk of the expected value. The remaining backlog is 90 items of…|
|`pmp-pr10-003`|core|single|predictive|2|Your system has passed acceptance testing and gone live. The operations team that will run it has raised no objection but has not…|
|`pmp-pr10-004`|core|single|hybrid|4|Your programme has been cancelled after fourteen months because the parent company was acquired and the acquirer already owns an…|
|`pmp-pr10-005`|core|hotspot|predictive|2|Your organisation closes out delivered projects using the sequence shown in the diagram. Several recent projects have stalled in…|
|`pmp-pr10-006`|core|single|hybrid|5|Your platform build is nine months into eighteen. The client that accounted for roughly seventy per cent of the forecast benefit…|
|`pmp-pr10-007`|core|multi|predictive|2|Your project's design phase is complete and the build phase begins next month. The organisation requires a phase closure before…|
|`pmp-pr10-008`|core|single|predictive|3|Your customer has been using the delivered system in production for six weeks and is satisfied with it. Nineteen low-severity…|
|`pmp-pr10-009`|core|pulldown|hybrid|3|Prompts: A risk the programme identified will not… / Two of eleven benefits in the business c… / A supplier contract has been fulfilled b…|
|`pmp-pr10-010`|core|multi|predictive|2|Your eighteen-month programme completes next month. Twenty-six people return to their functional departments or to other…|
|`pmp-pr10-011`|core|single|hybrid|3|Your customer formally accepted the delivered platform seven weeks ago. Since then your team has completed nineteen small changes…|
|`pmp-pr10-012`|core|single|predictive|4|Your project has met every acceptance criterion and the test evidence is complete and agreed with the customer's technical team.…|
|`pmp-pr10-013`|core|multi|predictive|3|The construction package on your project is physically complete and the client has occupied the building. The contractor has…|
|`pmp-pr10-014`|core|single|hybrid|2|A six-month pilot of a new scheduling tool in two of your organisation's forty depots ends next week. The pilot met its technical…|
|`pmp-case1-008`|case-01|single|predictive|3|Filter bank 3 is due to be handed over to operations after its commissioning tests next month. None of the operators has yet been…|
|`pmp-case4-008`|case-04|multi|predictive|3|The gallery is nearing completion. Select two.|
|`pmp-pr10-101`|process-10|single|predictive|1|The extension to a secondary school was handed over last Friday and the snagging list is closed. Two site cabins, a 100 kVA…|
|`pmp-pr10-102`|process-10|single|agile|3|An online fashion retailer's checkout team is holding its final sprint review before the team is disbanded and the product moves…|
|`pmp-pr10-103`|process-10|single|hybrid|4|Your hybrid project is installing a crane-scheduling system at a container port and closes at the end of the month. One risk…|
|`pmp-pr10-104`|process-10|single|predictive|4|A county council's library digitisation project has been completed and accepted with £48,000 of its contingency reserve unspent.…|
|`pmp-pr10-105`|process-10|single|agile|2|A payments team has finished its last sprint and is being released next Friday. The only engineer who has ever rotated the…|
|`pmp-pr10-106`|process-10|single|predictive|3|An insurer's new claims-handling system went live four weeks ago and project closure is due next week. The business case promised…|
|`pmp-pr10-107`|process-10|single|hybrid|4|A water utility's new billing platform has been in hypercare for six weeks. Four of the five agreed exit criteria are met. The…|
|`pmp-pr10-108`|process-10|single|predictive|2|Your rail signalling upgrade has been accepted, and finance plans to close the project's cost codes at month end, in ten days.…|
|`pmp-pr10-109`|process-10|single|agile|3|A charity's donations platform team has been told its funding ends after two more sprints rather than six, because the trustees…|
|`pmp-pr10-110`|process-10|single|agile|5|An airline's loyalty app has been built by a supplier's agile team under a time-and-materials contract that ends in three weeks.…|
|`pmp-pr10-111`|process-10|single|predictive|3|Your fibre rollout across three market towns is closing. Its largest delay, six weeks, came from a dispute over who was…|
|`pmp-pr10-112`|process-10|single|hybrid|2|A pharmaceutical packaging line has been delivered by a hybrid project: predictive procurement and installation of the equipment,…|
|`pmp-pr10-113`|process-10|multi|predictive|3|You managed this year's city marathon and are closing the project. Your delegated authority lets you settle individual supplier…|
|`pmp-pr10-114`|process-10|multi|agile|3|A university research consortium's agile team has finished building a data platform for a three-year sleep study. The…|
|`pmp-pr10-115`|process-10|multi|hybrid|3|Your hybrid project is rolling out a manufacturing execution system at a bottling plant. The final configuration audit before…|
|`pmp-pr10-116`|process-10|matching|predictive|3|Prompts: An approved change added a second data-c… / The data-extraction supplier missed its… / Data-mapping effort came in at three tim…|
|`pmp-pr10-117`|process-10|matching|agile|2|Prompts: Forty backlog items that the outgoing pr… / A medium-severity security vulnerability… / The team's reasoning for choosing an off…|
|`pmp-pr10-118`|process-10|pulldown|agile|3|Prompts: A service manager asks for a one-line wo… / The product owner asks for a new payment…|
|`pmp-pr10-119`|process-10|fillblank|predictive|3|A museum gallery fit-out has a final account of £640,000. The contract holds 5% retention, half released at practical completion…|
|`pmp-pr10-120`|process-10|hotspot|hybrid|4|A ferry operator's online booking service was built in sprints and launched through a planned cut-over. The charter defines…|
|`pmp-pr10-121`|process-10|single|predictive|1|A bridge-strengthening project for a county highways authority closes next week. The authority's records policy requires design…|
|`pmp-pr10-122`|process-10|single|predictive|2|A regional airport's baggage-hall upgrade is closing. The original budget was €4.2 million. Two approved change requests, for…|
|`pmp-pr10-123`|process-10|single|agile|5|A building society funded its mortgage-application app as a two-year project, and that funding ends this month. The executive has…|
|`pmp-pr10-124`|process-10|single|hybrid|3|A housing association's tenant portal was delivered through a hybrid project and has met every acceptance criterion. The closure…|
|`pmp-pr10-125`|process-10|single|predictive|4|You are the contractor's project manager on a flood defence scheme for a river authority. The works are complete and the client…|
|`pmp-pr10-126`|process-10|single|hybrid|4|Your hybrid project has refitted 60 stores for a homeware retailer, with the in-store stock app built in sprints, and is ready to…|
|`pmp-pr10-127`|process-10|single|predictive|2|Your engineering firm has finished durability testing on prototype battery packs for a car manufacturer. The contract and its…|
|`pmp-pr10-128`|process-10|single|predictive|2|Your project installing rooftop solar panels at four distribution depots has been accepted by the client, the final supplier…|
|`pmp-pr10-129`|process-10|single|agile|3|An agile team has built a new booking engine for a hotel group. In final acceptance testing, the client found that group bookings…|
|`pmp-pr10-130`|process-10|single|agile|4|An external supplier's agile team has built a self-service portal for an energy retailer under a fixed-budget contract whose…|
|`pmp-pr10-131`|process-10|multi|predictive|4|Your university's sports centre project has dropped its planned swimming pool after a funding review, so the board has terminated…|
|`pmp-pr10-132`|process-10|multi|hybrid|3|A logistics firm's new transport management system was accepted three months ago, and the hybrid project's scope includes…|
|`pmp-pr10-133`|process-10|multi|agile|3|Over a year, an agile team has built 22 operational dashboards for a supermarket chain's supply-chain analysts. The team disbands…|
|`pmp-pr10-134`|process-10|matching|predictive|3|Prompts: A print works has signed acceptance and… / A dairy has neither accepted nor rejecte… / A brewery has rejected the works in writ…|
|`pmp-pr10-135`|process-10|matching|hybrid|3|Prompts: A purchase order for lighting-technician… / Two developers released to another proje… / The rigging supplier issued a £3,100 cre…|
|`pmp-pr10-136`|process-10|pulldown|agile|2|Prompts: Three contract developers finish on Frid… / The deployment pipeline uses a shared se…|
|`pmp-pr10-137`|process-10|fillblank|agile|3|An agile supplier is paid €1,150 per accepted story point, up to a contract ceiling of €460,000. Before the final sprint, 371…|
|`pmp-pr10-138`|process-10|hotspot|predictive|3|A coastal path restoration project's closure board lists six items. The organisation's policy allows closure once every item is…|
|`pmp-pr10-139`|process-10|single|agile|3|An agile team has built an eSIM activation service for a mobile network operator and is due to be reassigned in three weeks. The…|
|`pmp-pr10-140`|process-10|single|hybrid|3|Your hybrid project has installed 1,200 air-quality sensors across a city, with the public data dashboard built in sprints. The…|
|`pmp-pr10-1`|pmp-lectures-1-11|single|predictive|3|A project was undertaken to set up a new business function in the company. Several months after completion, the project manager…|
|`pmp-pr10-2`|pmp-lectures-1-11|single|predictive|2|A junior project manager approaches a senior project manager and asks for assistance in initiating the final closeout process for…|
|`pmp-pr10-3`|pmp-lectures-1-11|single|predictive|3|A project manager works at a construction company. The current project is a three year, multimillion-dollar initiative to rebuild…|
|`pmp-pr10-201`|pmp-mocks-1-10|single|hybrid|3|A project is delivering incremental business value to a customer. The success criteria for the project is a 20% increase in…|
|`pmp-pr10-202`|pmp-mocks-1-10|single|predictive|3|A project team has completed project deliverables, but the project manager would like to evaluate whether the project has…|
|`pmp-pr10-203`|pmp-mocks-1-10|single|predictive|2|During a project governance assessment, and after expending 40% of the approved project budget, management approved the auditor's…|
|`pmp-pr10-204`|pmp-mocks-1-10|single|predictive|2|A project to develop a new product is nearing its closing phase after 6 months of contributions from the project team and vendor.…|
|`pmp-pr10-205`|pmp-mocks-1-10|single|hybrid|2|A project manager is involved in an e-commerce development project. The project has been completed successfully using the hybrid…|
|`pmp-pr10-206`|pmp-mocks-1-10|single|predictive|2|A project manager needs to close a project phase.|
|`pmp-pr10-207`|pmp-mocks-1-10|single|predictive|2|A project was cancelled during its initiation phase due to lack of adequate resources.|
|`pmp-pr10-208`|pmp-mocks-1-10|single|predictive|3|Due to a recent incident, a dissatisfied stakeholder files a complaint about a project that has been closed.|
|`pmp-pr10-209`|pmp-mocks-1-10|single|predictive|2|During project closure, the project manager finds it difficult to obtain successful project completion feedback. Some…|
|`pmp-pr10-210`|pmp-mocks-1-10|single|predictive|2|A group of stakeholders at a financial institution are conducting a project closure review. Some of the stakeholders are…|
|`pmp-pr10-211`|pmp-mocks-1-10|single|predictive|2|A large project involving functional groups in four countries is nearing completion. The project manager is meeting with the…|
|`pmp-pr10-212`|pmp-mocks-1-10|single|predictive|2|After all deliverables have been completed, a project manager needs to formally close a project.|
|`pmp-pr10-213`|pmp-mocks-1-10|single|predictive|3|A project manager is concerned that team members spend too much time assisting operations and maintenance staff with a recently…|
|`pmp-pr10-214`|pmp-mocks-1-10|single|predictive|1|Your team have completed the work on your project and the customer has confirmed the formal acceptance of the product that has…|
|`pmp-pr10-215`|pmp-mocks-1-10|single|predictive|2|You are nearing the end of a difficult project where the vendor providing the system to your company has taken longer than…|
|`pmp-pr10-216`|pmp-mocks-1-10|single|predictive|2|During your project, your company was taken over by another, larger company, and the project management office and its policies…|
|`pmp-pr10-217`|pmp-mocks-1-10|single|predictive|1|You are in the closing phase of your project, after having delivered a large change for employees in the HR department.|
|`pmp-pr10-218`|pmp-mocks-1-10|single|predictive|1|You as a project manager are listing the things that must be done to close out the project.|
