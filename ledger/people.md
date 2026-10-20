# Question ledger — People

1006 questions across the 8 People tasks.

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

### people-1 · Shared purpose — 93 items

*Build a common picture of what the project is for, promote it, and keep it current.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p1-001`|core|single|predictive|3|Six weeks into a plant automation project, the operations director tells you the point of the work is to cut headcount, the plant…|
|`pmp-p1-002`|core|single|agile|3|Four sprints in, an agile team is delivering steadily but the product owner notices the team is building whatever sits at the top…|
|`pmp-p1-003`|core|single|hybrid|4|A hybrid programme delivers a customer portal through two vendors: one runs iteratively on the front end, the other delivers…|
|`pmp-p1-004`|core|single|predictive|3|You take over a stalled warehouse relocation eleven months in. The original sponsor has left, the objectives in the charter refer…|
|`pmp-p1-005`|core|single|agile|2|A product team pivots from serving small independent retailers to serving two large grocery chains. The product owner announces…|
|`pmp-p1-006`|core|single|predictive|3|Your sponsor leaves the organisation nine months into an eighteen-month logistics programme. The replacement arrives from a…|
|`pmp-p1-007`|core|multi|hybrid|4|A reorganisation splits your programme's delivery teams across three new departments. Nothing about the scope changes, but within…|
|`pmp-p1-008`|core|single|agile|3|A second team joins your product six months in. The original team can recite the product vision word for word, but it was written…|
|`pmp-p1-009`|core|multi|predictive|2|You take over a fourteen-month-old programme from a departing project manager. The charter states an objective of reducing…|
|`pmp-p1-010`|core|single|hybrid|4|Your organisation announces a strategy shift away from the market your project was built to enter. Your project is eight months…|
|`pmp-p1-011`|core|single|predictive|3|A government-funded programme exists to shorten the time claimants wait for a decision on their benefit. Reporting to the…|
|`pmp-p1-012`|core|multi|hybrid|4|You are launching the third attempt in five years to replace a claims platform. The two previous attempts were cancelled after…|
|`pmp-p1-013`|core|single|predictive|3|Two insurers have merged and you are running the integration of their claims operations. Each company's project office has issued…|
|`pmp-p1-014`|core|multi|hybrid|4|A research consortium funds your programme through four partners. Each partner's grant agreement names a different primary…|
|`pmp-case3-008`|case-03|single|hybrid|3|The vendor's configuration team and the internal integration team now blame each other for interface defects found in testing.…|
|`pmp-case5-002`|case-05|single|hybrid|3|Ruth Ellis, who runs the Northway centre, wants success measured by average call handling time. Sam Okafor, who runs the Calder…|
|`pmp-p1-101`|people-1|single|predictive|1|You are preparing the 90-minute kick-off meeting for a project to install 400 pressure sensors across a regional water network.…|
|`pmp-p1-102`|people-1|single|predictive|2|Your firm is building a two-storey extension to a secondary school that stays open throughout the work. The contract and the…|
|`pmp-p1-103`|people-1|single|predictive|3|A €3m project is updating the firmware of an infusion pump. The charter lists two objectives: release before a competitor's…|
|`pmp-p1-104`|people-1|single|predictive|4|Five months into a ten-month, £1.2m project to build a consignment-tracking system for a freight depot, the team is on schedule…|
|`pmp-p1-105`|people-1|single|predictive|3|At a team day on a rail signalling upgrade, you ask the nine engineers to write one sentence saying what the project is for.…|
|`pmp-p1-106`|people-1|single|hybrid|5|A housing association's sponsor has issued a vision for your hybrid repairs-service project: 'Every tenant's repair fixed right…|
|`pmp-p1-107`|people-1|single|agile|2|The product owner of a payments team writes each sprint goal as a list of the stories planned, such as 'Deliver stories 41, 44,…|
|`pmp-p1-108`|people-1|single|agile|3|A team building a language-learning app has raised its velocity from 28 to 41 points over six sprints, and sprint reviews have…|
|`pmp-p1-109`|people-1|single|agile|3|Two quarters ago a council's web team wrote a product vision promising 'accessible online services for every resident.' Since…|
|`pmp-p1-110`|people-1|single|agile|4|Your start-up's product vision is a self-service route-planning tool for small courier firms. A large retailer has offered…|
|`pmp-p1-111`|people-1|single|hybrid|2|You lead a hybrid project to digitise patient records at a hospital trust. The agreed purpose is to give clinicians back time…|
|`pmp-p1-112`|people-1|single|agile|3|A charity's product team is building a monthly-giving feature. The product goal for this quarter is to learn whether 500 pilot…|
|`pmp-p1-113`|people-1|multi|predictive|3|Your project is moving a utility company's customer service desk to an outsourced provider. The project's purpose is to cut…|
|`pmp-p1-114`|people-1|multi|agile|4|You are the agile project manager for a cross-functional team building a fraud-detection product at a bank. Its six members…|
|`pmp-p1-115`|people-1|multi|hybrid|5|A community health charity is running a hybrid project to launch a mobile screening clinic, staffed by 6 paid employees and 40…|
|`pmp-p1-116`|people-1|matching|predictive|3|Prompts: A supplier's engineer asks why one fixtu… / After a fall in orders, the steering gro… / A newly appointed team lead asks which m…|
|`pmp-p1-117`|people-1|matching|hybrid|3|Prompts: Team members can quote the vision accura… / Team members give three different accoun… / The team's account of the purpose matche…|
|`pmp-p1-118`|people-1|pulldown|hybrid|3|Prompts: The sponsor confirms the aim of cutting… / After a council strategy review, the spo…|
|`pmp-p1-119`|people-1|fillblank|predictive|2|A planning department's project exists to cut the average time to decide a householder application from 18 weeks to 12 weeks. You…|
|`pmp-p1-120`|people-1|hotspot|agile|3|An agile team building a GP surgery booking product keeps arguing in refinement: half design for receptionists managing the…|
|`pmp-p1-121`|people-1|single|predictive|1|You are managing a nine-month project to upgrade a building society's transaction-monitoring system so that it flags suspected…|
|`pmp-p1-122`|people-1|single|predictive|2|A city museum's €4.5m gallery refurbishment has a charter whose purpose reads 'transform the visitor experience'. At the first…|
|`pmp-p1-123`|people-1|single|predictive|5|Your sponsor has commissioned a project to separate a manufacturing division's finance, HR and IT systems from the parent…|
|`pmp-p1-124`|people-1|single|predictive|4|Your project is automating dispensing at a mail-order pharmacy, and its stated purpose is to eliminate dispensing errors. At the…|
|`pmp-p1-125`|people-1|single|predictive|3|A flood-defence scheme was approved to protect 1,200 homes along a river. The funding body has now cut the budget by 25%, and the…|
|`pmp-p1-126`|people-1|single|predictive|3|Your team is designing a bypass for a market town, approved by the county council after public consultation to cut journey times…|
|`pmp-p1-127`|people-1|multi|predictive|3|In year three of a four-year project to build a civic centre designed for net-zero running over a 60-year life, most of the…|
|`pmp-p1-128`|people-1|pulldown|predictive|2|Prompts: For the finance committee deciding wheth… / For the reception staff whose daily work… / For residents hearing about the change t…|
|`pmp-p1-129`|people-1|single|agile|3|A team building a ferry-booking app has a product vision: 'Make booking a crossing as easy as buying a train ticket.' A respected…|
|`pmp-p1-130`|people-1|multi|agile|4|Your team's product vision is a simple payroll service for businesses with fewer than 20 staff. In the last quarter the sales…|
|`pmp-p1-131`|people-1|single|agile|4|Your team is building an app for smallholder farmers. Its vision, used to raise investment, is to give farmers live crop prices.…|
|`pmp-p1-132`|people-1|single|agile|2|A university's agile team is building a student wellbeing app. The product owner is in meetings most of the day, and the team now…|
|`pmp-p1-133`|people-1|single|agile|2|You support an internal platform team that provides deployment tools to 12 product teams at an online retailer. The platform team…|
|`pmp-p1-134`|people-1|fillblank|agile|3|An agile team is building route-planning software for a council's refuse-collection fleet. The product exists to save £468,000 a…|
|`pmp-p1-135`|people-1|matching|agile|3|Prompts: Before the first sprint, members from ea… / The team repeatedly argues over whether… / The team has never met the depot staff w…|
|`pmp-p1-136`|people-1|single|hybrid|4|A hybrid road-safety project, combining iterative app development with phased junction works, exists to cut serious injuries on a…|
|`pmp-p1-137`|people-1|hotspot|hybrid|3|A hybrid project is delivering a discharge app for a hospital trust, whose purpose is to cut readmissions caused by medication…|
|`pmp-p1-138`|people-1|matching|hybrid|3|Prompts: A team lead asks whether the year-one ta… / Developers on the agile app team ask whi… / Installers on the predictive rollout tea…|
|`pmp-p1-139`|people-1|multi|hybrid|3|Four developers have joined your hybrid project, which is replacing a city's parking-permit service, after their previous product…|
|`pmp-p1-140`|people-1|single|hybrid|3|You are running a hybrid project to introduce a common returns process across a retailer's stores in France, Germany and Poland.…|
|`pmp-p1-201`|export-batch-2|single|predictive|3|An organization recently updated its strategic plan and some of the changes were substantial. During a meeting for an ongoing…|
|`pmp-p1-202`|export-batch-2|multi|predictive|4|A project manager is developing a project management plan. The project duration is set for three months due to regulatory…|
|`pmp-p1-203`|export-batch-2|single|agile|3|A project manager just started managing agile projects. The project manager realizes that due to the lack of a clear definition…|
|`pmp-p1-204`|export-batch-2|single|agile|3|At the beginning of an agile project, the project manager is developing the project charter with the team. They are unable to…|
|`pmp-p1-205`|export-batch-2|single|predictive|3|The headquarters of a multinational company is in Europe, with many regional offices in other countries around the world. The…|
|`pmp-p1-206`|export-batch-2|single|predictive|3|A project manager is assigned to a legacy system modernization project. The project management plan is solidified and the…|
|`pmp-p1-207`|export-batch-2|single|predictive|1|A project manager has started developing a plan for a new project. During the first planning meeting, the stakeholders ask about…|
|`pmp-p1-208`|export-batch-2|single|predictive|2|A project team meeting is focused on examining if the business value and vision for a project are being delivered by all parties.…|
|`pmp-p1-209`|export-batch-2|single|predictive|1|Following project approval, a project team is assigned and the project management plan is created.|
|`pmp-p1-210`|export-batch-2|single|predictive|2|A project manager leaves an ongoing project and is replaced by a new project manager.|
|`pmp-p1-211`|export-batch-2|single|predictive|2|An experienced project manager is preparing a kick-off meeting for a new service assessment. A high-level description of the…|
|`pmp-p1-212`|export-batch-2|single|predictive|3|During a project's initiation phase, the project manager reviews the business case and finds that information is incomplete.|
|`pmp-p1-213`|export-batch-2|single|predictive|2|A research and development department is planning to develop a product that will introduce a new line of business for the…|
|`pmp-p1-214`|export-batch-2|single|hybrid|3|A team for a project is composed of individuals from different backgrounds, ages, and interests. The team is struggling to work…|
|`pmp-p1-215`|export-batch-2|single|agile|2|An agile team finds it difficult to complete a checklist because they have different interpretations of what the project aims to…|
|`pmp-p1-216`|export-batch-2|single|predictive|2|A project manager has been assigned to a new project. During the initial review of the project charter, the project manager…|
|`pmp-p1-217`|export-batch-2|single|predictive|2|A project sponsor assigns a project manager to a new, critical project. This project has tight budget and time constraints.|
|`pmp-p1-218`|export-batch-2|single|agile|3|An organization has implemented agile for its recent projects, but all of the recent agile projects have missed their delivery…|
|`pmp-p1-219`|export-batch-2|single|predictive|2|A functional manager questions whether certain aspects of a project are in compliance with the project's goals.|
|`pmp-p1-220`|export-batch-2|single|agile|3|An organization just implemented agile and forms a development team where members are not co-located. The organization’s…|
|`pmp-p1-221`|export-batch-2|single|agile|2|A new product is being constructed by an agile team, but the team has a limited amount of time to complete it. Due to these…|
|`pmp-p1-222`|export-batch-2|single|predictive|1|A project manager is in the initiation phase of a new project and is preparing to develop a project team charter. The project…|
|`pmp-p1-223`|export-batch-2|single|agile|1|In order to function optimally, a project team should have a project charter that states the vision or purpose of the project and…|
|`pmp-p1-224`|export-batch-2|single|predictive|2|The project manager needs to survey the necessary parties to reach a consensus and build a common vision for the project.|
|`pmp-p1-225`|export-batch-2|single|predictive|2|A project manager is working on an internal project and notes that the visions and goals of other department leaders are not…|
|`pmp-p1-226`|export-batch-2|single|predictive|2|Throughout a project, it becomes apparent that team members from different departments have differing perspectives on what the…|
|`pmp-p1-227`|export-batch-2|single|hybrid|2|As the project manager of a new healthcare software implementation project, you're leading a team that's decided to embrace a…|
|`pmp-p1-228`|export-batch-2|single|hybrid|2|You have been allocated to a new project team with members from different parts of the functional business area. It soon becomes…|
|`pmp-p1-229`|export-batch-2|single|agile|2|As the project manager for a software development project, you're informed that due to budget reallocations, several developers…|
|`pmp-p1-230`|export-batch-2|single|predictive|2|You have been assigned to a new project, where the work is behind schedule and the team are not working together well. The team…|
|`pmp-p1-231`|export-batch-2|single|agile|2|Your project team are protected by a union agreement. You notice that they don't know how to use the product you are improving,…|
|`pmp-p1-232`|export-batch-2|single|hybrid|2|You have formed a new project team and have worked through the purpose and mission of the project with them. You expanded on who…|
|`pmp-p1-233`|export-batch-2|single|agile|2|You are working in a new agile team, where the work has started to slow and there is uncertainty and low team engagement. You set…|
|`pmp-p1-234`|export-batch-2|single|agile|1|Your project is new and the team have just come on board. There have been multiple disagreements and the work is slow as they try…|
|`pmp-p1-235`|export-batch-2|single|agile|2|You are an Agile project manager leading a new software project. During the last retrospective there was very little engagement…|
|`pmp-p1-236`|export-batch-2|single|agile|1|You have brought together a new project team to deliver a system that has never been done before in your organization. The…|
|`pmp-p1-237`|export-batch-2|single|agile|1|You are working on an Agile project and have been asked to create a document that lists team principles, agreements, and…|

### people-2 · Conflict — 116 items

*Trace disagreement to its source and work it through to an agreed resolution.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p2-001`|core|matching|agile|3|Prompts: A production outage is live, two enginee… / Two team members disagree about a coding… / A designer and a developer disagree abou…|
|`pmp-p2-002`|core|single|predictive|3|In a matrix organisation, a functional manager has twice reassigned your lead analyst mid-sprint without telling you, and the…|
|`pmp-p2-003`|core|single|hybrid|4|Two technical leads on your programme have been arguing for a month about whether to build a shared service or let each stream…|
|`pmp-p2-004`|core|single|predictive|4|Two department heads both need the same test rig in the final fortnight before your go-live. Each has emailed you separately…|
|`pmp-p2-005`|core|single|agile|2|Two developers on your team have started leaving blunt comments on each other's pull requests. Nothing is abusive, but the tone…|
|`pmp-p2-006`|core|single|predictive|5|Your client's project manager insists that a data migration capability is within the fixed-price scope; you read the…|
|`pmp-p2-007`|core|matching|hybrid|3|Prompts: Two teams each believe the other is resp… / A business analyst and a developer disag… / Two senior engineers who have worked tog…|
|`pmp-p2-008`|core|single|predictive|3|A senior analyst allocated to your project half-time keeps missing your deadlines. Her functional manager tells you she is fully…|
|`pmp-p2-009`|core|pulldown|agile|3|Prompts: Two developers disagree about a library… / A tester has twice made remarks about a… / Two team members disagree about who shou…|
|`pmp-p2-010`|core|single|hybrid|4|Two workstream leads on your distributed programme have been in unresolved disagreement for months about interface ownership. In…|
|`pmp-p2-011`|core|single|agile|2|A developer tells you that a business analyst on the team is impossible to work with, changes her mind constantly, and is making…|
|`pmp-p2-012`|core|single|predictive|4|Your integration vendor and your internal infrastructure team each insist the other is responsible for a firewall configuration…|
|`pmp-p2-013`|core|single|agile|2|In yesterday's retrospective, two developers disagreed sharply about whether the team should keep pairing on complex work. The…|
|`pmp-p2-014`|core|single|predictive|4|Six weeks before a regulatory milestone, a senior engineer submits a formal grievance to human resources about a colleague on…|
|`pmp-p2-061`|core|graphic|hybrid|3|Your two most senior engineers disagree about how the interface between the billing and scheduling platforms should be…|
|`pmp-case2-003`|case-02|single|agile|3|Ewan and Lior, the two senior developers, disagree sharply about code review standards, and the tension has started to show in…|
|`pmp-case4-006`|case-04|single|predictive|3|The curators say the contractor's staff ignore the dust and vibration rules on site. The contractor's site manager says the…|
|`pmp-p2-101`|people-2|single|predictive|3|On a hospital extension, the main contractor's site manager and the mechanical and electrical subcontractor's foreman have argued…|
|`pmp-p2-102`|people-2|single|agile|1|During the daily stand-up of a mobile banking team, two developers start debating whether to adopt a new state-management…|
|`pmp-p2-103`|people-2|single|hybrid|5|Your firm is delivering a telemetry upgrade for a water company. The client's records manager, who owns its document standards,…|
|`pmp-p2-104`|people-2|single|predictive|4|On a line-automation project at a food plant near Lyon, the production planning lead and the quality lead clash at every weekly…|
|`pmp-p2-105`|people-2|single|agile|3|Your payments team is split between Leeds and Pune. Pune developers have started posting terse comments and missing refinement…|
|`pmp-p2-106`|people-2|single|hybrid|3|On a council housing-repairs system rollout, the test lead and the business analyst are in a running dispute about whether failed…|
|`pmp-p2-107`|people-2|single|predictive|3|On a claims-reserving project for a motor insurer, two actuarial analysts present reserve forecasts for the same portfolio that…|
|`pmp-p2-108`|people-2|single|predictive|2|On a substation replacement project for an electricity network operator, an email thread between the asset manager and your…|
|`pmp-p2-109`|people-2|multi|agile|3|On a retail e-commerce team, retrospectives have been short and positive for four sprints. Yet you have heard in corridors that…|
|`pmp-p2-110`|people-2|multi|agile|3|A public-sector digital team has been formed by merging four in-house developers with four from a contracted supplier. In the…|
|`pmp-p2-111`|people-2|matching|predictive|2|Prompts: The airport's operations lead and your d… / The conveyor supplier and your test engi… / The installation supervisor says the nig…|
|`pmp-p2-112`|people-2|matching|hybrid|3|Prompts: Two software teams disagree about which… / A developer and a tester on one software… / The mechanical contractor's supervisor a…|
|`pmp-p2-113`|people-2|pulldown|agile|3|Prompts: The team keeps reopening its decision on… / In refinement, two senior developers arg… / The team has argued about a level-loadin…|
|`pmp-p2-114`|people-2|fillblank|predictive|4|On a museum fit-out in Rotterdam, the joinery subcontractor claims €90,000 for work it says was added after award, and has…|
|`pmp-p2-115`|people-2|hotspot|hybrid|2|On a university research-data platform, the data architect and the data engineering lead have clashed three times this month,…|
|`pmp-p2-116`|people-2|single|predictive|4|On a packaging-line upgrade for a pharmaceutical manufacturer, your validation lead and the client's engineering lead disagreed…|
|`pmp-p2-117`|people-2|single|agile|3|On a charity's online donations platform, the head of fundraising and the digital accessibility lead have each been approaching…|
|`pmp-p2-118`|people-2|single|agile|2|During a sprint planning call for a travel-booking team, a tester asks how the new fare-caching service will handle currency…|
|`pmp-p2-119`|people-2|single|hybrid|5|Your hybrid programme is delivering connected e-bike docks for a city. The hardware lead wants the next firmware drop moved from…|
|`pmp-p2-120`|people-2|multi|predictive|3|On an ERP rollout for a logistics firm, the project charter sets a conflict escalation path: parties try to resolve a dispute…|
|`pmp-p2-121`|people-2|single|predictive|1|On a train maintenance depot upgrade, you are showing a delegation from the rail operator's board around the site. From the site…|
|`pmp-p2-122`|people-2|single|agile|2|A fully remote data-platform team at an online grocer has started arguing about cameras in meetings. Three developers keep their…|
|`pmp-p2-123`|people-2|single|predictive|3|On a flood defence scheme for a county council, the hydraulic modelling lead and the environmental assessment lead are openly…|
|`pmp-p2-124`|people-2|single|hybrid|4|On a hybrid programme building firmware for an infusion pump, your most experienced firmware engineer asks to see you and says:…|
|`pmp-p2-125`|people-2|single|predictive|3|Your company is implementing a purchasing system for a furniture retailer. In a requirements workshop, the retailer's finance…|
|`pmp-p2-126`|people-2|single|agile|3|On a parcel-tracking app team, a developer tells you in a one-to-one that she has picked up almost all the production support…|
|`pmp-p2-127`|people-2|single|predictive|5|On a regional museum's gallery refit, the lead curator and the exhibition designer have disliked each other for a year. You have…|
|`pmp-p2-128`|people-2|single|hybrid|3|On a housing association's tenant portal programme, a junior analyst tells you privately that the data lead has repeatedly mocked…|
|`pmp-p2-129`|people-2|single|agile|3|A developer who joined your energy-billing team two months ago keeps proposing contract testing between services. Each time, two…|
|`pmp-p2-130`|people-2|single|predictive|2|You manage a pension administration system replacement and prepare the monthly steering group pack. At this month's meeting, the…|
|`pmp-p2-131`|people-2|single|predictive|4|On a new secondary school building, the structural engineer and the architect have argued at the last three design meetings about…|
|`pmp-p2-132`|people-2|single|hybrid|4|You are leading a hybrid warehouse-system rollout for a Belgian distributor. The software supplier's account manager and your…|
|`pmp-p2-133`|people-2|multi|predictive|3|On a container terminal automation project, the crane-controls lead and the yard-systems lead agreed in a facilitated meeting two…|
|`pmp-p2-134`|people-2|multi|agile|3|Two developers on your video-streaming team were close friends until a falling-out outside work last month. Since then they have…|
|`pmp-p2-135`|people-2|multi|hybrid|4|Your supplier team is building a grants management system for a government department using Scrum, inside the department's…|
|`pmp-p2-136`|people-2|matching|agile|2|Prompts: Head of marketing: 'The virtual queue mu… / Lead developer: 'We are not shipping the… / Customer service manager: 'No release at…|
|`pmp-p2-137`|people-2|matching|predictive|3|Prompts: Two installation team leaders are giving… / In a weekly review attended by the clien… / Two trainers disagree about whether to t…|
|`pmp-p2-138`|people-2|pulldown|agile|2|Prompts: Two developers disagree about the loggin… / Team members now refer to 'the mobile ca… / One developer refuses to attend any meet…|
|`pmp-p2-139`|people-2|fillblank|hybrid|3|Two leads on a veterinary diagnostics firm's hybrid programme disagree about which laboratory information system to buy. Before…|
|`pmp-p2-140`|people-2|hotspot|agile|3|On a fintech app team, a tester asks you, as Scrum Master, to look over a message she plans to send a developer who keeps merging…|
|`pmp-p2-1`|export-batch-1|single|agile|3|During a retrospective meeting, some team members expressed that they strongly disagree with how the product owner and scrum…|
|`pmp-p2-2`|export-batch-1|single|agile|3|A new project manager is assigned to an ongoing agile project. The project is now starting its second 4-week iteration cycle. The…|
|`pmp-p2-3`|export-batch-1|single|agile|3|During a software development project, the team realized that several features failed to comply with quality requirements. After…|
|`pmp-p2-4`|export-batch-1|single|predictive|4|A client's interactions with the project team have been somewhat negative. Project team members don't want to be part of the…|
|`pmp-p2-5`|export-batch-1|single|agile|3|A team has recently adopted agile and is following a scrum model. There is a conflict between a lead developer and a quality…|
|`pmp-p2-6`|export-batch-1|single|predictive|4|A project team member suddenly approaches the project manager and asks to resign from the project. The team member is upset…|
|`pmp-p2-7`|export-batch-1|single|predictive|3|A project manager is leading a complex software development project. The project team cannot agree on which process to use when…|
|`pmp-p2-8`|export-batch-1|single|predictive|4|A key member of a project team publicly declares that another team member has been speaking negatively about them. The team…|
|`pmp-p2-9`|export-batch-1|single|predictive|3|During a multidisciplinary project, a project manager learns that there are many conflicts about design among team members from…|
|`pmp-p2-201`|export-batch-2|single|agile|3|During a standup, a team member falsely reports the completion of a story which is a dependency for other stories. When the team…|
|`pmp-p2-202`|export-batch-2|single|agile|2|During a standup meeting, some of the team members strongly disagreed about how tasks were distributed. They feel tired and…|
|`pmp-p2-203`|export-batch-2|single|predictive|2|Discussions during weekly project team meetings are frequently dominated by a senior team member who has a tendency to talk over…|
|`pmp-p2-204`|export-batch-2|single|predictive|3|A delivery team member is complaining about the workload and the number of top priorities they are handling. Another team member…|
|`pmp-p2-205`|export-batch-2|single|hybrid|3|A project manager is leading a hybrid project with a self-organizing team. The team is unable to agree on the scope for the next…|
|`pmp-p2-206`|export-batch-2|single|predictive|2|A senior team member is having challenges with a new project team member. The senior team member asks the project manager for…|
|`pmp-p2-207`|export-batch-2|single|agile|3|Company A is moving from a predictive approach to an agile one. After the second incremental release of project X, the product…|
|`pmp-p2-208`|export-batch-2|single|hybrid|3|A project is underway with team members from two different countries and there are complaints from both sides. The team members…|
|`pmp-p2-209`|export-batch-2|single|predictive|3|A project manager is working on a new project. Two key project resources have a disagreement regarding a requirement. The project…|
|`pmp-p2-210`|export-batch-2|single|hybrid|2|A virtual project team is composed of members from various countries with cultural differences. The project manager is concerned…|
|`pmp-p2-211`|export-batch-2|single|agile|3|A project leader and a product owner had a side conversation about the status of the next delivery. The product owner raised…|
|`pmp-p2-212`|export-batch-2|single|predictive|2|A delivery milestone for a project is fast approaching, but an item that is planned to be completed is at risk. A team member…|
|`pmp-p2-213`|export-batch-2|single|predictive|2|A project manager complains that three engineers from different cultural backgrounds leave project meetings twice a day and…|
|`pmp-p2-214`|export-batch-2|single|hybrid|2|A project manager identifies conflicts among local and virtual team members' understanding of the deliverables.|
|`pmp-p2-215`|export-batch-2|single|predictive|2|During a meeting with project stakeholders, two stakeholders have a disagreement regarding how a software component is being…|
|`pmp-p2-216`|export-batch-2|single|predictive|2|A project manager is working on a complex software project. Two team members must team up and work together on a critical module…|
|`pmp-p2-217`|export-batch-2|single|predictive|2|A team member is hosting an event and invites several colleagues through their work calendars. Another team member, who was not…|
|`pmp-p2-218`|export-batch-2|single|predictive|1|A project manager has noticed apparent interpersonal conflict between some team members during the initiation of a project.|
|`pmp-p2-219`|export-batch-2|single|predictive|2|A project team has an after-hours get-together. One of the team members who was recently hired and new to the team did not…|
|`pmp-p2-220`|export-batch-2|single|predictive|2|A newly-hired project manager is running a project for the first time and is worried about rising tensions. Project team members…|
|`pmp-p2-221`|export-batch-2|single|predictive|2|A project manager is leading a project team composed of members from different departments, including engineering, marketing, and…|
|`pmp-p2-222`|export-batch-2|single|predictive|2|During a project team meeting, the project manager notices a conflict between three key team members who have very different…|
|`pmp-p2-223`|export-batch-2|single|predictive|2|A project manager is assigned to a project with a lot of stakeholders and competing priorities.|
|`pmp-p2-224`|export-batch-2|single|agile|2|A project manager notices a potential conflict between a remote developer and an on-site tester during a sprint; the developer…|
|`pmp-p2-225`|export-batch-2|single|predictive|3|An expert resource avoids assignments that include working with a foreign contractor.|
|`pmp-p2-226`|export-batch-2|single|agile|2|A project manager is working on an IT project. During an iteration review meeting, a couple of key stakeholders enter into a…|
|`pmp-p2-227`|export-batch-2|single|agile|2|The project manager schedules an iteration review and learns that a few key stakeholders do not approve of how a feature has been…|
|`pmp-p2-228`|export-batch-2|single|predictive|2|A project manager working with a culturally diverse team discovers major intra-team conflicts during project execution.|
|`pmp-p2-229`|export-batch-2|single|predictive|2|A project manager is managing multidisciplinary teams. During the last project review meeting, the project manager realized that…|
|`pmp-p2-230`|export-batch-2|single|agile|2|A project team member continues to change a colleague's deliverable during every sprint. The colleague has asked the project…|
|`pmp-p2-231`|export-batch-2|single|predictive|2|A team member is unhappy with their workload and feels they are not being supported enough. The project manager wants to meet…|
|`pmp-p2-232`|export-batch-2|single|agile|2|The conflict about how to proceed has developed between two team members prior to a release. The team needs to work together to…|
|`pmp-p2-233`|export-batch-2|single|hybrid|3|A geographically distributed project team has development issues between the colocated and virtual teams, causing productivity to…|
|`pmp-p2-234`|export-batch-2|single|predictive|2|A project team is small and the team members have known each other for a long time. The team's project is in the execution phase.…|
|`pmp-p2-235`|export-batch-2|single|predictive|2|A team member with sufficient experience and knowledge informs the project manager about a conflict with another team member and…|
|`pmp-p2-236`|export-batch-2|single|predictive|2|The execution of a construction project in a remote area requires team members to live together in corporate dormitories when…|
|`pmp-p2-237`|export-batch-2|single|predictive|3|At the midpoint of a renewable energy project, the project manager is faced with a conflict between the design team and…|
|`pmp-p2-238`|export-batch-2|single|predictive|3|During a project charter review session, a dispute arises between the project sponsor and the product owner, witnessed by the…|
|`pmp-p2-239`|export-batch-2|single|predictive|3|During the project execution phase, conflicts have arisen between two key team members, negatively impacting their work output.…|
|`pmp-p2-240`|export-batch-2|multi|predictive|3|At the outset of a new phase in a complex project, after a successful kick-off meeting at the client's site, a team member…|
|`pmp-p2-241`|export-batch-2|single|predictive|2|A project manager is overseeing the development of a new urban park. During the project, a dispute arises between the landscape…|
|`pmp-p2-242`|export-batch-2|multi|predictive|3|During the construction phase of a new city park, conflicts have emerged between the landscape designers and environmental…|
|`pmp-p2-243`|export-batch-2|single|agile|2|As the project manager for a novel online education platform, you're faced with divergent views among stakeholders regarding the…|
|`pmp-p2-244`|export-batch-2|single|predictive|2|You are working on a high profile project. Several high-ranking senior executives want to take the credit for this initiative,…|
|`pmp-p2-245`|export-batch-2|single|predictive|2|During a key scope meeting, an argument arises. The business manager accuses the technology manager of not delivering to their…|
|`pmp-p2-246`|export-batch-2|single|predictive|3|Your team members have raised an issue as they believe the current project scope will not reach the intended project goal of…|
|`pmp-p2-247`|export-batch-2|single|agile|2|You are an agile project manager, and holding daily stand-ups with your team. You notice people accusing other team members of…|
|`pmp-p2-248`|export-batch-2|single|predictive|1|You are working on a project where the functional manager is unhappy with the changes being brought about to his part of the…|
|`pmp-p2-249`|export-batch-2|single|predictive|2|The project you are working on has recently started and the team has come together from many different parts of the organisation,…|
|`pmp-p2-250`|export-batch-2|single|predictive|2|You are working on a project where two high-ranked executives keep derailing project meetings with their own agenda, and adding…|

