# Question ledger — process-1 · Integrated planning

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