### people-3 · Leading the team — 224 items

*Set expectations, empower the team, and clear problems out of its path.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p3-001`|core|pulldown|agile|3|Prompts: A newly formed team is willing but has n… / A long-standing team knows the domain be…|
|`pmp-p3-002`|core|single|agile|2|During a daily standup, a developer mentions for the third day running that they are still waiting on a test environment from…|
|`pmp-p3-003`|core|single|predictive|3|You take over a project mid-execution. Within a week you notice that meeting attendance is optional in practice, decisions get…|
|`pmp-p3-016`|core|pulldown|hybrid|4|Prompts: The squad chose a single-page layout for… / The squad has planned the scanner integr… / To speed up testing, the squad has agree…|
|`pmp-p3-005`|core|single|agile|3|In daily standups you have started assigning the day's work item by item, because two sprints ago the team left an urgent defect…|
|`pmp-p3-007`|core|single|agile|3|A developer who joined your team three months ago is visibly struggling. Their work needs rework more often than anyone else's,…|
|`pmp-p3-008`|core|single|predictive|2|You inherit a well-established project team from a project manager who decided everything personally. The team is capable and…|
|`pmp-p3-017`|core|single|agile|3|Your team is building a track-inspection app for a rail operator. With six working days left in the sprint, the developers find…|
|`pmp-p3-018`|core|multi|predictive|3|On a £9 million refurbishment of a county archive building, you promoted Priya, an electrician on the project for a year, to…|
|`pmp-p3-011`|core|single|hybrid|4|A supplier team of twelve works alongside your eight employees on the same product. You have been asked to empower the combined…|
|`pmp-p3-012`|core|pulldown|agile|3|Prompts: For the tester whose work is invisible o… / For the developer who has done the same…|
|`pmp-p3-013`|core|single|agile|2|A developer tells you privately that she found a defect in her own work after it had passed review, fixed it quietly, and said…|
|`pmp-p3-014`|core|single|hybrid|3|Your strongest engineer is technically outstanding and consistently dismissive in design reviews, cutting people off and calling…|
|`pmp-p3-015`|core|pulldown|agile|3|Prompts: On the choice of testing library, which… / On the order of next quarter's themes, w… / On accepting a defect into the release,…|
|`pmp-p3-061`|core|graphic|agile|2|Three months after forming, your team has agreed its working norms, members resolve most disagreements between themselves, and…|
|`pmp-case2-004`|case-02|single|agile|4|Sunita, the sponsor, asks you to assign tasks to individual developers so she can show councillors who is doing what. The team…|
|`pmp-case4-010`|case-04|single|predictive|3|The in-house exhibition designers have worked long hours for six weeks to recover from late design approvals. One of them has…|
|`pmp-case5-009`|case-05|single|agile|2|Two advisers from the Calder centre who joined the agile team say little in retrospectives, although they raise useful points…|
|`pmp-p3-101`|people-3|single|predictive|1|You are leading the move of a building society's core systems to a new data centre. The client has moved the cutover from a…|
|`pmp-p3-102`|people-3|single|agile|3|Your team builds a payments app for a fintech start-up. Velocity has risen from 34 to 46 points over three sprints, and the…|
|`pmp-p3-103`|people-3|single|predictive|3|You lead a pharmaceutical team preparing a regulatory submission. Six weeks ago you told the team the filing deadline was 14…|
|`pmp-p3-104`|people-3|single|agile|3|On an online retailer's checkout team, the commercial director has started messaging two developers directly with small changes…|
|`pmp-p3-105`|people-3|single|predictive|2|You manage a water utility's mains-replacement programme. At the steering meeting, the sponsor thanks you personally for…|
|`pmp-p3-106`|people-3|single|agile|4|Your team builds route-planning software for a logistics firm. For two weeks the developers have debated which of two…|
|`pmp-p3-107`|people-3|single|predictive|3|On a highway-widening project, Sinead, a senior surveyor, tells you privately that her father has been diagnosed with a serious…|
|`pmp-p3-108`|people-3|single|hybrid|5|For a museum exhibition fit-out, you delegated authority to the build team to choose audio-visual suppliers up to €40,000. They…|
|`pmp-p3-109`|people-3|single|agile|2|Your game studio team has five developers in Lisbon and two in Auckland, which is thirteen hours ahead. The daily sync runs at…|
|`pmp-p3-110`|people-3|single|predictive|3|You lead a council's housing-repairs system replacement. At a closed steering meeting you learn that the project may be paused at…|
|`pmp-p3-111`|people-3|single|hybrid|4|You lead a hybrid programme replacing a university's student records system: a fixed data-migration plan alongside two agile…|
|`pmp-p3-112`|people-3|single|hybrid|3|At a medical-device maker, hardware follows a stage-gated plan while firmware is built in sprints. Joanna, a capable mid-level…|
|`pmp-p3-113`|people-3|multi|agile|3|Your team at an insurance comparison site can finish a change in two days, but every release must pass the security change board,…|
|`pmp-p3-114`|people-3|multi|predictive|4|You are managing a planned maintenance shutdown of a chemical plant, with forty fitters working in shifts. Site rules give every…|
|`pmp-p3-115`|people-3|multi|hybrid|3|Your team spent nine months building a claims self-service portal for an insurer. Two weeks before launch, the board shelved it…|
|`pmp-p3-116`|people-3|matching|agile|3|Prompts: The test suite fails intermittently beca… / The payment provider has ignored the tea… / A developer finds that the story she pic…|
|`pmp-p3-117`|people-3|matching|predictive|2|Prompts: A technician's careful isolation check b… / A site supervisor has submitted the crew… / A whole crew installed drainage to the w…|
|`pmp-p3-118`|people-3|pulldown|hybrid|4|Prompts: In estimation sessions the team waits fo… / A well-liked developer is openly sceptic… / A contractor with deep knowledge of the…|
|`pmp-p3-119`|people-3|fillblank|predictive|3|Your seven-engineer team at a broadcaster has a 10-working-day phase ahead. Two engineers each have 2 days of booked leave. Each…|
|`pmp-p3-120`|people-3|hotspot|agile|2|The diagram is your team's skills matrix, one row per skill, showing current expertise and the sprint in which the skill is next…|
|`pmp-p3-121`|people-3|single|predictive|1|You manage the construction of a secondary school extension. Site rules require hard hats and high-visibility vests beyond the…|
|`pmp-p3-122`|people-3|single|agile|3|Your team at a travel-booking platform agreed in its working agreement that pull requests are reviewed within one working day.…|
|`pmp-p3-123`|people-3|single|predictive|4|You lead the €2.4 million installation of a new production line at an industrial bakery. A key supplier has slipped its delivery…|
|`pmp-p3-124`|people-3|multi|predictive|3|Your £38 million rail signalling upgrade has eight weeks of commissioning left. The team of fourteen engineers, half of them on…|
|`pmp-p3-125`|people-3|single|hybrid|4|On a hybrid programme building an online grocer's fulfilment platform, you introduced a monthly 'save of the month' shout-out at…|
|`pmp-p3-126`|people-3|single|agile|3|Your team builds a booking app for a chain of veterinary practices. Four developers work in the office and three work remotely…|
|`pmp-p3-127`|people-3|matching|predictive|2|Prompts: Engineers received messages late in the… / Two discipline leads disagreed over a ce… / Drawings and markups were kept in email,…|
|`pmp-p3-128`|people-3|single|hybrid|2|You lead a hybrid team at an engineering consultancy designing a flood-defence scheme. Every milestone so far has been celebrated…|
|`pmp-p3-129`|people-3|single|agile|3|Two developers on your language-learning app team ask to move to a compressed week of four ten-hour days with Fridays off.…|
|`pmp-p3-130`|people-3|multi|agile|3|Your sports-streaming team of five had worked smoothly for a year. Three developers joined a month ago. Since then stand-ups have…|
|`pmp-p3-131`|people-3|single|predictive|3|You manage the design of a £60 million hospital wing for an architectural practice. Six architects each name layers, files and…|
|`pmp-p3-132`|people-3|single|agile|2|During a sprint review for a housing association's tenant portal, a senior stakeholder interrupts a junior developer's…|
|`pmp-p3-133`|people-3|single|hybrid|3|You lead a programme replacing an airline's crew-rostering system: a stage-gated vendor integration alongside two in-house scrum…|
|`pmp-p3-134`|people-3|single|predictive|5|On a design-and-build contract for a €45 million water treatment works, the design and site teams report to different directors…|
|`pmp-p3-135`|people-3|single|agile|4|Your team maintains a bank's mortgage-application service. For four sprints nobody except two senior developers has picked up…|
|`pmp-p3-136`|people-3|multi|predictive|4|You manage the design of a road bridge for a county council. A structural engineer by background, you have been quietly…|
|`pmp-p3-137`|people-3|matching|hybrid|3|Prompts: Engineers in Seoul rarely raise objectio… / A developer in Pune answers 'that may be… / A major release has been scheduled for t…|
|`pmp-p3-138`|people-3|pulldown|agile|3|Prompts: The team must decide how out-of-hours on… / Ticket sales have failed during a live o… / The team needs an indexing approach for…|
|`pmp-p3-139`|people-3|fillblank|predictive|3|Your five-engineer team on a gas network upgrade gains two new engineers next month. Each newcomer is expected to deliver 25%,…|
|`pmp-p3-140`|people-3|hotspot|hybrid|2|The diagram maps a fortnight of design decisions on your hybrid team building a ferry operator's booking system. Each box shows a…|
|`pmp-p3-1`|export-batch-1|single|predictive|3|A project manager is building a project team. All of the project team members have worked together on previous projects. One of…|
|`pmp-p3-2`|export-batch-1|single|agile|2|A project manager for a new project is tasked with applying agile practices to improve team performance.|
|`pmp-p3-3`|export-batch-1|single|agile|3|A project manager has a newly formed team that includes junior members who have not worked on agile projects before. The product…|
|`pmp-p3-4`|export-batch-1|single|agile|2|A newly appointed product owner is not sure how to prioritize product specifications and mentions this challenge to the project…|
|`pmp-p3-5`|export-batch-1|single|predictive|3|The project scope statement is approved by the project sponsor. The team members would like to modify the work breakdown…|
|`pmp-p3-6`|export-batch-1|single|agile|2|An agile project team member is working on a user story that is overdue. This delay is causing the team to miss sprint goals for…|
|`pmp-p3-7`|export-batch-1|single|predictive|3|A project manager is assigned to an ongoing project with virtual team members from different countries who are working remotely…|
|`pmp-p3-8`|export-batch-1|single|predictive|3|During a one-on-one session, a new team member shared their frustration regarding their inability to successfully complete an…|
|`pmp-p3-9`|export-batch-1|single|agile|3|A project team adopted agile practices to meet the business need to develop an application. One of the senior team members…|
|`pmp-p3-10`|export-batch-1|single|hybrid|4|A project manager has been assigned to a project that is currently late and over budget. The project uses a predictive approach…|
|`pmp-p3-11`|export-batch-1|single|predictive|3|An extremely talented and results-oriented team member is suddenly struggling with a personal problem. Due to this, there is a…|
|`pmp-p3-12`|export-batch-1|single|agile|3|A new agile team is forming and the project manager notices that the team is struggling in many areas. The team is experiencing…|
|`pmp-p3-13`|export-batch-1|single|predictive|2|A project team has just reached a project milestone successfully. The project manager noticed that the team performed extremely…|
|`pmp-p3-14`|export-batch-1|single|agile|3|A project manager oversees an agile project with an experienced project team. The project manager does not typically attend the…|
|`pmp-p3-201`|export-batch-2|single|hybrid|3|A highly skilled subject matter expert (SME) is engaged in a government project where approximately 30% of the deliverables will…|
|`pmp-p3-202`|export-batch-2|single|predictive|3|The lead sales manager and team are unable to submit a project status report on time for the new sales report integration…|
|`pmp-p3-203`|export-batch-2|single|agile|3|A midsize project has the objective of developing an innovative product with changing requirements. Several team members are…|
|`pmp-p3-204`|export-batch-2|single|hybrid|2|A project manager used to work on predictive projects and now has started working on a project using a hybrid approach.|
|`pmp-p3-205`|export-batch-2|single|predictive|2|A project manager believes that the project team could benefit from development activities to improve project performance and…|
|`pmp-p3-206`|export-batch-2|single|agile|2|Due to a lack of locally skilled resources, a company employs an internationally dispersed project team that will work virtually.…|
|`pmp-p3-207`|export-batch-2|single|predictive|2|A project manager notices that team members are not interacting with each other and believes that the lack of interaction is…|
|`pmp-p3-208`|export-batch-2|single|predictive|2|The project manager of a large team notices team members are not interacting together, which is impacting project performance and…|
|`pmp-p3-209`|export-batch-2|single|predictive|3|A project is in the implementation phase. A new project manager discovers that the team members have no previous experience with…|
|`pmp-p3-210`|export-batch-2|single|predictive|2|A project manager observes that team members are not collaborating or being proactive.|
|`pmp-p3-211`|export-batch-2|single|predictive|2|A project manager realizes that the team needs training on a specific technology that is required in the project.|
|`pmp-p3-212`|export-batch-2|single|agile|2|A team member who is unfamiliar with the subject matter is struggling to complete a complex deliverable.|
|`pmp-p3-213`|export-batch-2|single|predictive|3|A project manager is assembling a team with a mix of new and experienced team members. The project manager finds out that there…|
|`pmp-p3-214`|export-batch-2|single|agile|2|A project manager is not sure if the team has the critical skills needed to implement a key feature of an upcoming deliverable.|
|`pmp-p3-215`|export-batch-2|single|agile|2|A project manager notices that conflicts between team members are occurring in every sprint. After an investigation, the project…|
|`pmp-p3-216`|export-batch-2|single|agile|2|A remote project team consists of members from two countries. One of the teams is expecting to have three days off to celebrate a…|
|`pmp-p3-217`|export-batch-2|single|agile|3|Members of an agile team are struggling. While there is trust among the team members, the majority of team decisions are made by…|
|`pmp-p3-218`|export-batch-2|single|hybrid|2|In the early stages of team formation, a project manager is unable to focus on the main tasks because team members are constantly…|
|`pmp-p3-219`|export-batch-2|single|predictive|2|At the first meeting about a road construction project, the project manager reminds the team of the importance of observing all…|
|`pmp-p3-220`|export-batch-2|single|hybrid|1|A project manager is setting expectations with a team that is delivering a hybrid project.|
|`pmp-p3-221`|export-batch-2|single|predictive|2|A senior team member was very influential and played a crucial role in past projects. In this new project, the senior member no…|
|`pmp-p3-222`|export-batch-2|single|hybrid|3|A project manager is leading a project using a hybrid approach. The project team began the project with high morale and…|
|`pmp-p3-223`|export-batch-2|single|agile|2|Working in a continuous delivery environment, a project leader notices that one of the key team members is experiencing low…|
|`pmp-p3-224`|export-batch-2|single|predictive|2|During project execution, the project manager realizes that a team member is not completing tasks in accordance with the project…|
|`pmp-p3-225`|export-batch-2|single|hybrid|1|A project team is working on a regulatory application that has a fixed duration for delivery. The team is using a hybrid approach…|
|`pmp-p3-226`|export-batch-2|single|agile|2|A new project manager is assigned to lead an agile project. The project manager wants to use motivation to encourage the team to…|
|`pmp-p3-227`|export-batch-2|single|agile|3|A project manager leads two highly motivated teams who typically work on 3-4 concurrent short projects with tight deadlines.…|
|`pmp-p3-228`|export-batch-2|single|predictive|2|A new member joined a project during the execution phase. The work requires strong collaboration with other team members.|
|`pmp-p3-229`|export-batch-2|single|predictive|2|A project manager begins to notice disengaged members arriving late, not delivering on time, not attending meetings, and…|
|`pmp-p3-230`|export-batch-2|single|agile|3|A project manager is leading a Scrum team. During Business Acceptance Testing (BAT), the client rejects the deliverable due to…|
|`pmp-p3-231`|export-batch-2|single|predictive|3|During the execution phase of a project, a new project manager is assigned to replace the previous project manager. As the…|
|`pmp-p3-232`|export-batch-2|single|hybrid|3|A team is assembled for a new project that will be using a predictive approach through requirements gathering and an agile…|
|`pmp-p3-233`|export-batch-2|single|predictive|2|A project manager has been on a complex project that began eight months ago. The project manager has a quality team that has been…|
|`pmp-p3-234`|export-batch-2|single|agile|1|An organization has decided to implement agile and a project manager with Scrum Master has been selected to coach the project…|
|`pmp-p3-235`|export-batch-2|single|predictive|2|During the execution phase of your project, a Resource Manager informed you that one of the experts allocated to your project had…|
|`pmp-p3-236`|export-batch-2|single|predictive|2|During project execution, a team member made a mistake resulting in a 1-week delay to the project schedule.|
|`pmp-p3-237`|export-batch-2|single|predictive|2|The project manager for a complex project discovers that a certain team member has missed several targets.|
|`pmp-p3-238`|export-batch-2|single|predictive|2|A project leader noticed that a new team member is demotivated and always arrives late to work, causing a delay in the…|
|`pmp-p3-239`|export-batch-2|single|predictive|2|A project manager is assigned to an existing project, replacing the previous project manager. Soon after joining the project, the…|
|`pmp-p3-240`|export-batch-2|single|predictive|3|A project manager must make decisions daily, regardless of the decision's level of importance or criticality.|
|`pmp-p3-241`|export-batch-2|multi|hybrid|3|A project manager wants to develop their own leadership capability during a long project. Select three ways to improve and…|
|`pmp-p3-242`|export-batch-2|single|hybrid|2|A project manager is managing a hybrid project. A stakeholder has directly contacted a team member and has requested a change.…|
|`pmp-p3-243`|export-batch-2|single|hybrid|3|A project manager is managing a project with a hybrid approach and has requested a senior team member to perform a quantitative…|
|`pmp-p3-244`|export-batch-2|single|hybrid|2|A newly formed project team features members from several countries and continents, with significant age differences and a…|
|`pmp-p3-245`|export-batch-2|single|hybrid|1|A project manager engaged in the delivery of a global project is in the process of recruiting a project team.|
|`pmp-p3-246`|export-batch-2|single|predictive|2|Some team members assigned to a new technology project expressed their lack of knowledge about the technology and the challenges…|
|`pmp-p3-247`|export-batch-2|single|predictive|2|A project manager is in a matrix organization environment. The project manager notices the team is not working well together,…|
|`pmp-p3-248`|export-batch-2|single|hybrid|3|A team member received a request from a stakeholder to conduct a quantitative risk analysis. The team member is not certain…|
|`pmp-p3-249`|export-batch-2|single|agile|3|In the middle of a project incremental delivery, due to a change in the technology, a team member informed the project leader…|
|`pmp-p3-250`|export-batch-2|single|agile|1|A product owner has requested to incorporate embedded functionality for the patient portal in the next sprint. The team decided…|
|`pmp-p3-251`|export-batch-2|single|predictive|2|A project team just completed a major milestone and is now on track to deliver the project ahead of the due date. The project…|
|`pmp-p3-252`|export-batch-2|single|predictive|2|A project manager wants to motivate their team members and improve project performance.|
|`pmp-p3-253`|export-batch-2|multi|predictive|3|An internal project has delivered its scope on time, within budget, and with the expected quality. The client was satisfied with…|
|`pmp-p3-254`|export-batch-2|single|agile|2|An experienced project manager is managing a multi-million dollar agile project. The project manager communicates the ground…|
|`pmp-p3-255`|export-batch-2|single|agile|3|Since the beginning of a project, the product owner keeps asking during ceremonies about the budget spent for each product…|
|`pmp-p3-256`|export-batch-2|single|agile|2|A project team recently completed a long-term project using a predictive approach. The team then started a new project with the…|
|`pmp-p3-257`|export-batch-2|single|hybrid|3|A project is being executed. The project's life cycle is defined as predictive; however, a major deliverable will be handed over…|
|`pmp-p3-258`|export-batch-2|single|predictive|2|At which stage in the Tuckman team development model should the project manager put the most focus on increasing team motivation?|
|`pmp-p3-259`|export-batch-2|single|agile|2|A team member approaches the project manager immediately after the end of the project meeting about his need for help to complete…|
|`pmp-p3-260`|export-batch-2|single|hybrid|2|A project team often asks questions about the rationale for their work and how the client perceives their progress and…|
|`pmp-p3-261`|export-batch-2|single|predictive|3|Project manager B has been assigned to take over a project from Project Manager A. Project Manager A informs Project Manager B…|
|`pmp-p3-262`|export-batch-2|single|hybrid|3|A project manager with a directive behavioral orientation is leading a virtual team of software developers. The project manager…|
|`pmp-p3-263`|export-batch-2|single|agile|2|A project manager is working with a Scrum team that is continually missing deadlines. The steering committee is concerned about…|
|`pmp-p3-264`|export-batch-2|single|predictive|2|A lengthy, ongoing project exhausts a project team. Due to deadline issues, the project manager cannot extend the project…|
|`pmp-p3-265`|export-batch-2|single|agile|3|The stakeholders and project team working on an agile project meet with the project manager at the end of an iteration. The…|
|`pmp-p3-266`|export-batch-2|single|predictive|2|A team lead is assigned to an ongoing project. The team members have been working together for a while.|
|`pmp-p3-267`|export-batch-2|single|predictive|2|A project manager wants to establish an incentive and rewards strategy for the project team.|
|`pmp-p3-268`|export-batch-2|single|agile|2|A team member is working on a critical task for a project, and approaches the project manager immediately before the start of a…|
|`pmp-p3-269`|export-batch-2|single|agile|3|During the fourth iteration of an agile project, the customer requests a technical change to the development of the project…|
|`pmp-p3-270`|export-batch-2|single|agile|2|A new college graduate with a degree in computer science was recently hired by an organization. The new employee has been…|
|`pmp-p3-271`|export-batch-2|single|agile|2|A project manager is leading a daily standup meeting when a team member requests that other team members take on some of their…|
|`pmp-p3-272`|export-batch-2|multi|agile|3|(Choose 2).|
|`pmp-p3-273`|export-batch-2|single|agile|2|A software developer who is working on a critical component has to take emergency leave from the project. Due to time…|
|`pmp-p3-274`|export-batch-2|single|predictive|2|Team members express frustration because their functional manager does not listen to their opinions. They seem reluctant to…|
|`pmp-p3-275`|export-batch-2|single|predictive|2|A project is in the execution phase when the project manager needs to assign new resources to the existing project team.|
|`pmp-p3-276`|export-batch-2|single|predictive|1|In which stage of Tuckman's stages of team development do the team members begin to resolve conflicts effectively?|
|`pmp-p3-277`|export-batch-2|single|agile|2|A project manager is managing an agile project where the team is regularly missing user story due dates.|
|`pmp-p3-278`|export-batch-2|single|predictive|2|A project manager is committed to the success of a multinational project with tight deadlines. A new team member is continually…|
|`pmp-p3-279`|export-batch-2|single|agile|2|A project manager leading an agile project wants to motivate the project team to improve performance.|
|`pmp-p3-280`|export-batch-2|single|predictive|2|A project team is regularly missing milestones. Stakeholders are concerned that the project will miss the deliverable due date.…|
|`pmp-p3-281`|export-batch-2|single|agile|3|Two agile teams are collaborating on a project. Team A's output is not complete enough for Team B to integrate into their backlog.|
|`pmp-p3-282`|export-batch-2|single|predictive|2|A project manager notices schedule performance issues due to the expertise levels of project resources. Some experienced…|
|`pmp-p3-283`|export-batch-2|single|hybrid|2|A hybrid project has the following characteristics: It is in the early stages. It has a brand new team. The project manager will…|
|`pmp-p3-284`|export-batch-2|single|hybrid|3|A project is using a hybrid approach and team performance is consistently excellent during the predictive stages. During the…|
|`pmp-p3-285`|export-batch-2|single|predictive|2|A goal has been set at four months for a project team to distribute a product. Unfortunately, it will be difficult to adhere to…|
|`pmp-p3-286`|export-batch-2|single|agile|2|Members from an agile team have been consistently tardy for stand-up meetings and are not meeting project target dates. The…|
|`pmp-p3-287`|export-batch-2|single|predictive|2|A project's deliverables due date is approaching, but one team member's performance is negatively impacting the schedule.|
|`pmp-p3-288`|export-batch-2|single|predictive|2|A project manager observed that a team member is becoming increasingly disengaged from an ongoing project.|
|`pmp-p3-289`|export-batch-2|single|predictive|2|A project manager notices that one team member seems increasingly unhappy during team meetings. The team member is not making…|
|`pmp-p3-290`|export-batch-2|single|predictive|2|Early in a project, the project manager realizes that one of the key team members is not prepared to perform the assigned task.|
|`pmp-p3-291`|export-batch-2|single|predictive|2|A project manager provided video-based training for new software but several team members reported feeling unprepared to use it.|
|`pmp-p3-292`|export-batch-2|single|agile|2|A new project manager joins an existing team. The project manager is made aware that there are strong personalities on the team,…|
|`pmp-p3-293`|export-batch-2|single|predictive|2|A project manager's team has been efficiently delivering expected results in an environment where budget and scope are fixed.…|
|`pmp-p3-294`|export-batch-2|single|agile|2|In an agile project initiation phase, a senior team member suggests using a new documentation tool based on experience on a…|
|`pmp-p3-295`|export-batch-2|single|agile|3|An agile project was scaled from the feasibility study phase to the implementation phase. Due to time constraints, the same team…|
|`pmp-p3-296`|export-batch-2|single|hybrid|2|A project is in the execution phase when new team members are onboarded.|
|`pmp-p3-297`|export-batch-2|single|predictive|2|A project manager recently took over a project from a project manager who left the company. The new project manager is aware that…|
|`pmp-p3-298`|export-batch-2|single|agile|3|A project manager works in an organization where the culture encourages virtual teams. New challenges arise periodically in this…|
|`pmp-p3-299`|export-batch-2|single|agile|3|A project manager observes that team members are in a low mood after demos. Feedback from stakeholders is constantly causing…|
|`pmp-p3-300`|export-batch-2|multi|hybrid|3|A large transformation project has moved its project management office (PMO) to a virtual environment. There have been…|
|`pmp-p3-301`|export-batch-2|single|hybrid|2|A project manager is working on a global project with team members in several different countries. To manage communications, the…|
|`pmp-p3-302`|export-batch-2|single|predictive|2|During the execution phase of a project, the project manager is reassigned to another project. A new project manager takes over…|
|`pmp-p3-303`|export-batch-2|single|agile|2|A project team is working on a complex technology project. A competitor introduces a product with a new feature, forcing the team…|
|`pmp-p3-304`|export-batch-2|single|predictive|2|Regular check-ins with the delivery team reveal low morale and a lack of efficiency.|
|`pmp-p3-305`|export-batch-2|single|predictive|2|A team member could not be reached by email or phone during the week that they were absent. The team member had not notified…|
|`pmp-p3-306`|export-batch-2|multi|predictive|3|A project manager is preparing to start a project and notices that the project team is having difficulties communicating and…|
|`pmp-p3-307`|export-batch-2|single|agile|2|On an agile project, a team member has been taking smaller, easier user stories from the backlog, not those with the highest…|
|`pmp-p3-308`|export-batch-2|multi|hybrid|2|A project manager is beginning a new project and is starting to form the project team. The project manager has not worked with…|
|`pmp-p3-309`|export-batch-2|single|agile|2|In the middle of a release, the project manager is replaced. Feedback provided by the team to the new project manager hinted at…|
|`pmp-p3-310`|export-batch-2|single|agile|2|A software delivery team at a large bank is frustrated by the documentation requirements put in place by the governance…|
|`pmp-p3-311`|export-batch-2|single|agile|1|A company is implementing an agile approach for a new project.|
|`pmp-p3-312`|export-batch-2|single|predictive|2|A project manager notices that a critical team member has lost interest in the project. The team member's lack of interest has…|
|`pmp-p3-313`|export-batch-2|single|agile|2|An agile project team has not completed the tasks within the current iteration. During a meeting, the project sponsor gives the…|
|`pmp-p3-314`|export-batch-2|single|agile|2|A developer is expressing concerns about the time spent on daily standups, updating stories, creating tasks, and reviewing…|
|`pmp-p3-315`|export-batch-2|single|predictive|3|During a weekly team meeting, the project manager reviewed task progress and found that a senior team member did not follow the…|
|`pmp-p3-316`|export-batch-2|single|hybrid|3|In the middle of a software development project following a predictive lifecycle, a significant portion of the software is set…|
|`pmp-p3-317`|export-batch-2|single|hybrid|2|During routine progress update sessions for a multinational project, a project manager notes that one of the key technical…|
|`pmp-p3-318`|export-batch-2|single|agile|2|The goal on a project is to foster a culture of cooperation that drives the team towards exceptional performance. Following a…|
|`pmp-p3-319`|export-batch-2|single|agile|2|In a routine progress check, an agile project manager notices that a lead analyst is habitually passing off less complex analysis…|
|`pmp-p3-320`|export-batch-2|single|agile|2|During the second sprint of a software development project, it's observed that one team member frequently dictates scrum…|
|`pmp-p3-321`|export-batch-2|single|predictive|2|As the deadline for a critical project approaches, the project manager notices a decrease in team enthusiasm, with several…|
|`pmp-p3-322`|export-batch-2|single|hybrid|2|In leading a complex software development project with team members spread across diverse geographical locations, the project…|
|`pmp-p3-323`|export-batch-2|single|predictive|2|Following a corporate restructuring, the project manager notices that the newly assembled team is having difficulty adapting to…|
|`pmp-p3-324`|export-batch-2|single|predictive|2|After a project milestone, a team member voices concerns over the lack of detailed feedback regarding their project…|
|`pmp-p3-325`|export-batch-2|single|agile|1|You have been working on a project where the manager leads in a dictatorship style. Over time, the team have stopped making…|
|`pmp-p3-326`|export-batch-2|single|predictive|2|Team members lack enthusiasm for participating in essential project planning sessions, with many arriving late or not…|
|`pmp-p3-327`|export-batch-2|single|agile|2|Discovering that one project team member feels their efforts are undervalued compared to others, leading to a decline in their…|
|`pmp-p3-328`|export-batch-2|multi|agile|3|During a critical phase of a healthcare software implementation project, the project manager realizes that the project team is…|
|`pmp-p3-329`|export-batch-2|single|predictive|1|Following a critical review session of a healthcare software project, a project manager notes that the output from one of the…|
|`pmp-p3-330`|export-batch-2|single|predictive|2|In the late stages of launching a marketing campaign project, it's identified that a critical piece of content is not ready…|
|`pmp-p3-331`|export-batch-2|single|predictive|1|Near the completion of an extensive software development project, it's noted that one developer's innovative solutions have…|
|`pmp-p3-332`|export-batch-2|single|predictive|3|In the midst of executing a high-stakes software development project, several unexpected software bugs and team miscommunications…|
|`pmp-p3-333`|export-batch-2|single|predictive|1|You are managing a project team mostly made up of men. Recently Jane was hired for her high expertise in the brands that make up…|
|`pmp-p3-334`|export-batch-2|single|agile|3|Your agile project team has only recently formed. The product is still unclear, and there have been disagreements and minor…|
|`pmp-p3-335`|export-batch-2|single|predictive|2|Your project team has started to find their rhythm, yet you notice that they seem disengaged in their work and are dragging their…|
|`pmp-p3-336`|export-batch-2|single|predictive|1|Your project team came together from different parts of the organisation, and were introduced formally to each other. Now you…|
|`pmp-p3-337`|export-batch-2|single|agile|2|You are working as an Agile project manager in a new project team. Many project team members are despondent, and do not show up…|
|`pmp-p3-338`|export-batch-2|single|agile|2|Your Agile team leader works through servant leadership to the team, and has built a safe environment for disagreement.|
|`pmp-p3-339`|export-batch-2|single|agile|2|New projects go through the Tuckman Model of forming, storming, norming, and performing. You have just taken the lead of a new…|
|`pmp-p3-340`|export-batch-2|single|predictive|1|You have been working on a large waterfall project for some time, and one of the business analysts working with you has shown…|
|`pmp-p3-341`|export-batch-2|single|predictive|1|You are working in an organisation where a large change is happening to multiple departments. You have been asked to manage this…|
|`pmp-p3-342`|export-batch-2|single|predictive|2|You have started as a junior project manager on an internal process improvement project within your existing team. Your program…|
|`pmp-p3-343`|export-batch-2|single|predictive|1|In the last few months, your project team have had a large amount of conflict, finding their role and place within the team. They…|
|`pmp-p3-344`|export-batch-2|single|predictive|3|The project management office is looking to you for help in hiring and coaching new project managers, and they ask you to…|
|`pmp-p3-345`|export-batch-2|single|agile|2|You are leading an Agile team that has members in various locations around the country. You have arranged daily stand-ups via…|
|`pmp-p3-346`|export-batch-2|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values.|
|`pmp-p3-347`|export-batch-2|single|agile|2|You are working on the requirements for the next iteration and a decision needs to be made by the team. The product owner…|
|`pmp-p3-348`|export-batch-2|single|agile|2|You have just taken over a project that is behind schedule. The previous Project Manager was brought on during a company crisis,…|
|`pmp-p3-349`|export-batch-2|single|hybrid|2|The project you have taken over recently was behind schedule. It has seen some improvement with some minor releases, but the team…|
|`pmp-p3-350`|export-batch-2|single|hybrid|2|You are working as a Project Manager on a project where the Project Sponsor makes all decisions on project requirements and…|
|`pmp-p3-351`|export-batch-2|single|predictive|1|You have just been allocated to a project where the team has been working together for the past six months. The Project…|
|`pmp-p3-352`|export-batch-2|single|agile|2|The project you are working on is on schedule, but has been marred with conflict and politics for its duration so far. You ask…|

### people-4 · Stakeholder engagement — 153 items

*Identify who matters, analyse them, and tailor how you work with each.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p4-001`|core|multi|agile|3|Your sprint reviews are attended by the product owner and nobody else. Three business units will use the product at launch, and…|
|`pmp-p4-002`|core|single|predictive|2|You have identified twenty-eight stakeholders on a regulatory reporting project with a filing deadline in nine months, spread…|
|`pmp-p4-003`|core|single|hybrid|4|A senior operations manager with no formal authority over your project has begun criticising it in forums you are not invited to.…|
|`pmp-p4-004`|core|multi|predictive|3|A regulatory reporting project has a general counsel who holds an effective veto at the sign-off gate but has attended none of…|
|`pmp-p4-005`|core|single|predictive|3|A department head has attended every governance meeting for your systems replacement and has never objected to anything. Your…|
|`pmp-p4-006`|core|multi|hybrid|3|Your programme spans offices in three time zones. The team nearest you is well informed and shapes most decisions. The other two…|
|`pmp-p4-007`|core|single|agile|4|Your product owner accepts work at each review and then, two or three sprints later, asks for the same feature to be reworked…|
|`pmp-p4-008`|core|single|predictive|3|Four weeks from go-live on a payroll replacement, a union representative contacts you to say the change alters how overtime is…|
|`pmp-p4-009`|core|matching|hybrid|3|Prompts: A regulator who cannot influence day-to-… / A department head whose staff must adopt… / An IT security lead who has raised the s…|
|`pmp-p4-015`|core|single|agile|3|Your agile team is building a duty-calculation engine for a freight forwarder. Developers message the firm's three customs…|
|`pmp-p4-011`|core|hotspot|predictive|3|The diagram shows the sequence your organisation follows when working with stakeholders on a new project. A regulator who was…|
|`pmp-p4-012`|core|single|hybrid|3|On a hybrid finance transformation, a regional director with no formal role on the programme has twice persuaded the steering…|
|`pmp-p4-013`|core|single|predictive|2|Your project will widen a road through a residential area. A residents' association has formed, has begun writing to local…|
|`pmp-p4-014`|core|multi|agile|3|The operations director is your most significant stakeholder and has sent a junior analyst to every review for the past two…|
|`pmp-p4-061`|core|graphic|predictive|3|The power and interest grid for your reservoir dam safety upgrade was drawn at the start of the project. This week the regional…|
|`pmp-case3-005`|case-03|single|hybrid|3|Dr Helen Park, the chief biomedical scientist at Site C, was not part of the requirements workshops. After a demonstration she…|
|`pmp-p4-101`|people-4|single|predictive|1|You are managing a £3.2 million refurbishment of a regional museum's galleries. Your stakeholder engagement plan schedules a…|
|`pmp-p4-102`|people-4|single|predictive|3|On a rail signalling upgrade, the main installation contractor's new engagement lead asks you for a copy of your stakeholder…|
|`pmp-p4-103`|people-4|single|agile|4|Your team is building a stock-ordering app for 640 bakery franchisees. Because the whole network cannot attend sprint reviews,…|
|`pmp-p4-104`|people-4|single|hybrid|5|Your organisation is developing a telematics service for its tractors, with the hardware on a predictive plan and the software…|
|`pmp-p4-105`|people-4|single|predictive|2|You are replacing the laboratory information system at a pharmaceutical quality-control site. The lab manager is the only person…|
|`pmp-p4-106`|people-4|single|agile|3|A credit union's agile team is rebuilding its loan application journey. The compliance officer has started insisting that every…|
|`pmp-p4-107`|people-4|single|predictive|3|You are managing a £14 million new-build secondary school. Your engagement plan promised the heads of science, art and design…|
|`pmp-p4-108`|people-4|single|hybrid|4|Eight months into a council's housing repairs system replacement, your stakeholder register still records the head of tenant…|
|`pmp-p4-109`|people-4|single|agile|2|On a travel company's booking platform, the regional sales director has started attending the team's daily stand-up. Each morning…|
|`pmp-p4-110`|people-4|single|hybrid|3|You are planning consultation with academic staff for a university's new coursework submission system. Your engagement plan…|
|`pmp-p4-111`|people-4|single|agile|3|A logistics firm's agile team will demonstrate an automated shift-allocation feature at tomorrow's sprint review, which the…|
|`pmp-p4-112`|people-4|single|predictive|4|On a hospital pharmacy automation project, your business analyst has completed the stakeholder engagement assessment matrix for…|
|`pmp-p4-113`|people-4|multi|agile|3|Your company funds an open-source data-visualisation library, and you lead the agile team that maintains it. The next major…|
|`pmp-p4-114`|people-4|multi|predictive|4|You are managing the digitisation of a national archive's map collection on a predictive plan. Your engagement plan treats the…|
|`pmp-p4-115`|people-4|multi|hybrid|3|Six months into a hybrid programme moving a dental group's 40 practices to electronic patient records, the sponsor asks how you…|
|`pmp-p4-116`|people-4|matching|predictive|3|Prompts: You suspect there are affected groups in… / You need to be sure that every external… / A neighbouring council completed a simil…|
|`pmp-p4-117`|people-4|matching|agile|2|Prompts: The head of branch operations wants to t… / The marketing director wants a loyalty f… / The risk manager wants to see at any tim…|
|`pmp-p4-118`|people-4|pulldown|hybrid|3|Prompts: The airline operators' committee is asse… / The baggage handlers' shift manager is a… / The commercial director is assessed as l…|
|`pmp-p4-119`|people-4|fillblank|predictive|2|Your team prioritises stakeholders for a ferry-terminal expansion using a weighted score: power counts 60% and interest 40%, each…|
|`pmp-p4-120`|people-4|hotspot|agile|3|A charity's agile team is building a volunteer-scheduling app. The volunteer network coordinator attends every sprint review,…|
|`pmp-p4-121`|people-4|single|predictive|1|You are managing an ERP replacement for a food manufacturer. The warehouse manager asked for a bespoke pallet-labelling module,…|
|`pmp-p4-122`|people-4|single|predictive|3|You are managing a council's £6 million leisure centre rebuild. Your engagement plan includes a community open day next month at…|
|`pmp-p4-123`|people-4|single|predictive|4|You have taken over a €40 million warehouse automation project. In your first meeting with the employee forum, an advisory body…|
|`pmp-p4-124`|people-4|single|predictive|4|You are managing a £22 million water pipeline for a regional utility. The route crosses a farm whose owner is in a legal dispute…|
|`pmp-p4-125`|people-4|single|predictive|3|You are managing the relocation of a university's chemistry department to a new building. In a private conversation, a research…|
|`pmp-p4-126`|people-4|single|agile|2|Your agile team is building a journey-planning app for a rail operator. A sight-loss charity has joined as a stakeholder, and two…|
|`pmp-p4-127`|people-4|single|agile|3|An insurer's claims-portal team has become popular, and its fortnightly sprint review now draws about 45 people from across the…|
|`pmp-p4-128`|people-4|single|agile|3|Your agile team is building a reading-practice app for children aged seven to nine. Three primary schools have agreed to take…|
|`pmp-p4-129`|people-4|single|agile|5|A bank's agile data team has built a model that ranks suspected card-fraud cases for 60 investigators, replacing a first-in,…|
|`pmp-p4-130`|people-4|single|hybrid|4|On a hybrid programme replacing a port authority's vessel-booking system, the harbour master has raised objections at each of the…|
|`pmp-p4-131`|people-4|single|hybrid|3|Your hybrid programme is consolidating an insurer's three offices into one. The facilities director controls the floor space…|
|`pmp-p4-132`|people-4|single|agile|2|An online furniture retailer's agile team is building a returns-processing app. The head of customer returns is a key stakeholder…|
|`pmp-p4-133`|people-4|multi|agile|3|Your agile team is building a task-allocation app for a hospital's porters, commissioned by the hospital's estates manager. The…|
|`pmp-p4-134`|people-4|multi|predictive|4|You are managing the consolidation of a utility's four billing centres into two, a predictive project going live in nine months.…|
|`pmp-p4-135`|people-4|multi|hybrid|3|Your hybrid programme for a fashion retailer has finished building a new point-of-sale system with a core team of…|
|`pmp-p4-136`|people-4|matching|hybrid|3|Prompts: A non-executive director has questioned… / Site foremen doubt that filling in diari… / The firm's IT architect wants detail on…|
|`pmp-p4-137`|people-4|matching|predictive|2|Prompts: The fire safety officer, whose approval… / The external support desk that will take… / Residents' families, who have asked how…|
|`pmp-p4-138`|people-4|pulldown|hybrid|3|Prompts: At one site, managers agree with every p… / At another site, junior engineers will n… / Near a third site, the neighbouring comm…|
|`pmp-p4-139`|people-4|fillblank|predictive|2|You can give 20 hours a month to engagement on a water utility's smart-meter programme. The plan has monthly one-hour meetings…|
|`pmp-p4-140`|people-4|hotspot|agile|3|A bus operator's agile team is replacing paper concessionary passes with a phone app. A pensioners' forum has a formal right to…|
|`pmp-p4-1`|export-batch-1|single|predictive|4|A project manager conducted an analysis with relevant stakeholders to validate project alignment with organizational strategy and…|
|`pmp-p4-2`|export-batch-1|single|predictive|3|During the planning phase of a project, the project manager is having trouble identifying stakeholders. Without input from the…|
|`pmp-p4-3`|export-batch-1|single|predictive|3|A project manager has just finished a project charter and received approval from the project sponsor. The project manager then…|
|`pmp-p4-4`|export-batch-1|single|agile|3|A project manager receives an email shortly after the beginning of a sprint. It is from a key stakeholder who is unfamiliar with…|
|`pmp-p4-5`|export-batch-1|single|predictive|3|During the first few days of executing a six-week project, an impediment is discovered by the project team. The project manager…|
|`pmp-p4-6`|export-batch-1|single|predictive|3|A project manager has been assigned to a multimillion-dollar project already in execution. During a project status meeting, one…|
|`pmp-p4-7`|export-batch-1|single|predictive|3|A new product development project has been launched. The project sponsor has reviewed the results of the risk analysis as…|
|`pmp-p4-8`|export-batch-1|single|predictive|2|A project manager has recently been assigned to an existing multinational project and is not familiar with the key stakeholders.|
|`pmp-p4-9`|export-batch-1|single|predictive|2|A project manager is leading a project where a low-level stakeholder has just been promoted to a new position and has now become…|
|`pmp-p4-10`|export-batch-1|single|predictive|3|A company is implementing a new computerized system. During project execution, the leader of a department that is critical to…|
|`pmp-p4-11`|export-batch-1|single|agile|3|A key stakeholder is upset about the new features in the current iteration for the product. The key stakeholder was unavailable…|
|`pmp-p4-12`|export-batch-1|single|predictive|3|An external stakeholder has a significant impact on the project, as they have the ability to approve or reject changes to the…|
|`pmp-p4-13`|export-batch-1|single|predictive|3|A project manager is working on a project charter and discovers that two key stakeholders are transferring to other departments…|
|`pmp-p4-14`|export-batch-1|single|predictive|3|During project execution, a key stakeholder is replaced with a new team member who has approval authority. The new member states…|
|`pmp-p4-15`|export-batch-1|single|predictive|3|A five-year highway construction project is in its third year of execution when the project manager receives a complaint from a…|
|`pmp-p4-16`|export-batch-1|single|predictive|3|During a kick-off meeting with the project sponsor and various project members, it became clear that it was not necessary for…|
|`pmp-p4-17`|export-batch-1|single|predictive|3|During project implementation, a stakeholder is questioning the purpose of the project and wants to pause the project to assess a…|
|`pmp-p4-18`|export-batch-1|single|agile|3|A company is creating a new application for its users. The agile team completed the initiating stage and is performing iterations…|
|`pmp-p4-19`|export-batch-1|single|predictive|2|An experienced project manager is mentoring a new project manager assigned to their first project. As the new project manager…|
|`pmp-p4-201`|export-batch-2|single|predictive|3|A project manager in the execution phase of a project discovers several stakeholders identified at project initiation are no…|
|`pmp-p4-202`|export-batch-2|single|hybrid|3|An ongoing project has a high degree of change.|
|`pmp-p4-203`|export-batch-2|single|predictive|3|A project manager has been working on an internal project for the organization's project management office (PMO). The project was…|
|`pmp-p4-204`|export-batch-2|single|predictive|3|A project manager is in the execution phase of a project. A newly identified stakeholder continuously bypasses the project…|
|`pmp-p4-205`|export-batch-2|single|predictive|3|A project is in the planning phase and requires tremendous time and effort for data collection and analysis. The project sponsor…|
|`pmp-p4-206`|export-batch-2|single|agile|2|A senior stakeholder was invited to a sprint review meeting and asked a project team member to describe the final product that…|
|`pmp-p4-207`|export-batch-2|single|hybrid|3|A business environment has become unstable because a stakeholder has altered the project deliverables and outcomes in the middle…|
|`pmp-p4-208`|export-batch-2|single|predictive|2|A project stakeholder has been missing scheduled facilitation workshops and has delegated assigned tasks to another member of the…|
|`pmp-p4-209`|export-batch-2|single|predictive|2|A project manager is developing a project charter, but a key stakeholder is failing to provide information about the expected…|
|`pmp-p4-210`|export-batch-2|single|agile|3|A critical software development project has been in development for the past six sprints. A demonstration for stakeholders took…|
|`pmp-p4-211`|export-batch-2|single|predictive|2|A complex project has multiple participants. The project manager agrees with the team that they need to better understand the…|
|`pmp-p4-212`|export-batch-2|single|predictive|2|A key stakeholder refuses to sign off on a product because it does not meet the expectations outlined in the project scope.|
|`pmp-p4-213`|export-batch-2|single|predictive|3|While executing a project that is nearly behind schedule, a project manager must commission a new piece of equipment. Two local…|
|`pmp-p4-214`|export-batch-2|single|predictive|1|A project manager compiling a list of project stakeholders discovers an interested stakeholder with little to no authority.|
|`pmp-p4-215`|export-batch-2|single|predictive|2|During a project's execution, a previously unidentified stakeholder within the customer's organization requests a change that…|
|`pmp-p4-216`|export-batch-2|single|predictive|2|A new project manager joins a project during the delivery of a critical milestone. This project has multiple stakeholders, and…|
|`pmp-p4-217`|export-batch-2|single|predictive|2|A project manager must ensure stakeholder responsibilities are clear, concise, and promote successful completion while…|
|`pmp-p4-218`|export-batch-2|single|hybrid|3|A project manager is working on a hybrid project. The team performance has been suffering and multiple team members have…|
|`pmp-p4-219`|export-batch-2|single|agile|3|A key project stakeholder showed interest in the beginning of a complex agile project but has become less involved as the sprint…|
|`pmp-p4-220`|export-batch-2|single|predictive|2|A key stakeholder's input is essential to a project's success. However, the stakeholder is always late on critical project…|
|`pmp-p4-221`|export-batch-2|single|predictive|3|During the initial planning phase of a complex development project, a project team has identified several stakeholders, including…|
|`pmp-p4-222`|export-batch-2|single|hybrid|2|A project manager is managing a hybrid project. After attending an iteration review, a stakeholder tells the project manager that…|
|`pmp-p4-223`|export-batch-2|single|predictive|2|An executive stakeholder has crucial decision power on the execution and success of a project.|
|`pmp-p4-224`|export-batch-2|single|predictive|1|After stakeholder data and information collection is complete, an analysis of the collected data begins. The power/interest…|
|`pmp-p4-225`|export-batch-2|single|hybrid|2|A project manager is leading an environmental project and realizes that they missed an influential foreign stakeholder. After the…|
|`pmp-p4-226`|export-batch-2|single|predictive|1|A project manager is leading a project with seven key stakeholders.|
|`pmp-p4-227`|export-batch-2|single|hybrid|2|A project manager sends out requests for regular status meetings and daily standup meetings during the project setup phase.…|
|`pmp-p4-228`|export-batch-2|single|predictive|2|During a project steering committee meeting, the project manager reports that a formerly supportive key team member has become…|
|`pmp-p4-229`|export-batch-2|single|predictive|2|A project manager has just completed the initiation phase of a project and needs to identify and analyze the stakeholders. The…|
|`pmp-p4-230`|export-batch-2|single|agile|2|A long-term agile project has just reached its midway mark and a new project manager is appointed to take over. Additional to the…|
|`pmp-p4-231`|export-batch-2|single|hybrid|2|A project manager is leading a complex initiative with diverse stakeholders and must cultivate strong stakeholder relationships.…|
|`pmp-p4-232`|export-batch-2|single|predictive|2|A project manager has identified stakeholders for a software development project: the sales team will be the primary users of the…|
|`pmp-p4-233`|export-batch-2|single|predictive|2|A project is experiencing delays because cultural differences are causing misunderstandings among some stakeholders.|
|`pmp-p4-234`|export-batch-2|single|agile|3|A project manager for an important agile project has a key stakeholder who was actively involved at the beginning of the project.…|
|`pmp-p4-235`|export-batch-2|single|predictive|2|Midway through a project, the project manager identifies new stakeholders. Each of these new stakeholders plays a different…|
|`pmp-p4-236`|export-batch-2|single|predictive|1|A project is in development when a functional manager leaves the company and is no longer involved with the project.|
|`pmp-p4-237`|export-batch-2|single|predictive|1|A project manager in a global organization is responsible for a project involving six different companies. Midway through the…|
|`pmp-p4-238`|export-batch-2|single|predictive|2|A project manager is having difficulty managing stakeholders because the stakeholder register does not distinguish how aware of…|
|`pmp-p4-239`|export-batch-2|single|agile|2|A project manager is attempting to engage project stakeholders in a new project and has invited them to daily standup meetings.…|
|`pmp-p4-240`|export-batch-2|single|agile|2|An agile project that the current project manager has been managing for three years is replacing one of the stakeholders with one…|
|`pmp-p4-241`|export-batch-2|single|predictive|2|A project manager is leading a complex infrastructure project that is vital for the company's growth. During the project's…|
|`pmp-p4-242`|export-batch-2|single|agile|3|During the execution of a project, the finance team identified that they were not involved in building the business case for the…|
|`pmp-p4-243`|export-batch-2|single|agile|2|An agile project manager is leading a new software system development project that is in the user acceptance testing phase. The…|
|`pmp-p4-244`|export-batch-2|multi|hybrid|2|A large transformation project that requires cross-functional effort from various business teams is in progress. (Choose two)|
|`pmp-p4-245`|export-batch-2|single|agile|2|A director is acting as the product owner for a government software project. The director is repeatedly unable to engage with the…|
|`pmp-p4-246`|export-batch-2|multi|predictive|2|A project is close to completion when an unidentified and influential stakeholder appears and threatens to put the project in…|
|`pmp-p4-247`|export-batch-2|single|agile|1|At what point in an agile project should a project manager engage stakeholders?|
|`pmp-p4-248`|export-batch-2|single|predictive|2|A project manager is managing a construction project that is 40% through the execution phase. The project sponsor has asked the…|
|`pmp-p4-249`|export-batch-2|single|predictive|1|A project team is creating a stakeholder engagement assessment matrix to govern stakeholder management activities during project…|
|`pmp-p4-250`|export-batch-2|single|agile|2|A project manager is concerned about delivering the planned business value because the product owner is not engaged.|
|`pmp-p4-251`|export-batch-2|single|hybrid|3|Several items remain in the Kanban blocked column despite the team’s efforts to address them. The retrospective reveals that all…|
|`pmp-p4-252`|export-batch-2|single|predictive|2|A functional manager who is also a project stakeholder has time constraints and delegates project responsibilities to someone…|
|`pmp-p4-253`|export-batch-2|single|agile|3|An R&D executive, who is a project stakeholder, occasionally visits the agile team’s work area to give instructions about urgent…|
|`pmp-p4-254`|export-batch-2|single|predictive|2|A new project team is setting early priorities, and they plan to gain the customer's trust by delivering value as quickly as…|
|`pmp-p4-255`|export-batch-2|single|predictive|2|A project manager overseeing the launch of a new e-commerce platform discovers that the finance department has concerns about not…|
|`pmp-p4-256`|export-batch-2|single|predictive|3|A project manager is overseeing the construction of a new office building. A key stakeholder complains that they never received…|
|`pmp-p4-257`|export-batch-2|single|predictive|2|During a pivotal project phase, it was revealed that critical stakeholders did not attend a meeting focused on confirming the…|
|`pmp-p4-258`|export-batch-2|single|predictive|2|At the beginning of a new environmental conservation project, a key stakeholder expresses concerns over the project's approach,…|
|`pmp-p4-259`|export-batch-2|single|hybrid|2|In the development phase of an international public health project, a project manager faces skepticism from government partners…|
|`pmp-p4-260`|export-batch-2|single|predictive|2|As a project manager overseeing the development of a new line of eco-friendly packaging materials, you encounter pushback from…|
|`pmp-p4-261`|export-batch-2|single|predictive|3|Your stakeholders seem to be engaged, however there are frequent changes to scope and requirements, causing delays. Issues are…|
|`pmp-p4-262`|export-batch-2|single|predictive|2|A project manager is leading the development of a new public park in a densely populated urban area. Community leaders have…|
|`pmp-p4-263`|export-batch-2|single|predictive|2|A project manager is leading the development of a new public library. During the planning phase, it comes to light that the…|
|`pmp-p4-264`|export-batch-2|single|predictive|2|At the outset of an initiative to introduce smart city technologies, it's discovered that the input from a crucial environmental…|
|`pmp-p4-265`|export-batch-2|single|predictive|2|You are delivering a critical change and several key stakeholders in your organisation have added seemingly unnecessary steps for…|
|`pmp-p4-266`|export-batch-2|single|predictive|2|You are analysing your stakeholder through an impact over influence chart and have found more than 100 different groups of…|
|`pmp-p4-267`|export-batch-2|single|predictive|2|You are working on a change that includes stakeholders at all levels from front-line workers to senior executives. After…|
|`pmp-p4-268`|export-batch-2|single|predictive|2|The functional manager raises a concern that they have not been engaged correctly and the program is missing some key…|
|`pmp-p4-269`|export-batch-2|single|predictive|2|Your project has been in progress for the past six months. Someone you have not met before approaches you and says they have not…|
|`pmp-p4-270`|export-batch-2|single|predictive|2|You have been working with the stakeholders on your project for some time, and the communication has been getting more and more…|
|`pmp-p4-271`|export-batch-2|single|predictive|2|You have been working on a project in your organisation for some time. In the latest working group meeting, the project sponsor…|
|`pmp-p4-272`|export-batch-2|single|predictive|1|You are creating a high level project charter which has now been approved. The Project Sponsor expects you to get to work…|
|`pmp-p4-273`|export-batch-2|single|predictive|2|The project charter has been approved, you have identified the stakeholders you will be working with and who will be impacted. As…|
|`pmp-p4-274`|export-batch-2|single|predictive|2|Your project charter has been approved and your stakeholder analysis reveals multiple impacted highly paid executives with the…|
|`pmp-p4-275`|export-batch-2|single|agile|2|You are at the beginning of a new iteration and about to plan and decide what gets worked on in the upcoming sprint. You decide…|
|`pmp-p4-276`|export-batch-2|single|hybrid|3|During a retrospective, your team advise that a high ranking executive from another part of the business keep approaching them to…|
|`pmp-p4-277`|export-batch-2|single|predictive|1|You are working on a project to deliver a major system change to retail workers in a large department store. You have worked…|
|`pmp-p4-278`|export-batch-2|single|predictive|1|You are in the initiating stages of a project, and have been given an initial project team. Your team are gathering the project…|

### people-5 · Expectation alignment — 93 items

*Bring differing stakeholder expectations onto common ground.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p5-001`|core|single|predictive|3|Your sponsor has told the board the new system will go live in March. Your schedule, which the sponsor approved, shows a June…|
|`pmp-p5-002`|core|single|agile|4|A sponsor new to iterative delivery has asked for a fixed scope, fixed date and fixed budget commitment for a twelve-month…|
|`pmp-p5-003`|core|matching|predictive|3|Prompts: Two directors want incompatible things f… / A user group expects a reporting capabil… / A regional head expects a March cutover;…|
|`pmp-p5-004`|core|single|agile|2|Your product owner, wanting to close a deal, told a prospective customer that a bulk import feature would ship in the next…|
|`pmp-p5-005`|core|single|hybrid|4|A shared platform is being rolled out to two divisions. The retail division has been told it will get a phased rollout with a…|
|`pmp-p5-006`|core|single|predictive|3|Your sponsor has served notice to vacate the company's leased data centre at the end of March, the date set by the lease break…|
|`pmp-p5-007`|core|pulldown|hybrid|3|Prompts: Two directors agree on what the project… / A regulator, a sponsor and an operations…|
|`pmp-p5-008`|core|single|predictive|3|Your operations director believes the new warehouse system arrives in March, because that is what she was told at a steering…|
|`pmp-p5-009`|core|single|agile|4|Your team has averaged forty points a sprint over six sprints, with a range of thirty-one to forty-nine. The remaining backlog is…|
|`pmp-p5-010`|core|matching|predictive|2|Prompts: At initiation, before any decomposition… / After planning is complete and the sched… / Two weeks out from a milestone, with the…|
|`pmp-p5-011`|core|single|agile|3|Your programme has been called Single Customer View since it was funded. It builds a data layer that lets existing systems query…|
|`pmp-p5-012`|core|single|predictive|4|A warehouse automation project will finish on time and on budget. The operations director who sponsored it has moved on, and her…|
|`pmp-p5-013`|core|single|hybrid|4|Your steering group approved delivering a minimum viable product in June before scaling up. Preparing the June demonstration, you…|
|`pmp-p5-014`|core|matching|predictive|3|Prompts: The finance director's savings date, whi… / The depot managers' manual override, rem… / The regulator's evidence expectation, wh…|
|`pmp-case1-004`|case-01|single|predictive|3|Grace Adeyemi has said she will not release the next shutdown window until she sees a plan she believes. Both earlier shutdowns…|
|`pmp-case4-005`|case-04|single|predictive|4|Anthony Burke, the major donor, has asked to approve the gallery layout and for the gallery to carry his family's name. Neither…|
|`pmp-p5-101`|people-5|single|predictive|3|Your project is replacing a hospital pathology laboratory's information system under a fixed-price contract. At a progress…|
|`pmp-p5-102`|people-5|single|agile|1|During a sprint review for a travel company's booking app, the head of customer service watches the new itinerary screen and says…|
|`pmp-p5-103`|people-5|single|predictive|3|Your firm is building a teaching block for a university under a standard construction contract. Practical completion is due in…|
|`pmp-p5-104`|people-5|single|agile|3|Your team of six developers on a payments app has delivered a steady 30 points a sprint for five sprints. The sponsor has funded…|
|`pmp-p5-105`|people-5|multi|hybrid|3|Two district councils have merged and are jointly sponsoring a new resident contact system. One co-sponsor measures success by…|
|`pmp-p5-106`|people-5|single|predictive|4|You are managing a route-optimisation system for a logistics company. The business case, owned by the sponsor, forecasts €400,000…|
|`pmp-p5-107`|people-5|single|agile|5|Your team is building a model that routes motor insurance claims either to fast-track settlement or to an investigator. The…|
|`pmp-p5-108`|people-5|matching|hybrid|3|Prompts: The senior curator expected 3D scans of… / The plan relies on volunteers being avai… / The web team and the curators disagreed…|
|`pmp-p5-109`|people-5|single|agile|2|You lead an agile team building a members' app for an agricultural co-operative. Progress is shown on a live board, and every…|
|`pmp-p5-110`|people-5|multi|hybrid|3|Your project is replacing an airline's maintenance planning system, built iteratively but released through a single planned…|
|`pmp-p5-111`|people-5|single|predictive|2|You are managing a billing system upgrade for a water company. Three weeks ago the change control board approved removing…|
|`pmp-p5-112`|people-5|single|agile|4|Your team is building a stock-scanning app for a frozen food distributor. At the third sprint review, pickers said they expected…|
|`pmp-p5-113`|people-5|single|predictive|3|During requirements sign-off for a hospital's electronic prescribing system, the director of nursing sent her deputy, who agreed…|
|`pmp-p5-114`|people-5|fillblank|predictive|3|The sponsor of a laboratory relocation has been telling the board it will take 20 weeks, which is the team's most likely…|
|`pmp-p5-115`|people-5|hotspot|agile|3|The sponsor of a council's planning portal expects release at sprint 10, based on the original 250-point scope and a velocity of…|
|`pmp-p5-116`|people-5|matching|hybrid|2|Prompts: Counter staff who find it hard to descri… / Twenty-eight branch managers spread acro… / Three senior heads of service whose expe…|
|`pmp-p5-117`|people-5|pulldown|hybrid|3|Prompts: The plant director assumes every softwar… / The finance manager expects a formal cha…|
|`pmp-p5-118`|people-5|single|agile|5|You are the agile lead for a team building a ticketing platform for a theatre group. The product owner expects every sprint…|
|`pmp-p5-119`|people-5|single|predictive|1|You are managing an electrical upgrade at a food processing plant, carried out in planned shutdowns. The communications…|
|`pmp-p5-120`|people-5|multi|predictive|4|Your team is delivering a pensions administration system to a client in Dublin under a time-and-materials contract. During a…|
|`pmp-p5-121`|people-5|single|predictive|2|You are managing the refurbishment of 120 guest bathrooms for a hotel group in Edinburgh. The approved specification and budget…|
|`pmp-p5-122`|people-5|single|predictive|3|Your project delivered a berth-booking portal for a container port. It passed every user acceptance test against the signed-off…|
|`pmp-p5-123`|people-5|single|predictive|3|Your company is building a bespoke permit-management module for a city council under a fixed-price contract, and you are the…|
|`pmp-p5-124`|people-5|single|predictive|4|Your £6 million rail depot upgrade carries a £300,000 contingency reserve tied to risks in the risk register. The cost management…|
|`pmp-p5-125`|people-5|single|hybrid|5|Your hybrid programme is rolling out a new outpatient booking system to 14 hospital clinics in waves of two. The pilot clinic,…|
|`pmp-p5-126`|people-5|single|agile|1|You are the agile lead for a newly formed team split between Lisbon and Kraków, building a booking platform for a chain of dental…|
|`pmp-p5-127`|people-5|single|agile|3|Your agile team is building an inspections app for a housing association. The product owner, a senior surveyor, has been given…|
|`pmp-p5-128`|people-5|single|agile|4|Two agile teams work on a utility company's customer platform, each with its own product owner. The billing team's plan for…|
|`pmp-p5-129`|people-5|single|hybrid|2|Your hybrid project is replacing a university's payroll system: configuration is done in fortnightly iterations, followed by a…|
|`pmp-p5-130`|people-5|single|agile|4|Your agile team is building a benefits-advice app for a charity, funded by a foundation grant that lists six features in its…|
|`pmp-p5-131`|people-5|single|hybrid|3|Your hybrid programme is implementing a cloud HR system for a logistics group. The steering group approved an 'adopt, not adapt'…|
|`pmp-p5-132`|people-5|single|hybrid|1|Your hybrid project is replacing a brewing company's production-scheduling software. The equipment interfaces are baselined;…|
|`pmp-p5-133`|people-5|multi|hybrid|3|Your hybrid project is introducing new warehouse workflows for a distribution company in the Netherlands. The handheld hardware…|
|`pmp-p5-134`|people-5|multi|predictive|3|Midway through a terminal expansion at a regional airport, a new head of security joins the steering group. She tells you she…|
|`pmp-p5-135`|people-5|multi|agile|4|One agile team serves both the retail and commercial divisions of a bank, with a single product owner ordering the backlog by…|
|`pmp-p5-136`|people-5|matching|predictive|2|Prompts: The head of the structures team says the… / The managing partner says there must be… / The office manager says a colleague who…|
|`pmp-p5-137`|people-5|matching|agile|3|Prompts: A regional manager believes his delivery… / A marketing director expects a firm poin… / The sponsor's chief of staff expects the…|
|`pmp-p5-138`|people-5|pulldown|predictive|3|Prompts: Residents expect no work on Sundays, and… / Residents expect the street to stay open… / Residents expect bin collections to cont…|
|`pmp-p5-139`|people-5|fillblank|predictive|3|A supplier's contract for a new contact-centre telephony platform guarantees 99.5% availability, measured over each 30-day month…|
|`pmp-p5-140`|people-5|hotspot|agile|3|The roadmap for a ferry operator's booking app is shown with each item's current state. The commercial director wants to write…|
|`pmp-p5-1`|export-batch-1|single|agile|4|An agile project is in its first of 12 iterations, and each iteration lasts two weeks. In the first iteration review, the…|
|`pmp-p5-2`|export-batch-1|single|agile|3|The product owner has asked the project manager and the agile team to demonstrate the completed functionality to key project…|
|`pmp-p5-3`|export-batch-1|single|agile|3|A company has started doing agile projects. The project sponsor is frustrated and does not understand the new requirements…|
|`pmp-p5-4`|export-batch-1|single|predictive|3|A company used to deliver technical services to customers using external labor. When a new project manager arrived, the team…|
|`pmp-p5-5`|export-batch-1|single|agile|3|The project manager of an agile project notices that many stakeholders are skipping iteration planning and review meetings. Some…|
|`pmp-p5-6`|export-batch-1|single|predictive|3|An organizational transformation project has two workstreams running in parallel between corporate headquarters and an overseas…|
|`pmp-p5-7`|export-batch-1|single|predictive|3|While gathering requirements for the project scope, the project manager realizes that some of the stakeholder requirements have a…|
|`pmp-p5-201`|export-batch-2|single|agile|2|An agile team is planning for a 2-week iteration. In the middle of the last few iterations, the product owner made changes and…|
|`pmp-p5-202`|export-batch-2|single|hybrid|2|In a software development project, a stakeholder wants to add a major requirement, which they feel will be a great benefit to the…|
|`pmp-p5-203`|export-batch-2|single|predictive|2|A project manager has been assigned to a new project with multiple high-level stakeholders. Each stakeholder has provided their…|
|`pmp-p5-204`|export-batch-2|single|agile|3|A senior business stakeholder with many years of experience is pressuring an agile team to deliver most of the promised…|
|`pmp-p5-205`|export-batch-2|single|predictive|3|Midway through project execution, several stakeholders raise concerns about team performance and delivery. The project manager…|
|`pmp-p5-206`|export-batch-2|single|predictive|3|A project manager is working on a complex project that includes many stakeholders with conflicting priorities.|
|`pmp-p5-207`|export-batch-2|single|agile|3|Following the start of a sprint, a key stakeholder constructed an email to a project team voicing displeasure of the work that…|
|`pmp-p5-208`|export-batch-2|single|predictive|3|A key supplier has become unavailable and the project manager has found a replacement that will meet project approval. Bringing…|
|`pmp-p5-209`|export-batch-2|single|predictive|2|In the early stages of a project, two key stakeholders have different opinions about a project's goals.|
|`pmp-p5-210`|export-batch-2|single|predictive|2|While preparing a project charter for a new project, a project manager reviews lessons learned from previous projects. The…|
|`pmp-p5-211`|export-batch-2|multi|hybrid|3|A project manager is working on a complex project. The project includes many stakeholders with conflicting priorities. (Choose…|
|`pmp-p5-212`|export-batch-2|single|predictive|3|A project manager is working on a complex, political project with many stakeholders having conflicting priorities. Senior…|
|`pmp-p5-213`|export-batch-2|single|predictive|2|A project manager is working on a complex project with multiple stakeholders and competing priorities.|
|`pmp-p5-214`|export-batch-2|single|predictive|2|A project manager is working on a complex project with multiple stakeholders and competing priorities. During a meeting with the…|
|`pmp-p5-215`|export-batch-2|single|predictive|3|A project manager is working on a complex project that requires expertise in negotiation skills.|
|`pmp-p5-216`|export-batch-2|single|predictive|3|A project manager is working on a complex project with many stakeholders that have conflicting priorities. Senior management…|
|`pmp-p5-217`|export-batch-2|single|agile|3|After monitoring a product’s development after the first three releases, the product owner wants to launch a product even though…|
|`pmp-p5-218`|export-batch-2|single|predictive|3|During a project status meeting, the project manager discovers that stakeholders have different expectations that are not…|
|`pmp-p5-219`|export-batch-2|single|predictive|3|The company's top executives are embarking on a pivotal project that holds immense importance for the organization's future. This…|
|`pmp-p5-220`|export-batch-2|single|predictive|2|A project manager has been assigned to a new project. The client has mentioned that the project's inception is based on ideas and…|
|`pmp-p5-221`|export-batch-2|single|agile|2|A stakeholder that is not experienced with agile has emailed the project team expressing frustration and displeasure with their…|
|`pmp-p5-222`|export-batch-2|single|predictive|2|A multinational company with a cross-border project wants to implement new software that will impact several departments. The…|
|`pmp-p5-223`|export-batch-2|single|predictive|3|A project team lead meets with the project manager before a scheduled kick-off meeting. The team lead is worried that…|
|`pmp-p5-224`|export-batch-2|single|predictive|3|A project manager is working on an complex project with multiple stakeholders and competing priorities. Each stakeholder wants to…|
|`pmp-p5-225`|export-batch-2|single|agile|3|A project manager is leading a key flagship solution initiative prioritizing agility, market adaptation, and rapid response to…|
|`pmp-p5-226`|export-batch-2|single|predictive|2|A project manager learns that a few stakeholders have a disagreement regarding the results of one project deliverable.|
|`pmp-p5-227`|export-batch-2|single|predictive|3|A project manager is planning the construction of a new public library. The community and project stakeholders are having…|
|`pmp-p5-228`|export-batch-2|single|agile|2|You are working on an Agile project where the product business representative is pushing for more and more features to be…|
|`pmp-p5-229`|export-batch-2|single|agile|2|While meeting with the customers or business owners, they introduce several pieces of new functionality into the product backlog…|
|`pmp-p5-230`|export-batch-2|single|hybrid|2|Your project has just had its first release of a deliverable, which has taken 4 months to deliver. The team showcased it to the…|

### people-6 · Customer expectations — 85 items

*Track what internal and external customers expect, and respond as it moves.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p6-001`|core|single|hybrid|3|Halfway through a customer-facing implementation, the client's business has been reorganised and their new operations lead wants…|
|`pmp-p6-002`|core|single|agile|2|At a sprint review for an expenses tool, the customer representative says the new receipt-matching feature works exactly as…|
|`pmp-p6-003`|core|single|predictive|3|Halfway through a twelve-month customer portal build, your client's own market has shifted and their competitors have launched…|
|`pmp-p6-004`|core|single|agile|2|An internal operations team is the customer for a scheduling tool. For four consecutive sprints they have accepted the demo, then…|
|`pmp-p6-005`|core|single|agile|3|Six sprints into a customer portal, usage data from the beta shows that the feature the customer described as essential is barely…|
|`pmp-p6-006`|core|single|hybrid|4|Your customer's operations director escalates to your managing director, saying the delivered reporting module is unusable…|
|`pmp-p6-007`|core|multi|predictive|3|Your project's customer is an internal department that will run the system after handover. Their operations manager has been…|
|`pmp-p6-015`|core|single|predictive|2|Your firm is supplying four switchgear panels to a data-centre operator under a fixed-price contract that requires a factory…|
|`pmp-p6-016`|core|multi|agile|3|Your team develops a timetabling system for a further-education college, and the first release went live five weeks ago. The…|
|`pmp-p6-010`|core|multi|agile|3|Three sprints into an implementation, your new customer is increasingly unhappy. Reviewing the sales cycle, you find a pre-sales…|
|`pmp-p6-011`|core|single|predictive|2|Three months into a twelve-month customer portal build, your client's marketing head asks for a change to the visual design that…|
|`pmp-p6-012`|core|single|hybrid|4|A hybrid platform serves two internal customers. Claims processing needs the release in March to meet a regulatory commitment.…|
|`pmp-p6-013`|core|single|agile|2|Your team demonstrates every two weeks to a customer representative who has begun comparing each increment unfavourably with a…|
|`pmp-p6-014`|core|single|predictive|3|Two weeks before go-live on a system built for an internal department, the department head mentions in passing that she expects…|
|`pmp-case2-002`|case-02|single|agile|3|Older residents repeatedly struggle at the vehicle registration step in usability sessions. Malik wants to hold the release scope…|
|`pmp-case3-010`|case-03|single|hybrid|2|Clinicians on the wards have been told little about the cutover at Site A and expect turnaround times for test results to stay as…|
|`pmp-case5-005`|case-05|single|hybrid|3|Since the first centre moved onto the new platform, customers have been waiting longer on the phone and complaints have risen.…|
|`pmp-p6-101`|people-6|single|predictive|1|You are managing the refurbishment of a 120-room hotel under a fixed-price contract with an agreed specification. During a site…|
|`pmp-p6-102`|people-6|single|agile|2|Your team is building a stock-management app for a hospital pharmacy. The product owner comes from the hospital's IT department,…|
|`pmp-p6-103`|people-6|multi|predictive|3|You are managing a three-year bridge strengthening programme for a county council. At the annual review every milestone is on…|
|`pmp-p6-104`|people-6|single|predictive|2|Your agency is building a public information website for a government department under a contract requiring conformance with WCAG…|
|`pmp-p6-105`|people-6|single|agile|3|You lead an agile team building a claims app for an insurance broker. The broker's operations manager, who acts as product owner,…|
|`pmp-p6-106`|people-6|matching|predictive|3|Prompts: The extract rate measured at two fume cu… / The lab manager says she expected the sy… / The client asks for a third fume cupboar…|
|`pmp-p6-107`|people-6|single|predictive|3|You are delivering a bespoke packaging line to a food manufacturer for €2.4 million. Acceptance tests passed last month and the…|
|`pmp-p6-108`|people-6|single|agile|3|Your team is building an online banking feature for a building society. Its product owner has started insisting on approving a…|
|`pmp-p6-109`|people-6|single|predictive|3|You are managing the fit-out of three office floors for a law firm. The contract delegates to you authority to agree variations…|
|`pmp-p6-110`|people-6|fillblank|hybrid|3|After rolling out a new field-service system to a water utility, your team runs a six-week hypercare period. The utility expects…|
|`pmp-p6-111`|people-6|single|hybrid|4|Your hybrid team is delivering a loyalty app for a fashion retailer. The retailer's marketing lead has a national campaign…|
|`pmp-p6-112`|people-6|single|agile|5|You lead an agile team building a route-planning platform for a logistics firm. Its chief operating officer tells you she has…|
|`pmp-p6-113`|people-6|multi|agile|4|Your team is building a case-management tool for a utility's contact centre. Agents in the pilot group have started messaging…|
|`pmp-p6-114`|people-6|single|hybrid|1|Yesterday your team released an update to the online booking service of a chain of physiotherapy clinics. This morning the…|
|`pmp-p6-115`|people-6|pulldown|agile|2|Prompts: The head of libraries wants to know, spr… / Librarians say search must be 'fast', bu… / Branch staff keep asking about features…|
|`pmp-p6-116`|people-6|single|predictive|4|Your company is implementing a records system for a regional health authority. The contract, managed by the authority's contracts…|
|`pmp-p6-117`|people-6|hotspot|hybrid|3|A council is piloting self-service kiosks in its libraries. In interviews, no user mentioned printed loan receipts. When receipts…|
|`pmp-p6-118`|people-6|single|agile|4|Your team supports a university's student-records system and has moved from monthly to weekly releases, as the registry director…|
|`pmp-p6-119`|people-6|multi|predictive|3|Your consultancy is designing a flood defence wall for an environment agency under an agreed brief that covers capacity,…|
|`pmp-p6-120`|people-6|matching|hybrid|3|Prompts: Call-centre agents say the system 'feels… / Staff in 40 branches need to say which o… / The sales director and the service direc…|
|`pmp-p6-121`|people-6|single|predictive|1|Your software company is three months into implementing a patient-administration system for a hospital trust under a fixed-scope…|
|`pmp-p6-122`|people-6|single|agile|2|Your team is building a mobile inspection app for a housing association. At the last review the association's head of property…|
|`pmp-p6-123`|people-6|single|hybrid|2|Your hybrid team is replacing the online ticketing system for a science museum. To test the booking journey, the designer built a…|
|`pmp-p6-124`|people-6|single|predictive|3|Your firm has taken over a university's building-maintenance contract from a long-standing incumbent. The new contract requires…|
|`pmp-p6-125`|people-6|single|predictive|4|Two months after handing over a warehouse management system to a furniture manufacturer, its interface to a carrier's booking…|
|`pmp-p6-126`|people-6|single|agile|3|Your team builds features for an airport operator's passenger app. Stories accepted at each sprint review go into a release that…|
|`pmp-p6-127`|people-6|single|agile|3|You are the project manager for an agile team building an appointment app for a chain of veterinary practices. The product owner,…|
|`pmp-p6-128`|people-6|single|hybrid|4|Your team delivers quarterly releases of a booking system for a ferry operator. For the first two releases, as goodwill, your…|
|`pmp-p6-129`|people-6|single|agile|4|Your team builds a candidate-matching platform for a recruitment agency. At the end of each sprint the agency's head of…|
|`pmp-p6-130`|people-6|single|predictive|5|You are the internal project manager for a new vehicle-scheduling system at a regional bus company. The business case, owned by…|
|`pmp-p6-131`|people-6|single|hybrid|4|You are managing the refurbishment of a charity's head office on a £1.2 million budget. At the start, its chief executive said…|
|`pmp-p6-132`|people-6|single|predictive|2|Six weeks before migrating a parcel firm's warehouse system to a new data centre, you review the cutover plan with its site…|
|`pmp-p6-133`|people-6|multi|agile|3|Your team is building a training platform for a pharmaceutical company. Its learning and development director, the main customer…|
|`pmp-p6-134`|people-6|multi|agile|3|Your agile team works on a time-and-materials contract for a payments start-up, billed at €52,000 a month. After six months, the…|
|`pmp-p6-135`|people-6|multi|predictive|3|You are managing a £3.1 million extension to a secondary school, built while the school stays open. The contract allows noisy…|
|`pmp-p6-136`|people-6|matching|predictive|2|Prompts: The monthly payroll run for 18,000 staff… / Processing costs should fall by 30% with… / The finance director wants to hear withi…|
|`pmp-p6-137`|people-6|matching|hybrid|3|Prompts: Were tenants satisfied with how their in… / How much effort did tenants have to put… / Would tenants recommend the association'…|
|`pmp-p6-138`|people-6|pulldown|agile|3|Prompts: Midway through a sprint, the clubs' memb… / During a review, the membership lead poi… / The clubs' procurement officer asks for…|
|`pmp-p6-139`|people-6|fillblank|hybrid|3|An online grocer's operations director is about to sign a hosting contract offering 99.5% monthly availability. She says this…|
|`pmp-p6-140`|people-6|hotspot|predictive|3|You are delivering a warehouse automation project for a pharmaceutical distributor. The diagram is a timeline of the monthly…|
|`pmp-p6-1`|export-batch-1|single|agile|3|A company has hired a project manager to implement an adaptive project framework. The project has made some progress, but the…|
|`pmp-p6-2`|export-batch-1|single|agile|3|An agile team is developing a product with strict budget constraints. The customer is requesting a new independent feature be…|
|`pmp-p6-3`|export-batch-1|single|agile|3|Even though the customer is receiving sprint velocity and results reports from the project manager by email, escalations to the…|
|`pmp-p6-4`|export-batch-1|single|agile|3|A client has contracted a company to manage a large project in an agile environment with many subcontractors. The client is…|
|`pmp-p6-201`|export-batch-2|single|agile|2|A project manager is involved in a project where requirements are constantly evolving.|
|`pmp-p6-202`|export-batch-2|single|predictive|2|As part of a project deliverable, a project manager demonstrates how to use one of the products to a subset of a client's…|
|`pmp-p6-203`|export-batch-2|single|hybrid|2|In a hybrid project, the need for customer involvement throughout the project is not understood by the customer. As a…|
|`pmp-p6-204`|export-batch-2|single|hybrid|3|During one of the final incremental deliveries of a project, the end user informed the project leader that they are not satisfied…|
|`pmp-p6-205`|export-batch-2|single|predictive|2|A project has been closed and customer care needs to measure customer satisfaction with the deliverables.|
|`pmp-p6-206`|export-batch-2|single|agile|3|A customer is concerned that the current project deliverable will not fulfill stated project requirements.|
|`pmp-p6-207`|export-batch-2|single|agile|3|A complex technology project is in the execution phase. New technologies are added to the backlog based on customer requirements,…|
|`pmp-p6-208`|export-batch-2|single|agile|3|During a daily meeting, the project team raised an impediment that one of the business subject matter experts is frequently…|
|`pmp-p6-209`|export-batch-2|single|hybrid|3|The team informed the project leader that they are not sure of the stakeholder needs, despite the multiple collection…|
|`pmp-p6-210`|export-batch-2|single|predictive|2|An operational support project is running smoothly, but a customer consistently complains about a lack of visibility into the…|
|`pmp-p6-211`|export-batch-2|single|predictive|2|A customer requires knowing exactly the state of a product, the overall project, and the next steps planned for the next month…|
|`pmp-p6-212`|export-batch-2|single|predictive|2|Multiple stakeholders express dissatisfaction with a project deliverable.|
|`pmp-p6-213`|export-batch-2|single|agile|3|A project manager is assigned a project for a customer with a history of being demanding and unclear about requirements. The…|
|`pmp-p6-214`|export-batch-2|single|hybrid|2|A project manager is on a project with a very involved customer. The customer demands regular updates on the project, including…|
|`pmp-p6-215`|export-batch-2|single|predictive|3|During a meeting with stakeholders, the project manager received multiple concerns about the quality of the project’s…|
|`pmp-p6-216`|export-batch-2|single|agile|2|A project manager receives an email from a customer detailing reservations about the project deliverables.|
|`pmp-p6-217`|export-batch-2|single|agile|2|A customer is not satisfied with the delivered product, saying that it was not what they expected. The project manager is…|
|`pmp-p6-218`|export-batch-2|single|agile|3|A customer repeatedly complains, saying that a function developed by the development team does not seem to align with the…|
|`pmp-p6-219`|export-batch-2|single|hybrid|3|During a challenging shareholder meeting, the customer indicates that they will reject an iteration's deliverable.|
|`pmp-p6-220`|export-batch-2|single|predictive|2|A customer's resources have not completed the required training. The project manager has to consider the risk it poses to the…|
|`pmp-p6-221`|export-batch-2|single|predictive|2|A customer is performing a weekly visit to a project site to inspect the final stage of product development. The quality manager…|
|`pmp-p6-222`|export-batch-2|single|agile|2|During a one-year project, a team delivered everything as requested and defined by the product owner and met all of the sprint…|
|`pmp-p6-223`|export-batch-2|single|agile|2|While working on a long project with many changes, a project manager is informed by a team member that some users are…|
|`pmp-p6-224`|export-batch-2|multi|agile|3|A stakeholder on an agile project has attended the sprint demo where the team shows the minimum viable product (MVP) as defined…|

### people-7 · Knowledge transfer — 89 items

*Capture what the project knows and move it to whoever needs it next.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p7-001`|core|single|predictive|2|Your organisation runs a lessons-learned workshop at the end of every project and files the output in a shared drive. Reviewing…|
|`pmp-p7-015`|core|single|hybrid|3|Your hybrid programme is rebuilding a regional courier's parcel-sorting software: hardware changes follow a fixed plan and the…|
|`pmp-p7-003`|core|single|predictive|3|The only engineer who understands your legacy billing interface has resigned and works four more weeks. Her documentation is…|
|`pmp-p7-004`|core|single|hybrid|4|A vendor has run your data platform for three years under a contract ending in five months, and will be replaced by an internal…|
|`pmp-p7-016`|core|single|predictive|3|You are ten weeks from handing a new secondary school to a county council, which will run it for forty years. The council's asset…|
|`pmp-p7-006`|core|matching|hybrid|3|Prompts: The reasoning behind twelve architectura… / The step-by-step procedure for the month… / A feel for which of the client's stakeho…|
|`pmp-p7-007`|core|single|agile|3|Your agile team of eight has quietly specialised. Two members handle anything involving the payments service and always pick up…|
|`pmp-p7-017`|core|single|predictive|3|Your company is completing a new stage-machinery control system for a regional theatre under a fixed-scope contract. The…|
|`pmp-p7-009`|core|hotspot|hybrid|3|The diagram shows the route a lesson takes from the project that learns it to the project that needs it. Your organisation runs…|
|`pmp-p7-010`|core|pulldown|predictive|3|Prompts: To understand why the attempt was really… / To understand the technical constraints…|
|`pmp-p7-011`|core|single|agile|2|Your agile team of seven has worked together for two years and everything it knows lives in conversation. Two new developers…|
|`pmp-p7-012`|core|single|predictive|3|Your PMO has scheduled the knowledge capture session for your two-year platform programme in its final month, which is the…|
|`pmp-p7-013`|core|single|hybrid|3|Your programme runs five workstreams, each integrating with the same ageing customer master file. Reviewing recent technical…|
|`pmp-p7-014`|core|multi|predictive|4|Your two-year programme is scaling up and twelve contractors join over the next six weeks. The existing team of nine holds most…|
|`pmp-p7-061`|core|graphic|hybrid|3|Two developers leave your platform team in six weeks, at the end of their contracts. The skills matrix shows, for each component,…|
|`pmp-case1-006`|case-01|single|predictive|2|Dr Ines Moreau, whose hydraulic calculations underpin the filter-bank design, leaves for another client in four weeks. Much of…|
|`pmp-p7-101`|people-7|single|predictive|1|A water utility is commissioning new chemical dosing pumps at a treatment works. The manufacturer's engineer is on site for one…|
|`pmp-p7-102`|people-7|single|predictive|3|Your consultancy is bidding to upgrade signalling on a regional rail line. The prospective client asks to see the full…|
|`pmp-p7-103`|people-7|single|hybrid|3|Your hybrid project is customising a CRM platform for a housing association, with go-live in seven weeks. The sponsor asks you to…|
|`pmp-p7-104`|people-7|single|predictive|2|At the end of the design phase of a hospital car-park extension, your lessons register has eleven entries. Typical ones read…|
|`pmp-p7-105`|people-7|single|hybrid|4|On a pension administration migration, two senior analysts hold most of the knowledge of the legacy calculation rules. For three…|
|`pmp-p7-106`|people-7|single|agile|4|Your agile team building a booking platform for a chain of dental clinics uses a tool that transcribes every meeting and makes…|
|`pmp-p7-107`|people-7|single|agile|3|Your agile team owns an address-validation component used in an online insurance quote journey. A second team, building a claims…|
|`pmp-p7-108`|people-7|single|hybrid|3|A bank is rolling a new teller system out to 30 branches in six waves, using a rollout playbook baselined after a successful…|
|`pmp-p7-109`|people-7|single|predictive|3|You manage the design of a ship's ballast-water treatment system. The pump-control subsystem, designed by one team, will be…|
|`pmp-p7-110`|people-7|single|predictive|3|The sponsor has cancelled your logistics company's route-optimisation project at 60% complete because a merger has made it…|
|`pmp-p7-111`|people-7|single|agile|5|Your agile team is building a consumer lending app. Eight months ago, a compliance officer told the product owner in a corridor…|
|`pmp-p7-112`|people-7|single|agile|3|Your agile team has released the first increments of a new online billing portal for an energy supplier. The customer service…|
|`pmp-p7-113`|people-7|multi|predictive|3|A contractor is completing a new hospital outpatient wing that the trust's estates team will operate and maintain for the next…|
|`pmp-p7-114`|people-7|multi|agile|4|Your agile team of fourteen, building a logistics tracking product, has become too large to work well. You and the product owner…|
|`pmp-p7-115`|people-7|multi|hybrid|3|An insurer's in-house team is taking over a claims-triage application built by a supplier under a hybrid contract. Knowledge…|
|`pmp-p7-116`|people-7|matching|agile|2|Prompts: A new developer must learn the stable, s… / A junior analyst must learn how the seni… / Developers joining next year will need t…|
|`pmp-p7-117`|people-7|matching|predictive|4|Prompts: Registers hold almost no lessons about e… / The same ground-survey lesson has been r… / Lessons from the firm's rail depot proje…|
|`pmp-p7-118`|people-7|pulldown|hybrid|3|Prompts: A lesson shows that the organisation's s… / A lesson shows that the team's handoffs…|
|`pmp-p7-119`|people-7|fillblank|predictive|2|A laboratory instrument-validation project relies on 15 critical procedures. A skills matrix shows that 6 of them can be…|
|`pmp-p7-120`|people-7|hotspot|agile|1|In sprint 9 your agile team lost two days because a shared test database was refreshed without warning. The team wants to agree a…|
|`pmp-p7-121`|people-7|single|predictive|3|You are being moved to a larger programme and will hand your county council's road-widening scheme to a new project manager in…|
|`pmp-p7-122`|people-7|single|predictive|1|In the round-the-clock support period after a hospital's new pharmacy system went live, the overnight analyst found the interface…|
|`pmp-p7-123`|people-7|single|hybrid|4|Your hybrid project is transferring a tablet-coating process from development to a manufacturing site. A senior formulation…|
|`pmp-p7-124`|people-7|single|predictive|4|Your university library refurbishment finished six weeks late. Your draft lessons report records that the steering committee took…|
|`pmp-p7-125`|people-7|single|predictive|5|You are planning installation of a bottling line from a German supplier. The lessons register of a sister plant that bought from…|
|`pmp-p7-126`|people-7|single|hybrid|2|A local authority is replacing its 20-year-old housing benefit system using a hybrid approach. In requirements interviews,…|
|`pmp-p7-127`|people-7|single|hybrid|3|Your company is building a ticketing and membership app for a museum group under a fixed-price contract that excludes training…|
|`pmp-p7-128`|people-7|single|hybrid|3|Your UK team is handing a new warehouse control system to the client's operations staff at a distribution centre near Valencia.…|
|`pmp-p7-129`|people-7|single|agile|4|Your agile team at an energy-trading firm started mob programming three sprints ago because only one developer understood the…|
|`pmp-p7-130`|people-7|single|agile|2|A developer who joined your agile team at a travel booking start-up two weeks ago has found nine steps in the onboarding guide…|
|`pmp-p7-131`|people-7|single|agile|3|Your agile team maintains an appointment-booking API used by twelve partner veterinary practices. Partners regularly raise…|
|`pmp-p7-132`|people-7|single|agile|3|Leadership has decided, for cost reasons, that the next five developers on your agile team will be hired in Manila, seven hours…|
|`pmp-p7-133`|people-7|multi|agile|3|A data science group has built a demand-forecasting model for a grocery chain as a set of notebooks. Your agile team will put it…|
|`pmp-p7-134`|people-7|multi|predictive|4|Funding for the detailed design of a coastal desalination plant has been frozen for nine months pending a government spending…|
|`pmp-p7-135`|people-7|multi|hybrid|3|Your hybrid project is rolling out a new case-management system to 600 probation officers across twelve offices over three…|
|`pmp-p7-136`|people-7|matching|predictive|3|Prompts: A senior geotechnical engineer answers q… / Site teams say they would use lessons fr… / A specialist subcontractor declines to s…|
|`pmp-p7-137`|people-7|matching|agile|3|Prompts: Testers embedded in six different teams… / Your team needs a small change to a libr… / One team has found a build configuration…|
|`pmp-p7-138`|people-7|pulldown|predictive|3|Prompts: The embankment design assumes the local… / The team has found that the environmenta… / A subcontractor's foreman has explained…|
|`pmp-p7-139`|people-7|fillblank|predictive|2|Since handover of a new fleet-maintenance scheduling system, a bus operator's depot staff raise about 60 queries a month to your…|
|`pmp-p7-140`|people-7|hotspot|agile|2|Your agile team's knowledge map lists each component it owns, how many developers can change it safely, how often it has changed…|
|`pmp-p7-1`|export-batch-1|single|predictive|3|A project manager is working on a new healthcare management application. One of the team members will be leaving the organization…|
|`pmp-p7-2`|export-batch-1|single|predictive|2|During project execution, the project sponsor decides to replace a subject matter expert (SME) on the project.|
|`pmp-p7-3`|export-batch-1|single|agile|4|A group of agile team members have just completed in-person training for a number of critical and complex organizational…|
|`pmp-p7-201`|export-batch-2|single|predictive|2|A multi-year project is underway when a key resource announces that they are retiring. The project manager is concerned about the…|
|`pmp-p7-202`|export-batch-2|single|predictive|3|A project manager has just taken over a 5-year project that is at the midpoint. At the steering committee meeting, the CEO asked…|
|`pmp-p7-203`|export-batch-2|single|agile|2|A project manager working in an agile environment realizes that some new team members are having difficulty understanding how…|
|`pmp-p7-204`|export-batch-2|single|predictive|2|In a project where a third-party provider is implementing a custom solution, the project manager must ensure that the local team…|
|`pmp-p7-205`|export-batch-2|single|agile|2|A dispersed project team has been working together on a long-term development project. The team just finished an iteration when a…|
|`pmp-p7-206`|export-batch-2|single|predictive|3|A new project manager is hired to replace a project manager who was recently transferred to another project. The team members…|
|`pmp-p7-207`|export-batch-2|single|predictive|2|An events company is planning a new event for a client that holds biannual conferences. The project is being managed by a new…|
|`pmp-p7-208`|export-batch-2|single|predictive|2|In a functional organization, a product analyst is assigned to a short-duration application development effort. The analyst…|
|`pmp-p7-209`|export-batch-2|single|predictive|2|A project manager needs to close out a project and does not have time to update the lessons learned register.|
|`pmp-p7-210`|export-batch-2|single|hybrid|3|A project manager recognizes the need to improve team dynamics and knowledge sharing during a megaproject.|
|`pmp-p7-211`|export-batch-2|single|predictive|1|A pilot project is ready to close and the project team wants to capture the challenges that were overcome and the opportunities…|
|`pmp-p7-212`|export-batch-2|single|predictive|2|A project manager just completed a project in an organization that values lessons learned. Leadership encourages all the project…|
|`pmp-p7-213`|export-batch-2|single|predictive|2|After an annual staff review, discussing project management and soft skills knowledge, the company made a decision to train all…|
|`pmp-p7-214`|export-batch-2|single|predictive|1|A project manager is consolidating project documentation such as videos, pictures, audio, to ensure the team is sharing out…|
|`pmp-p7-215`|export-batch-2|multi|predictive|3|A project manager is assigned to a project that is already in progress because the original project manager left the organization…|
|`pmp-p7-216`|export-batch-2|single|agile|3|Operations for a new technology-based project are being transitioned to a new project team. Early in the execution phase, the…|
|`pmp-p7-217`|export-batch-2|single|predictive|2|A project manager has been assigned to a new project that revisits a previously completed project.|
|`pmp-p7-218`|export-batch-2|single|hybrid|2|A project manager is new to the organization and is assigned to replace the project manager of an active project.|
|`pmp-p7-219`|export-batch-2|single|predictive|1|Upon completion of a long-term contract, the project manager confirms with the release manager that all planned features are…|
|`pmp-p7-220`|export-batch-2|single|predictive|2|A project manager realizes that some newly hired team members have not been as productive as expected. The project has a very…|
|`pmp-p7-221`|export-batch-2|single|predictive|3|A project manager is preparing for an organization-wide software system replacement project that will have a significant impact…|
|`pmp-p7-222`|export-batch-2|single|predictive|2|A project manager is overseeing the development of a new healthcare facility. The project team is struggling to understand some…|
|`pmp-p7-223`|export-batch-2|single|predictive|2|During a crucial phase of the project, a pivotal team member informs the project manager of their impending resignation,…|
|`pmp-p7-224`|export-batch-2|single|agile|2|During a critical phase of an agile development project, a specialist responsible for designing a key component is unexpectedly…|
|`pmp-p7-225`|export-batch-2|single|hybrid|3|During the critical phase of a remote-operated environmental research project, a key analyst departs, necessitating the swift…|
|`pmp-p7-226`|export-batch-2|multi|hybrid|3|In the midst of deploying a major IT infrastructure update, a project manager identifies a significant knowledge gap within the…|
|`pmp-p7-227`|export-batch-2|single|predictive|2|In finalizing an infrastructure upgrade project, the project manager needs to ensure that all critical operational knowledge has…|
|`pmp-p7-228`|export-batch-2|single|agile|2|You are working on an agile project where there are vastly differing levels of product knowledge within your team. The scrum…|
|`pmp-p7-229`|export-batch-2|single|agile|1|As an Agile project manager, you understand the need to operate with openness and transparency with your team and stakeholders.|
|`pmp-p7-230`|export-batch-2|single|predictive|2|You are nearing the end of your project which has delivered a brand new system to the organization. There have been many…|

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
|`pmp-p8-1`|export-batch-1|single|predictive|4|A project manager is working on a virtual project with a global team for the first time and encounters an issue with stakeholder…|
|`pmp-p8-2`|export-batch-1|single|predictive|2|A company has launched a new strategic project. The goal and scope of the project were loosely defined at the executive committee…|
|`pmp-p8-3`|export-batch-1|single|agile|2|A virtual team is midway through a sprint of a complex task and is ahead of schedule. During the daily standup, a new team member…|
|`pmp-p8-4`|export-batch-1|single|predictive|3|The project manager discovers that a stakeholder provides inaccurate status reports to the steering committee. Moreover, the…|
|`pmp-p8-5`|export-batch-1|single|predictive|3|In a large organization, a project is experiencing communication difficulties. Team members and functional leaders are expressing…|
|`pmp-p8-6`|export-batch-1|single|agile|3|A team is working on an innovation project using an incremental approach. After every status meeting, some key stakeholders…|
|`pmp-p8-7`|export-batch-1|single|predictive|4|A stakeholder is concerned that the project is falling behind schedule, based on unofficial information from the project team.…|
|`pmp-p8-8`|export-batch-1|single|predictive|3|During the execution phase of a healthcare IT project, the customer's project manager resigns and has not been replaced. The…|
|`pmp-p8-9`|export-batch-1|single|predictive|4|A key project sponsor requires monthly progress reports and assigns a contact person to communicate with the project manager.…|
|`pmp-p8-10`|export-batch-1|single|predictive|3|A project manager realizes that an email intended for a specific team member has been mistakenly sent to the entire global…|
|`pmp-p8-11`|export-batch-1|single|predictive|3|Although a project is performing within its time, scope, and budget constraints, the project manager learns that the project…|
|`pmp-p8-12`|export-batch-1|single|agile|3|The night before a standup meeting, a project manager realizes that they will be unable to attend the daily standup meeting in…|
|`pmp-p8-13`|export-batch-1|single|predictive|3|As agreed at the start of a predictive project, all outgoing information should be shared beforehand with the project manager.…|
|`pmp-p8-14`|export-batch-1|single|predictive|3|A project is halfway complete, and despite a good schedule performance index (SPI), the customer has identified several minor…|
|`pmp-p8-15`|export-batch-1|single|predictive|3|A project manager has created the stakeholder list and is deciding how to distribute information effectively to the customer.|
|`pmp-p8-201`|export-batch-2|single|agile|3|A pilot project is underway using an agile approach to development. The project is delivering the planned increments on time, but…|
|`pmp-p8-202`|export-batch-2|single|agile|3|An agile project manager based in country A is working with a development team and business analyst in country B, and a quality…|
|`pmp-p8-203`|export-batch-2|single|predictive|2|A strategic project has been launched and a project manager has been assigned. The project is progressing, and the team is…|
|`pmp-p8-204`|export-batch-2|single|agile|3|A large-scale project has been consistently progressing. However, the team is experiencing difficulties in completing some…|
|`pmp-p8-205`|export-batch-2|single|agile|1|At an agile project meeting, a team member gives an update on the status of their important task. A new team member interrupts…|
|`pmp-p8-206`|export-batch-2|single|agile|2|A new team is formed for an agile project, in which members are located in three different countries and five time zones.|
|`pmp-p8-207`|export-batch-2|single|predictive|2|A project manager communicates with a key stakeholder via phone calls only, as indicated in the communication management plan.…|
|`pmp-p8-208`|export-batch-2|single|agile|1|A project manager is working with remote team members who are acting independently.|
|`pmp-p8-209`|export-batch-2|single|hybrid|2|In a review meeting, a project manager detects a misunderstanding about one critical deliverable.|
|`pmp-p8-210`|export-batch-2|single|hybrid|2|A project manager has been assigned a project that involves a virtual, multicultural team including representatives from various…|
|`pmp-p8-211`|export-batch-2|single|agile|1|A new project involving team members from two countries has just begun. Due to a time difference, one site finishes work an hour…|
|`pmp-p8-212`|export-batch-2|single|hybrid|1|A global, cross-functional team has been assembled to deliver a multinational initiative.|
|`pmp-p8-213`|export-batch-2|single|hybrid|3|A project manager works for a company involved in complex turnkey projects. During a team assessment, the project manager finds…|
|`pmp-p8-214`|export-batch-2|single|hybrid|3|A new project manager is assigned to a project in the execution phase. The project just completed the deliverables for the first…|
|`pmp-p8-215`|export-batch-2|single|hybrid|2|A project team using a hybrid approach is producing both predictive and incremental deliverables simultaneously. The…|
|`pmp-p8-216`|export-batch-2|single|hybrid|3|A project management organization has a project that leverages waterfall for the requirements gathering phase and agile for…|
|`pmp-p8-217`|export-batch-2|single|predictive|2|A project manager learned that the project deliverables will not meet the quality requirements unless a change is made to a…|
|`pmp-p8-218`|export-batch-2|multi|hybrid|3|A project manager working in a global organization decides to use a low-cost social media platform to exchange information,…|
|`pmp-p8-219`|export-batch-2|single|agile|2|A project team is actively engaged in the preparation of the last delivery before the final product is live in production.|
|`pmp-p8-220`|export-batch-2|single|agile|2|A company begins to implement Scrum. About halfway through the first sprint, communication issues develop. Some project team…|
|`pmp-p8-221`|export-batch-2|single|predictive|2|A project management team is developing a project management information system (PMIS) as a collaboration tool for internal and…|
|`pmp-p8-222`|export-batch-2|single|hybrid|3|Stakeholders in a multinational project are experiencing cross-cultural misunderstandings. A seemingly small cultural…|
|`pmp-p8-223`|export-batch-2|single|hybrid|3|In an industrial plant, the owner has decided to add more capacity to the plant by installing a new machine and updating the…|
|`pmp-p8-224`|export-batch-2|single|predictive|2|In the company cafeteria, a project manager overhears an unengaged stakeholder mocking the style of language used in the project…|
|`pmp-p8-225`|export-batch-2|single|hybrid|2|A project manager is leading a project that is being implemented simultaneously in two countries with an 11-hour time zone…|
|`pmp-p8-226`|export-batch-2|single|predictive|2|A project manager has just started building the team for a huge and complex project. The project manager is highly interested in…|
|`pmp-p8-227`|export-batch-2|single|predictive|3|What can a project manager do to communicate the formal project announcement and relevant information to stakeholders, and then…|
|`pmp-p8-228`|export-batch-2|single|predictive|1|A project requires the services of a specialized vendor to install a very complex and expensive piece of equipment. The project…|
|`pmp-p8-229`|export-batch-2|single|predictive|2|A stakeholder requests more frequent and detailed project status reports than a previous agreement. The stakeholder also…|
|`pmp-p8-230`|export-batch-2|single|predictive|2|A complex manufacturing project has multiple stakeholders in multiple locations. The project is entering a high-activity phase…|
|`pmp-p8-231`|export-batch-2|single|predictive|2|A project manager learns that a stakeholder thinks that only certain stakeholders are getting all the project updates and feels…|
|`pmp-p8-232`|export-batch-2|single|predictive|2|The project sponsors for a multinational project have put a process in place that generates emails and reports at the end of each…|
|`pmp-p8-233`|export-batch-2|single|predictive|2|During a project's execution phase, two stakeholders complain that they receive insufficient information on the project's status.…|
|`pmp-p8-234`|export-batch-2|multi|agile|3|A project manager for a cross-functional team realizes that there is a lack of commitment from stakeholders to the project…|
|`pmp-p8-235`|export-batch-2|single|hybrid|2|A project manager for a cross-functional team is finding it difficult to schedule regular meetings and communicate with all team…|
|`pmp-p8-236`|export-batch-2|single|agile|2|A global organization that has always used predictive project management is now moving towards agile project management. In the…|
|`pmp-p8-237`|export-batch-2|single|predictive|2|A construction project is in the execution phase when several new team members from different countries are assigned to the…|
|`pmp-p8-238`|export-batch-2|single|predictive|2|As a result of a drastic cut to the budget, a project's steering committee is replaced with a single sponsor.|
|`pmp-p8-239`|export-batch-2|single|predictive|2|A project manager must get approval for a complex project management plan from multiple stakeholders in various countries before…|
|`pmp-p8-240`|export-batch-2|single|agile|2|A project manager in Location A works with a local team, while developers are in another remote location, and a quality assessor…|
|`pmp-p8-241`|export-batch-2|single|hybrid|2|A project manager wants to ensure that virtual team meetings are effective.|
|`pmp-p8-242`|export-batch-2|single|agile|3|An efficient, empowered agile innovation team does NOT understand why required approvals from the legal department are allowed to…|
|`pmp-p8-243`|export-batch-2|single|predictive|1|A colocated project team is facing communication and collaboration issues. Some team members have roles that require them to work…|
|`pmp-p8-244`|export-batch-2|single|agile|2|An agile project is in the first of six iterations. The team is located globally, so the meetings are in a virtual environment.…|
|`pmp-p8-245`|export-batch-2|single|predictive|1|After emailing a recent project status report, the project manager learns that a number of recipients are no longer involved with…|
|`pmp-p8-246`|export-batch-2|single|hybrid|2|A project manager is leading a new implementation project with a global project team. As team members are in different…|
|`pmp-p8-247`|export-batch-2|single|agile|2|As part of the project sprint review, stakeholders appear uninformed and are unable to agree on how to move the project forward.|
|`pmp-p8-248`|export-batch-2|single|predictive|2|A project’s single sponsor has been replaced by a steering committee due to a significant increase in project size.|
|`pmp-p8-249`|export-batch-2|single|hybrid|2|A project team, distributed across three different countries, is having a difficult time arranging meetings. The team members are…|
|`pmp-p8-250`|export-batch-2|single|hybrid|2|A company has decided to utilize a diverse international team that will work virtually. This will be the first time that the team…|
|`pmp-p8-251`|export-batch-2|single|agile|2|An adaptative project team is globally distributed and the team members speak different primary languages. Daily meetings are…|
|`pmp-p8-252`|export-batch-2|single|hybrid|2|A geographically distributed project team disagrees about status reporting. Some team members prefer detailed reports, while…|
|`pmp-p8-253`|export-batch-2|single|predictive|1|During the rollout of a new IT system within a large corporation, the project manager observes that discussions in stakeholder…|
|`pmp-p8-254`|export-batch-2|single|predictive|2|A project manager is leading a city park renovation project. During a status meeting with city officials, the project manager…|
|`pmp-p8-255`|export-batch-2|single|agile|2|On a high-stakes software development agile project, the project manager notices a pattern where numerous team members are either…|
|`pmp-p8-256`|export-batch-2|single|hybrid|3|In spearheading a groundbreaking environmental initiative within a large organization, the project manager faces the challenge of…|
|`pmp-p8-257`|export-batch-2|single|predictive|2|During the execution phase of an urban development project, the project manager uses email updates to keep all stakeholders in…|
|`pmp-p8-258`|export-batch-2|single|predictive|3|In the midst of a project's lifecycle, a project manager identifies that essential feedback from a vital operational division has…|
|`pmp-p8-259`|export-batch-2|single|agile|1|As a project manager initiates a project with a fully remote team, ensuring effective communication becomes a challenge.|
|`pmp-p8-260`|export-batch-2|single|agile|2|In an initiative to manage a software development team spread across multiple continents, a project manager seeks to enhance…|
|`pmp-p8-261`|export-batch-2|single|predictive|2|In the process of deploying a new customer relationship management (CRM) system, the project manager realizes that the updates…|
|`pmp-p8-262`|export-batch-2|single|hybrid|2|During the integration phase of an international IT project, the project manager notices frequent miscommunications between the…|
|`pmp-p8-263`|export-batch-2|single|predictive|2|During the implementation phase of a citywide public transportation improvement project, a city council member requests a…|
|`pmp-p8-264`|export-batch-2|single|agile|2|Leading a software development agile project for a multinational corporation, a project manager faces the challenge of managing a…|
|`pmp-p8-265`|export-batch-2|single|predictive|1|To enhance project communication and address emerging issues, a project manager organizes bi-weekly meetings with the development…|
|`pmp-p8-266`|export-batch-2|single|predictive|1|In the midst of a complex IT project, feedback from an executive sponsor indicates dissatisfaction with the detailed nature of…|
|`pmp-p8-267`|export-batch-2|single|predictive|1|You are managing a project which communicates to the customer using mostly pull communication so far. When monitoring the…|
|`pmp-p8-268`|export-batch-2|single|predictive|2|You have taken over on a project with a vendor where scope and requirements have been missed. You notice that miscommunication…|
|`pmp-p8-269`|export-batch-2|single|predictive|2|You are putting together a project plan for a change that affects nearly ten thousand stakeholders in your organisation. You have…|
|`pmp-p8-270`|export-batch-2|single|predictive|1|Part of your project management plan is a detailed communications plan, which has the stakeholders on the project, their…|
|`pmp-p8-271`|export-batch-2|single|predictive|2|You are working as a project manager on a regulatory change for your organisation. You notice that the team are not communicating…|
|`pmp-p8-272`|export-batch-2|single|agile|2|You are working as a project manager with an Agile project team. The project sponsor sits on another floor, and prefers to…|
|`pmp-p8-273`|export-batch-2|single|agile|1|You are working as a project manager in a team that is moving to an Agile way of work. You have previously engaged the team in a…|
|`pmp-p8-274`|export-batch-2|single|predictive|2|You are a project manager working in the initial phases of a project, and trying to put together a document for your stakeholders…|
|`pmp-p8-275`|export-batch-2|single|predictive|2|The project manager on your project recently left, and you agreed to see the project through as it is nearly complete. The…|
|`pmp-p8-276`|export-batch-2|single|predictive|2|Your project involves nearly a dozen teams who are dispersed around the country at different sites, with thousands of impacted…|
|`pmp-p8-277`|export-batch-2|single|agile|1|You have joined a new project team and are moving to an Agile way of work. Part of this is co-locating the team to gain the…|
|`pmp-p8-278`|export-batch-2|single|agile|1|You are working with an Agile team who are not co-located. They are in various locations around the country.|
|`pmp-p8-279`|export-batch-2|single|agile|1|The twelve Agile clarifying principles also match up with the best advice for projects, leadership and communication.|
|`pmp-p8-280`|export-batch-2|single|agile|1|You are working in an organization that is new to Agile, and have just built your initial team to start your project. The team is…|
|`pmp-p8-281`|export-batch-2|single|agile|2|You have been working on a high-profile project with a large amount of business executives who will gain benefit from what you…|
