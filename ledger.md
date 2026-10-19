# Question ledger

Every question the app ships — the core pack and case studies in `index.html`
and every file in `packs/` — grouped by ECO task. Per-task and per-domain
slices of this file are in `ledger/`; upload the slice you need to a model
rather than this whole file.

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

## Totals

|Slice|Count|Share|
|-|-:|-:|
|**Items**|**2992**||
|People|1006|33.6%|
|Process|1182|39.5%|
|Business Environment|804|26.9%|

|Type|Count|
|-|-:|
|fillblank|79|
|graphic|12|
|hotspot|66|
|matching|117|
|multi|257|
|pulldown|71|
|single|2390|

|Approach|Count|
|-|-:|
|agile|976|
|hybrid|565|
|predictive|1451|

---

## People

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
|`pmp-p1-201`|pmp-mocks-1-10|single|predictive|3|An organization recently updated its strategic plan and some of the changes were substantial. During a meeting for an ongoing…|
|`pmp-p1-202`|pmp-mocks-1-10|multi|predictive|4|A project manager is developing a project management plan. The project duration is set for three months due to regulatory…|
|`pmp-p1-203`|pmp-mocks-1-10|single|agile|3|A project manager just started managing agile projects. The project manager realizes that due to the lack of a clear definition…|
|`pmp-p1-204`|pmp-mocks-1-10|single|agile|3|At the beginning of an agile project, the project manager is developing the project charter with the team. They are unable to…|
|`pmp-p1-205`|pmp-mocks-1-10|single|predictive|3|The headquarters of a multinational company is in Europe, with many regional offices in other countries around the world. The…|
|`pmp-p1-206`|pmp-mocks-1-10|single|predictive|3|A project manager is assigned to a legacy system modernization project. The project management plan is solidified and the…|
|`pmp-p1-207`|pmp-mocks-1-10|single|predictive|1|A project manager has started developing a plan for a new project. During the first planning meeting, the stakeholders ask about…|
|`pmp-p1-208`|pmp-mocks-1-10|single|predictive|2|A project team meeting is focused on examining if the business value and vision for a project are being delivered by all parties.…|
|`pmp-p1-209`|pmp-mocks-1-10|single|predictive|1|Following project approval, a project team is assigned and the project management plan is created.|
|`pmp-p1-210`|pmp-mocks-1-10|single|predictive|2|A project manager leaves an ongoing project and is replaced by a new project manager.|
|`pmp-p1-211`|pmp-mocks-1-10|single|predictive|2|An experienced project manager is preparing a kick-off meeting for a new service assessment. A high-level description of the…|
|`pmp-p1-212`|pmp-mocks-1-10|single|predictive|3|During a project's initiation phase, the project manager reviews the business case and finds that information is incomplete.|
|`pmp-p1-213`|pmp-mocks-1-10|single|predictive|2|A research and development department is planning to develop a product that will introduce a new line of business for the…|
|`pmp-p1-214`|pmp-mocks-1-10|single|hybrid|3|A team for a project is composed of individuals from different backgrounds, ages, and interests. The team is struggling to work…|
|`pmp-p1-215`|pmp-mocks-1-10|single|agile|2|An agile team finds it difficult to complete a checklist because they have different interpretations of what the project aims to…|
|`pmp-p1-216`|pmp-mocks-1-10|single|predictive|2|A project manager has been assigned to a new project. During the initial review of the project charter, the project manager…|
|`pmp-p1-217`|pmp-mocks-1-10|single|predictive|2|A project sponsor assigns a project manager to a new, critical project. This project has tight budget and time constraints.|
|`pmp-p1-218`|pmp-mocks-1-10|single|agile|3|An organization has implemented agile for its recent projects, but all of the recent agile projects have missed their delivery…|
|`pmp-p1-219`|pmp-mocks-1-10|single|predictive|2|A functional manager questions whether certain aspects of a project are in compliance with the project's goals.|
|`pmp-p1-220`|pmp-mocks-1-10|single|agile|3|An organization just implemented agile and forms a development team where members are not co-located. The organization’s…|
|`pmp-p1-221`|pmp-mocks-1-10|single|agile|2|A new product is being constructed by an agile team, but the team has a limited amount of time to complete it. Due to these…|
|`pmp-p1-222`|pmp-mocks-1-10|single|predictive|1|A project manager is in the initiation phase of a new project and is preparing to develop a project team charter. The project…|
|`pmp-p1-223`|pmp-mocks-1-10|single|agile|1|In order to function optimally, a project team should have a project charter that states the vision or purpose of the project and…|
|`pmp-p1-224`|pmp-mocks-1-10|single|predictive|2|The project manager needs to survey the necessary parties to reach a consensus and build a common vision for the project.|
|`pmp-p1-225`|pmp-mocks-1-10|single|predictive|2|A project manager is working on an internal project and notes that the visions and goals of other department leaders are not…|
|`pmp-p1-226`|pmp-mocks-1-10|single|predictive|2|Throughout a project, it becomes apparent that team members from different departments have differing perspectives on what the…|
|`pmp-p1-227`|pmp-mocks-1-10|single|hybrid|2|As the project manager of a new healthcare software implementation project, you're leading a team that's decided to embrace a…|
|`pmp-p1-228`|pmp-mocks-1-10|single|hybrid|2|You have been allocated to a new project team with members from different parts of the functional business area. It soon becomes…|
|`pmp-p1-229`|pmp-mocks-1-10|single|agile|2|As the project manager for a software development project, you're informed that due to budget reallocations, several developers…|
|`pmp-p1-230`|pmp-mocks-1-10|single|predictive|2|You have been assigned to a new project, where the work is behind schedule and the team are not working together well. The team…|
|`pmp-p1-231`|pmp-mocks-1-10|single|agile|2|Your project team are protected by a union agreement. You notice that they don't know how to use the product you are improving,…|
|`pmp-p1-232`|pmp-mocks-1-10|single|hybrid|2|You have formed a new project team and have worked through the purpose and mission of the project with them. You expanded on who…|
|`pmp-p1-233`|pmp-mocks-1-10|single|agile|2|You are working in a new agile team, where the work has started to slow and there is uncertainty and low team engagement. You set…|
|`pmp-p1-234`|pmp-mocks-1-10|single|agile|1|Your project is new and the team have just come on board. There have been multiple disagreements and the work is slow as they try…|
|`pmp-p1-235`|pmp-mocks-1-10|single|agile|2|You are an Agile project manager leading a new software project. During the last retrospective there was very little engagement…|
|`pmp-p1-236`|pmp-mocks-1-10|single|agile|1|You have brought together a new project team to deliver a system that has never been done before in your organization. The…|
|`pmp-p1-237`|pmp-mocks-1-10|single|agile|1|You are working on an Agile project and have been asked to create a document that lists team principles, agreements, and…|

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
|`pmp-p2-1`|pmp-lectures-1-11|single|agile|3|During a retrospective meeting, some team members expressed that they strongly disagree with how the product owner and scrum…|
|`pmp-p2-2`|pmp-lectures-1-11|single|agile|3|A new project manager is assigned to an ongoing agile project. The project is now starting its second 4-week iteration cycle. The…|
|`pmp-p2-3`|pmp-lectures-1-11|single|agile|3|During a software development project, the team realized that several features failed to comply with quality requirements. After…|
|`pmp-p2-4`|pmp-lectures-1-11|single|predictive|4|A client's interactions with the project team have been somewhat negative. Project team members don't want to be part of the…|
|`pmp-p2-5`|pmp-lectures-1-11|single|agile|3|A team has recently adopted agile and is following a scrum model. There is a conflict between a lead developer and a quality…|
|`pmp-p2-6`|pmp-lectures-1-11|single|predictive|4|A project team member suddenly approaches the project manager and asks to resign from the project. The team member is upset…|
|`pmp-p2-7`|pmp-lectures-1-11|single|predictive|3|A project manager is leading a complex software development project. The project team cannot agree on which process to use when…|
|`pmp-p2-8`|pmp-lectures-1-11|single|predictive|4|A key member of a project team publicly declares that another team member has been speaking negatively about them. The team…|
|`pmp-p2-9`|pmp-lectures-1-11|single|predictive|3|During a multidisciplinary project, a project manager learns that there are many conflicts about design among team members from…|
|`pmp-p2-201`|pmp-mocks-1-10|single|agile|3|During a standup, a team member falsely reports the completion of a story which is a dependency for other stories. When the team…|
|`pmp-p2-202`|pmp-mocks-1-10|single|agile|2|During a standup meeting, some of the team members strongly disagreed about how tasks were distributed. They feel tired and…|
|`pmp-p2-203`|pmp-mocks-1-10|single|predictive|2|Discussions during weekly project team meetings are frequently dominated by a senior team member who has a tendency to talk over…|
|`pmp-p2-204`|pmp-mocks-1-10|single|predictive|3|A delivery team member is complaining about the workload and the number of top priorities they are handling. Another team member…|
|`pmp-p2-205`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a hybrid project with a self-organizing team. The team is unable to agree on the scope for the next…|
|`pmp-p2-206`|pmp-mocks-1-10|single|predictive|2|A senior team member is having challenges with a new project team member. The senior team member asks the project manager for…|
|`pmp-p2-207`|pmp-mocks-1-10|single|agile|3|Company A is moving from a predictive approach to an agile one. After the second incremental release of project X, the product…|
|`pmp-p2-208`|pmp-mocks-1-10|single|hybrid|3|A project is underway with team members from two different countries and there are complaints from both sides. The team members…|
|`pmp-p2-209`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a new project. Two key project resources have a disagreement regarding a requirement. The project…|
|`pmp-p2-210`|pmp-mocks-1-10|single|hybrid|2|A virtual project team is composed of members from various countries with cultural differences. The project manager is concerned…|
|`pmp-p2-211`|pmp-mocks-1-10|single|agile|3|A project leader and a product owner had a side conversation about the status of the next delivery. The product owner raised…|
|`pmp-p2-212`|pmp-mocks-1-10|single|predictive|2|A delivery milestone for a project is fast approaching, but an item that is planned to be completed is at risk. A team member…|
|`pmp-p2-213`|pmp-mocks-1-10|single|predictive|2|A project manager complains that three engineers from different cultural backgrounds leave project meetings twice a day and…|
|`pmp-p2-214`|pmp-mocks-1-10|single|hybrid|2|A project manager identifies conflicts among local and virtual team members' understanding of the deliverables.|
|`pmp-p2-215`|pmp-mocks-1-10|single|predictive|2|During a meeting with project stakeholders, two stakeholders have a disagreement regarding how a software component is being…|
|`pmp-p2-216`|pmp-mocks-1-10|single|predictive|2|A project manager is working on a complex software project. Two team members must team up and work together on a critical module…|
|`pmp-p2-217`|pmp-mocks-1-10|single|predictive|2|A team member is hosting an event and invites several colleagues through their work calendars. Another team member, who was not…|
|`pmp-p2-218`|pmp-mocks-1-10|single|predictive|1|A project manager has noticed apparent interpersonal conflict between some team members during the initiation of a project.|
|`pmp-p2-219`|pmp-mocks-1-10|single|predictive|2|A project team has an after-hours get-together. One of the team members who was recently hired and new to the team did not…|
|`pmp-p2-220`|pmp-mocks-1-10|single|predictive|2|A newly-hired project manager is running a project for the first time and is worried about rising tensions. Project team members…|
|`pmp-p2-221`|pmp-mocks-1-10|single|predictive|2|A project manager is leading a project team composed of members from different departments, including engineering, marketing, and…|
|`pmp-p2-222`|pmp-mocks-1-10|single|predictive|2|During a project team meeting, the project manager notices a conflict between three key team members who have very different…|
|`pmp-p2-223`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to a project with a lot of stakeholders and competing priorities.|
|`pmp-p2-224`|pmp-mocks-1-10|single|agile|2|A project manager notices a potential conflict between a remote developer and an on-site tester during a sprint; the developer…|
|`pmp-p2-225`|pmp-mocks-1-10|single|predictive|3|An expert resource avoids assignments that include working with a foreign contractor.|
|`pmp-p2-226`|pmp-mocks-1-10|single|agile|2|A project manager is working on an IT project. During an iteration review meeting, a couple of key stakeholders enter into a…|
|`pmp-p2-227`|pmp-mocks-1-10|single|agile|2|The project manager schedules an iteration review and learns that a few key stakeholders do not approve of how a feature has been…|
|`pmp-p2-228`|pmp-mocks-1-10|single|predictive|2|A project manager working with a culturally diverse team discovers major intra-team conflicts during project execution.|
|`pmp-p2-229`|pmp-mocks-1-10|single|predictive|2|A project manager is managing multidisciplinary teams. During the last project review meeting, the project manager realized that…|
|`pmp-p2-230`|pmp-mocks-1-10|single|agile|2|A project team member continues to change a colleague's deliverable during every sprint. The colleague has asked the project…|
|`pmp-p2-231`|pmp-mocks-1-10|single|predictive|2|A team member is unhappy with their workload and feels they are not being supported enough. The project manager wants to meet…|
|`pmp-p2-232`|pmp-mocks-1-10|single|agile|2|The conflict about how to proceed has developed between two team members prior to a release. The team needs to work together to…|
|`pmp-p2-233`|pmp-mocks-1-10|single|hybrid|3|A geographically distributed project team has development issues between the colocated and virtual teams, causing productivity to…|
|`pmp-p2-234`|pmp-mocks-1-10|single|predictive|2|A project team is small and the team members have known each other for a long time. The team's project is in the execution phase.…|
|`pmp-p2-235`|pmp-mocks-1-10|single|predictive|2|A team member with sufficient experience and knowledge informs the project manager about a conflict with another team member and…|
|`pmp-p2-236`|pmp-mocks-1-10|single|predictive|2|The execution of a construction project in a remote area requires team members to live together in corporate dormitories when…|
|`pmp-p2-237`|pmp-mocks-1-10|single|predictive|3|At the midpoint of a renewable energy project, the project manager is faced with a conflict between the design team and…|
|`pmp-p2-238`|pmp-mocks-1-10|single|predictive|3|During a project charter review session, a dispute arises between the project sponsor and the product owner, witnessed by the…|
|`pmp-p2-239`|pmp-mocks-1-10|single|predictive|3|During the project execution phase, conflicts have arisen between two key team members, negatively impacting their work output.…|
|`pmp-p2-240`|pmp-mocks-1-10|multi|predictive|3|At the outset of a new phase in a complex project, after a successful kick-off meeting at the client's site, a team member…|
|`pmp-p2-241`|pmp-mocks-1-10|single|predictive|2|A project manager is overseeing the development of a new urban park. During the project, a dispute arises between the landscape…|
|`pmp-p2-242`|pmp-mocks-1-10|multi|predictive|3|During the construction phase of a new city park, conflicts have emerged between the landscape designers and environmental…|
|`pmp-p2-243`|pmp-mocks-1-10|single|agile|2|As the project manager for a novel online education platform, you're faced with divergent views among stakeholders regarding the…|
|`pmp-p2-244`|pmp-mocks-1-10|single|predictive|2|You are working on a high profile project. Several high-ranking senior executives want to take the credit for this initiative,…|
|`pmp-p2-245`|pmp-mocks-1-10|single|predictive|2|During a key scope meeting, an argument arises. The business manager accuses the technology manager of not delivering to their…|
|`pmp-p2-246`|pmp-mocks-1-10|single|predictive|3|Your team members have raised an issue as they believe the current project scope will not reach the intended project goal of…|
|`pmp-p2-247`|pmp-mocks-1-10|single|agile|2|You are an agile project manager, and holding daily stand-ups with your team. You notice people accusing other team members of…|
|`pmp-p2-248`|pmp-mocks-1-10|single|predictive|1|You are working on a project where the functional manager is unhappy with the changes being brought about to his part of the…|
|`pmp-p2-249`|pmp-mocks-1-10|single|predictive|2|The project you are working on has recently started and the team has come together from many different parts of the organisation,…|
|`pmp-p2-250`|pmp-mocks-1-10|single|predictive|2|You are working on a project where two high-ranked executives keep derailing project meetings with their own agenda, and adding…|

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
|`pmp-p3-1`|pmp-lectures-1-11|single|predictive|3|A project manager is building a project team. All of the project team members have worked together on previous projects. One of…|
|`pmp-p3-2`|pmp-lectures-1-11|single|agile|2|A project manager for a new project is tasked with applying agile practices to improve team performance.|
|`pmp-p3-3`|pmp-lectures-1-11|single|agile|3|A project manager has a newly formed team that includes junior members who have not worked on agile projects before. The product…|
|`pmp-p3-4`|pmp-lectures-1-11|single|agile|2|A newly appointed product owner is not sure how to prioritize product specifications and mentions this challenge to the project…|
|`pmp-p3-5`|pmp-lectures-1-11|single|predictive|3|The project scope statement is approved by the project sponsor. The team members would like to modify the work breakdown…|
|`pmp-p3-6`|pmp-lectures-1-11|single|agile|2|An agile project team member is working on a user story that is overdue. This delay is causing the team to miss sprint goals for…|
|`pmp-p3-7`|pmp-lectures-1-11|single|predictive|3|A project manager is assigned to an ongoing project with virtual team members from different countries who are working remotely…|
|`pmp-p3-8`|pmp-lectures-1-11|single|predictive|3|During a one-on-one session, a new team member shared their frustration regarding their inability to successfully complete an…|
|`pmp-p3-9`|pmp-lectures-1-11|single|agile|3|A project team adopted agile practices to meet the business need to develop an application. One of the senior team members…|
|`pmp-p3-10`|pmp-lectures-1-11|single|hybrid|4|A project manager has been assigned to a project that is currently late and over budget. The project uses a predictive approach…|
|`pmp-p3-11`|pmp-lectures-1-11|single|predictive|3|An extremely talented and results-oriented team member is suddenly struggling with a personal problem. Due to this, there is a…|
|`pmp-p3-12`|pmp-lectures-1-11|single|agile|3|A new agile team is forming and the project manager notices that the team is struggling in many areas. The team is experiencing…|
|`pmp-p3-13`|pmp-lectures-1-11|single|predictive|2|A project team has just reached a project milestone successfully. The project manager noticed that the team performed extremely…|
|`pmp-p3-14`|pmp-lectures-1-11|single|agile|3|A project manager oversees an agile project with an experienced project team. The project manager does not typically attend the…|
|`pmp-p3-201`|pmp-mocks-1-10|single|hybrid|3|A highly skilled subject matter expert (SME) is engaged in a government project where approximately 30% of the deliverables will…|
|`pmp-p3-202`|pmp-mocks-1-10|single|predictive|3|The lead sales manager and team are unable to submit a project status report on time for the new sales report integration…|
|`pmp-p3-203`|pmp-mocks-1-10|single|agile|3|A midsize project has the objective of developing an innovative product with changing requirements. Several team members are…|
|`pmp-p3-204`|pmp-mocks-1-10|single|hybrid|2|A project manager used to work on predictive projects and now has started working on a project using a hybrid approach.|
|`pmp-p3-205`|pmp-mocks-1-10|single|predictive|2|A project manager believes that the project team could benefit from development activities to improve project performance and…|
|`pmp-p3-206`|pmp-mocks-1-10|single|agile|2|Due to a lack of locally skilled resources, a company employs an internationally dispersed project team that will work virtually.…|
|`pmp-p3-207`|pmp-mocks-1-10|single|predictive|2|A project manager notices that team members are not interacting with each other and believes that the lack of interaction is…|
|`pmp-p3-208`|pmp-mocks-1-10|single|predictive|2|The project manager of a large team notices team members are not interacting together, which is impacting project performance and…|
|`pmp-p3-209`|pmp-mocks-1-10|single|predictive|3|A project is in the implementation phase. A new project manager discovers that the team members have no previous experience with…|
|`pmp-p3-210`|pmp-mocks-1-10|single|predictive|2|A project manager observes that team members are not collaborating or being proactive.|
|`pmp-p3-211`|pmp-mocks-1-10|single|predictive|2|A project manager realizes that the team needs training on a specific technology that is required in the project.|
|`pmp-p3-212`|pmp-mocks-1-10|single|agile|2|A team member who is unfamiliar with the subject matter is struggling to complete a complex deliverable.|
|`pmp-p3-213`|pmp-mocks-1-10|single|predictive|3|A project manager is assembling a team with a mix of new and experienced team members. The project manager finds out that there…|
|`pmp-p3-214`|pmp-mocks-1-10|single|agile|2|A project manager is not sure if the team has the critical skills needed to implement a key feature of an upcoming deliverable.|
|`pmp-p3-215`|pmp-mocks-1-10|single|agile|2|A project manager notices that conflicts between team members are occurring in every sprint. After an investigation, the project…|
|`pmp-p3-216`|pmp-mocks-1-10|single|agile|2|A remote project team consists of members from two countries. One of the teams is expecting to have three days off to celebrate a…|
|`pmp-p3-217`|pmp-mocks-1-10|single|agile|3|Members of an agile team are struggling. While there is trust among the team members, the majority of team decisions are made by…|
|`pmp-p3-218`|pmp-mocks-1-10|single|hybrid|2|In the early stages of team formation, a project manager is unable to focus on the main tasks because team members are constantly…|
|`pmp-p3-219`|pmp-mocks-1-10|single|predictive|2|At the first meeting about a road construction project, the project manager reminds the team of the importance of observing all…|
|`pmp-p3-220`|pmp-mocks-1-10|single|hybrid|1|A project manager is setting expectations with a team that is delivering a hybrid project.|
|`pmp-p3-221`|pmp-mocks-1-10|single|predictive|2|A senior team member was very influential and played a crucial role in past projects. In this new project, the senior member no…|
|`pmp-p3-222`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a project using a hybrid approach. The project team began the project with high morale and…|
|`pmp-p3-223`|pmp-mocks-1-10|single|agile|2|Working in a continuous delivery environment, a project leader notices that one of the key team members is experiencing low…|
|`pmp-p3-224`|pmp-mocks-1-10|single|predictive|2|During project execution, the project manager realizes that a team member is not completing tasks in accordance with the project…|
|`pmp-p3-225`|pmp-mocks-1-10|single|hybrid|1|A project team is working on a regulatory application that has a fixed duration for delivery. The team is using a hybrid approach…|
|`pmp-p3-226`|pmp-mocks-1-10|single|agile|2|A new project manager is assigned to lead an agile project. The project manager wants to use motivation to encourage the team to…|
|`pmp-p3-227`|pmp-mocks-1-10|single|agile|3|A project manager leads two highly motivated teams who typically work on 3-4 concurrent short projects with tight deadlines.…|
|`pmp-p3-228`|pmp-mocks-1-10|single|predictive|2|A new member joined a project during the execution phase. The work requires strong collaboration with other team members.|
|`pmp-p3-229`|pmp-mocks-1-10|single|predictive|2|A project manager begins to notice disengaged members arriving late, not delivering on time, not attending meetings, and…|
|`pmp-p3-230`|pmp-mocks-1-10|single|agile|3|A project manager is leading a Scrum team. During Business Acceptance Testing (BAT), the client rejects the deliverable due to…|
|`pmp-p3-231`|pmp-mocks-1-10|single|predictive|3|During the execution phase of a project, a new project manager is assigned to replace the previous project manager. As the…|
|`pmp-p3-232`|pmp-mocks-1-10|single|hybrid|3|A team is assembled for a new project that will be using a predictive approach through requirements gathering and an agile…|
|`pmp-p3-233`|pmp-mocks-1-10|single|predictive|2|A project manager has been on a complex project that began eight months ago. The project manager has a quality team that has been…|
|`pmp-p3-234`|pmp-mocks-1-10|single|agile|1|An organization has decided to implement agile and a project manager with Scrum Master has been selected to coach the project…|
|`pmp-p3-235`|pmp-mocks-1-10|single|predictive|2|During the execution phase of your project, a Resource Manager informed you that one of the experts allocated to your project had…|
|`pmp-p3-236`|pmp-mocks-1-10|single|predictive|2|During project execution, a team member made a mistake resulting in a 1-week delay to the project schedule.|
|`pmp-p3-237`|pmp-mocks-1-10|single|predictive|2|The project manager for a complex project discovers that a certain team member has missed several targets.|
|`pmp-p3-238`|pmp-mocks-1-10|single|predictive|2|A project leader noticed that a new team member is demotivated and always arrives late to work, causing a delay in the…|
|`pmp-p3-239`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to an existing project, replacing the previous project manager. Soon after joining the project, the…|
|`pmp-p3-240`|pmp-mocks-1-10|single|predictive|3|A project manager must make decisions daily, regardless of the decision's level of importance or criticality.|
|`pmp-p3-241`|pmp-mocks-1-10|multi|hybrid|3|A project manager wants to develop their own leadership capability during a long project. Select three ways to improve and…|
|`pmp-p3-242`|pmp-mocks-1-10|single|hybrid|2|A project manager is managing a hybrid project. A stakeholder has directly contacted a team member and has requested a change.…|
|`pmp-p3-243`|pmp-mocks-1-10|single|hybrid|3|A project manager is managing a project with a hybrid approach and has requested a senior team member to perform a quantitative…|
|`pmp-p3-244`|pmp-mocks-1-10|single|hybrid|2|A newly formed project team features members from several countries and continents, with significant age differences and a…|
|`pmp-p3-245`|pmp-mocks-1-10|single|hybrid|1|A project manager engaged in the delivery of a global project is in the process of recruiting a project team.|
|`pmp-p3-246`|pmp-mocks-1-10|single|predictive|2|Some team members assigned to a new technology project expressed their lack of knowledge about the technology and the challenges…|
|`pmp-p3-247`|pmp-mocks-1-10|single|predictive|2|A project manager is in a matrix organization environment. The project manager notices the team is not working well together,…|
|`pmp-p3-248`|pmp-mocks-1-10|single|hybrid|3|A team member received a request from a stakeholder to conduct a quantitative risk analysis. The team member is not certain…|
|`pmp-p3-249`|pmp-mocks-1-10|single|agile|3|In the middle of a project incremental delivery, due to a change in the technology, a team member informed the project leader…|
|`pmp-p3-250`|pmp-mocks-1-10|single|agile|1|A product owner has requested to incorporate embedded functionality for the patient portal in the next sprint. The team decided…|
|`pmp-p3-251`|pmp-mocks-1-10|single|predictive|2|A project team just completed a major milestone and is now on track to deliver the project ahead of the due date. The project…|
|`pmp-p3-252`|pmp-mocks-1-10|single|predictive|2|A project manager wants to motivate their team members and improve project performance.|
|`pmp-p3-253`|pmp-mocks-1-10|multi|predictive|3|An internal project has delivered its scope on time, within budget, and with the expected quality. The client was satisfied with…|
|`pmp-p3-254`|pmp-mocks-1-10|single|agile|2|An experienced project manager is managing a multi-million dollar agile project. The project manager communicates the ground…|
|`pmp-p3-255`|pmp-mocks-1-10|single|agile|3|Since the beginning of a project, the product owner keeps asking during ceremonies about the budget spent for each product…|
|`pmp-p3-256`|pmp-mocks-1-10|single|agile|2|A project team recently completed a long-term project using a predictive approach. The team then started a new project with the…|
|`pmp-p3-257`|pmp-mocks-1-10|single|hybrid|3|A project is being executed. The project's life cycle is defined as predictive; however, a major deliverable will be handed over…|
|`pmp-p3-258`|pmp-mocks-1-10|single|predictive|2|At which stage in the Tuckman team development model should the project manager put the most focus on increasing team motivation?|
|`pmp-p3-259`|pmp-mocks-1-10|single|agile|2|A team member approaches the project manager immediately after the end of the project meeting about his need for help to complete…|
|`pmp-p3-260`|pmp-mocks-1-10|single|hybrid|2|A project team often asks questions about the rationale for their work and how the client perceives their progress and…|
|`pmp-p3-261`|pmp-mocks-1-10|single|predictive|3|Project manager B has been assigned to take over a project from Project Manager A. Project Manager A informs Project Manager B…|
|`pmp-p3-262`|pmp-mocks-1-10|single|hybrid|3|A project manager with a directive behavioral orientation is leading a virtual team of software developers. The project manager…|
|`pmp-p3-263`|pmp-mocks-1-10|single|agile|2|A project manager is working with a Scrum team that is continually missing deadlines. The steering committee is concerned about…|
|`pmp-p3-264`|pmp-mocks-1-10|single|predictive|2|A lengthy, ongoing project exhausts a project team. Due to deadline issues, the project manager cannot extend the project…|
|`pmp-p3-265`|pmp-mocks-1-10|single|agile|3|The stakeholders and project team working on an agile project meet with the project manager at the end of an iteration. The…|
|`pmp-p3-266`|pmp-mocks-1-10|single|predictive|2|A team lead is assigned to an ongoing project. The team members have been working together for a while.|
|`pmp-p3-267`|pmp-mocks-1-10|single|predictive|2|A project manager wants to establish an incentive and rewards strategy for the project team.|
|`pmp-p3-268`|pmp-mocks-1-10|single|agile|2|A team member is working on a critical task for a project, and approaches the project manager immediately before the start of a…|
|`pmp-p3-269`|pmp-mocks-1-10|single|agile|3|During the fourth iteration of an agile project, the customer requests a technical change to the development of the project…|
|`pmp-p3-270`|pmp-mocks-1-10|single|agile|2|A new college graduate with a degree in computer science was recently hired by an organization. The new employee has been…|
|`pmp-p3-271`|pmp-mocks-1-10|single|agile|2|A project manager is leading a daily standup meeting when a team member requests that other team members take on some of their…|
|`pmp-p3-272`|pmp-mocks-1-10|multi|agile|3|(Choose 2).|
|`pmp-p3-273`|pmp-mocks-1-10|single|agile|2|A software developer who is working on a critical component has to take emergency leave from the project. Due to time…|
|`pmp-p3-274`|pmp-mocks-1-10|single|predictive|2|Team members express frustration because their functional manager does not listen to their opinions. They seem reluctant to…|
|`pmp-p3-275`|pmp-mocks-1-10|single|predictive|2|A project is in the execution phase when the project manager needs to assign new resources to the existing project team.|
|`pmp-p3-276`|pmp-mocks-1-10|single|predictive|1|In which stage of Tuckman's stages of team development do the team members begin to resolve conflicts effectively?|
|`pmp-p3-277`|pmp-mocks-1-10|single|agile|2|A project manager is managing an agile project where the team is regularly missing user story due dates.|
|`pmp-p3-278`|pmp-mocks-1-10|single|predictive|2|A project manager is committed to the success of a multinational project with tight deadlines. A new team member is continually…|
|`pmp-p3-279`|pmp-mocks-1-10|single|agile|2|A project manager leading an agile project wants to motivate the project team to improve performance.|
|`pmp-p3-280`|pmp-mocks-1-10|single|predictive|2|A project team is regularly missing milestones. Stakeholders are concerned that the project will miss the deliverable due date.…|
|`pmp-p3-281`|pmp-mocks-1-10|single|agile|3|Two agile teams are collaborating on a project. Team A's output is not complete enough for Team B to integrate into their backlog.|
|`pmp-p3-282`|pmp-mocks-1-10|single|predictive|2|A project manager notices schedule performance issues due to the expertise levels of project resources. Some experienced…|
|`pmp-p3-283`|pmp-mocks-1-10|single|hybrid|2|A hybrid project has the following characteristics: It is in the early stages. It has a brand new team. The project manager will…|
|`pmp-p3-284`|pmp-mocks-1-10|single|hybrid|3|A project is using a hybrid approach and team performance is consistently excellent during the predictive stages. During the…|
|`pmp-p3-285`|pmp-mocks-1-10|single|predictive|2|A goal has been set at four months for a project team to distribute a product. Unfortunately, it will be difficult to adhere to…|
|`pmp-p3-286`|pmp-mocks-1-10|single|agile|2|Members from an agile team have been consistently tardy for stand-up meetings and are not meeting project target dates. The…|
|`pmp-p3-287`|pmp-mocks-1-10|single|predictive|2|A project's deliverables due date is approaching, but one team member's performance is negatively impacting the schedule.|
|`pmp-p3-288`|pmp-mocks-1-10|single|predictive|2|A project manager observed that a team member is becoming increasingly disengaged from an ongoing project.|
|`pmp-p3-289`|pmp-mocks-1-10|single|predictive|2|A project manager notices that one team member seems increasingly unhappy during team meetings. The team member is not making…|
|`pmp-p3-290`|pmp-mocks-1-10|single|predictive|2|Early in a project, the project manager realizes that one of the key team members is not prepared to perform the assigned task.|
|`pmp-p3-291`|pmp-mocks-1-10|single|predictive|2|A project manager provided video-based training for new software but several team members reported feeling unprepared to use it.|
|`pmp-p3-292`|pmp-mocks-1-10|single|agile|2|A new project manager joins an existing team. The project manager is made aware that there are strong personalities on the team,…|
|`pmp-p3-293`|pmp-mocks-1-10|single|predictive|2|A project manager's team has been efficiently delivering expected results in an environment where budget and scope are fixed.…|
|`pmp-p3-294`|pmp-mocks-1-10|single|agile|2|In an agile project initiation phase, a senior team member suggests using a new documentation tool based on experience on a…|
|`pmp-p3-295`|pmp-mocks-1-10|single|agile|3|An agile project was scaled from the feasibility study phase to the implementation phase. Due to time constraints, the same team…|
|`pmp-p3-296`|pmp-mocks-1-10|single|hybrid|2|A project is in the execution phase when new team members are onboarded.|
|`pmp-p3-297`|pmp-mocks-1-10|single|predictive|2|A project manager recently took over a project from a project manager who left the company. The new project manager is aware that…|
|`pmp-p3-298`|pmp-mocks-1-10|single|agile|3|A project manager works in an organization where the culture encourages virtual teams. New challenges arise periodically in this…|
|`pmp-p3-299`|pmp-mocks-1-10|single|agile|3|A project manager observes that team members are in a low mood after demos. Feedback from stakeholders is constantly causing…|
|`pmp-p3-300`|pmp-mocks-1-10|multi|hybrid|3|A large transformation project has moved its project management office (PMO) to a virtual environment. There have been…|
|`pmp-p3-301`|pmp-mocks-1-10|single|hybrid|2|A project manager is working on a global project with team members in several different countries. To manage communications, the…|
|`pmp-p3-302`|pmp-mocks-1-10|single|predictive|2|During the execution phase of a project, the project manager is reassigned to another project. A new project manager takes over…|
|`pmp-p3-303`|pmp-mocks-1-10|single|agile|2|A project team is working on a complex technology project. A competitor introduces a product with a new feature, forcing the team…|
|`pmp-p3-304`|pmp-mocks-1-10|single|predictive|2|Regular check-ins with the delivery team reveal low morale and a lack of efficiency.|
|`pmp-p3-305`|pmp-mocks-1-10|single|predictive|2|A team member could not be reached by email or phone during the week that they were absent. The team member had not notified…|
|`pmp-p3-306`|pmp-mocks-1-10|multi|predictive|3|A project manager is preparing to start a project and notices that the project team is having difficulties communicating and…|
|`pmp-p3-307`|pmp-mocks-1-10|single|agile|2|On an agile project, a team member has been taking smaller, easier user stories from the backlog, not those with the highest…|
|`pmp-p3-308`|pmp-mocks-1-10|multi|hybrid|2|A project manager is beginning a new project and is starting to form the project team. The project manager has not worked with…|
|`pmp-p3-309`|pmp-mocks-1-10|single|agile|2|In the middle of a release, the project manager is replaced. Feedback provided by the team to the new project manager hinted at…|
|`pmp-p3-310`|pmp-mocks-1-10|single|agile|2|A software delivery team at a large bank is frustrated by the documentation requirements put in place by the governance…|
|`pmp-p3-311`|pmp-mocks-1-10|single|agile|1|A company is implementing an agile approach for a new project.|
|`pmp-p3-312`|pmp-mocks-1-10|single|predictive|2|A project manager notices that a critical team member has lost interest in the project. The team member's lack of interest has…|
|`pmp-p3-313`|pmp-mocks-1-10|single|agile|2|An agile project team has not completed the tasks within the current iteration. During a meeting, the project sponsor gives the…|
|`pmp-p3-314`|pmp-mocks-1-10|single|agile|2|A developer is expressing concerns about the time spent on daily standups, updating stories, creating tasks, and reviewing…|
|`pmp-p3-315`|pmp-mocks-1-10|single|predictive|3|During a weekly team meeting, the project manager reviewed task progress and found that a senior team member did not follow the…|
|`pmp-p3-316`|pmp-mocks-1-10|single|hybrid|3|In the middle of a software development project following a predictive lifecycle, a significant portion of the software is set…|
|`pmp-p3-317`|pmp-mocks-1-10|single|hybrid|2|During routine progress update sessions for a multinational project, a project manager notes that one of the key technical…|
|`pmp-p3-318`|pmp-mocks-1-10|single|agile|2|The goal on a project is to foster a culture of cooperation that drives the team towards exceptional performance. Following a…|
|`pmp-p3-319`|pmp-mocks-1-10|single|agile|2|In a routine progress check, an agile project manager notices that a lead analyst is habitually passing off less complex analysis…|
|`pmp-p3-320`|pmp-mocks-1-10|single|agile|2|During the second sprint of a software development project, it's observed that one team member frequently dictates scrum…|
|`pmp-p3-321`|pmp-mocks-1-10|single|predictive|2|As the deadline for a critical project approaches, the project manager notices a decrease in team enthusiasm, with several…|
|`pmp-p3-322`|pmp-mocks-1-10|single|hybrid|2|In leading a complex software development project with team members spread across diverse geographical locations, the project…|
|`pmp-p3-323`|pmp-mocks-1-10|single|predictive|2|Following a corporate restructuring, the project manager notices that the newly assembled team is having difficulty adapting to…|
|`pmp-p3-324`|pmp-mocks-1-10|single|predictive|2|After a project milestone, a team member voices concerns over the lack of detailed feedback regarding their project…|
|`pmp-p3-325`|pmp-mocks-1-10|single|agile|1|You have been working on a project where the manager leads in a dictatorship style. Over time, the team have stopped making…|
|`pmp-p3-326`|pmp-mocks-1-10|single|predictive|2|Team members lack enthusiasm for participating in essential project planning sessions, with many arriving late or not…|
|`pmp-p3-327`|pmp-mocks-1-10|single|agile|2|Discovering that one project team member feels their efforts are undervalued compared to others, leading to a decline in their…|
|`pmp-p3-328`|pmp-mocks-1-10|multi|agile|3|During a critical phase of a healthcare software implementation project, the project manager realizes that the project team is…|
|`pmp-p3-329`|pmp-mocks-1-10|single|predictive|1|Following a critical review session of a healthcare software project, a project manager notes that the output from one of the…|
|`pmp-p3-330`|pmp-mocks-1-10|single|predictive|2|In the late stages of launching a marketing campaign project, it's identified that a critical piece of content is not ready…|
|`pmp-p3-331`|pmp-mocks-1-10|single|predictive|1|Near the completion of an extensive software development project, it's noted that one developer's innovative solutions have…|
|`pmp-p3-332`|pmp-mocks-1-10|single|predictive|3|In the midst of executing a high-stakes software development project, several unexpected software bugs and team miscommunications…|
|`pmp-p3-333`|pmp-mocks-1-10|single|predictive|1|You are managing a project team mostly made up of men. Recently Jane was hired for her high expertise in the brands that make up…|
|`pmp-p3-334`|pmp-mocks-1-10|single|agile|3|Your agile project team has only recently formed. The product is still unclear, and there have been disagreements and minor…|
|`pmp-p3-335`|pmp-mocks-1-10|single|predictive|2|Your project team has started to find their rhythm, yet you notice that they seem disengaged in their work and are dragging their…|
|`pmp-p3-336`|pmp-mocks-1-10|single|predictive|1|Your project team came together from different parts of the organisation, and were introduced formally to each other. Now you…|
|`pmp-p3-337`|pmp-mocks-1-10|single|agile|2|You are working as an Agile project manager in a new project team. Many project team members are despondent, and do not show up…|
|`pmp-p3-338`|pmp-mocks-1-10|single|agile|2|Your Agile team leader works through servant leadership to the team, and has built a safe environment for disagreement.|
|`pmp-p3-339`|pmp-mocks-1-10|single|agile|2|New projects go through the Tuckman Model of forming, storming, norming, and performing. You have just taken the lead of a new…|
|`pmp-p3-340`|pmp-mocks-1-10|single|predictive|1|You have been working on a large waterfall project for some time, and one of the business analysts working with you has shown…|
|`pmp-p3-341`|pmp-mocks-1-10|single|predictive|1|You are working in an organisation where a large change is happening to multiple departments. You have been asked to manage this…|
|`pmp-p3-342`|pmp-mocks-1-10|single|predictive|2|You have started as a junior project manager on an internal process improvement project within your existing team. Your program…|
|`pmp-p3-343`|pmp-mocks-1-10|single|predictive|1|In the last few months, your project team have had a large amount of conflict, finding their role and place within the team. They…|
|`pmp-p3-344`|pmp-mocks-1-10|single|predictive|3|The project management office is looking to you for help in hiring and coaching new project managers, and they ask you to…|
|`pmp-p3-345`|pmp-mocks-1-10|single|agile|2|You are leading an Agile team that has members in various locations around the country. You have arranged daily stand-ups via…|
|`pmp-p3-346`|pmp-mocks-1-10|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values.|
|`pmp-p3-347`|pmp-mocks-1-10|single|agile|2|You are working on the requirements for the next iteration and a decision needs to be made by the team. The product owner…|
|`pmp-p3-348`|pmp-mocks-1-10|single|agile|2|You have just taken over a project that is behind schedule. The previous Project Manager was brought on during a company crisis,…|
|`pmp-p3-349`|pmp-mocks-1-10|single|hybrid|2|The project you have taken over recently was behind schedule. It has seen some improvement with some minor releases, but the team…|
|`pmp-p3-350`|pmp-mocks-1-10|single|hybrid|2|You are working as a Project Manager on a project where the Project Sponsor makes all decisions on project requirements and…|
|`pmp-p3-351`|pmp-mocks-1-10|single|predictive|1|You have just been allocated to a project where the team has been working together for the past six months. The Project…|
|`pmp-p3-352`|pmp-mocks-1-10|single|agile|2|The project you are working on is on schedule, but has been marred with conflict and politics for its duration so far. You ask…|

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
|`pmp-p4-1`|pmp-lectures-1-11|single|predictive|4|A project manager conducted an analysis with relevant stakeholders to validate project alignment with organizational strategy and…|
|`pmp-p4-2`|pmp-lectures-1-11|single|predictive|3|During the planning phase of a project, the project manager is having trouble identifying stakeholders. Without input from the…|
|`pmp-p4-3`|pmp-lectures-1-11|single|predictive|3|A project manager has just finished a project charter and received approval from the project sponsor. The project manager then…|
|`pmp-p4-4`|pmp-lectures-1-11|single|agile|3|A project manager receives an email shortly after the beginning of a sprint. It is from a key stakeholder who is unfamiliar with…|
|`pmp-p4-5`|pmp-lectures-1-11|single|predictive|3|During the first few days of executing a six-week project, an impediment is discovered by the project team. The project manager…|
|`pmp-p4-6`|pmp-lectures-1-11|single|predictive|3|A project manager has been assigned to a multimillion-dollar project already in execution. During a project status meeting, one…|
|`pmp-p4-7`|pmp-lectures-1-11|single|predictive|3|A new product development project has been launched. The project sponsor has reviewed the results of the risk analysis as…|
|`pmp-p4-8`|pmp-lectures-1-11|single|predictive|2|A project manager has recently been assigned to an existing multinational project and is not familiar with the key stakeholders.|
|`pmp-p4-9`|pmp-lectures-1-11|single|predictive|2|A project manager is leading a project where a low-level stakeholder has just been promoted to a new position and has now become…|
|`pmp-p4-10`|pmp-lectures-1-11|single|predictive|3|A company is implementing a new computerized system. During project execution, the leader of a department that is critical to…|
|`pmp-p4-11`|pmp-lectures-1-11|single|agile|3|A key stakeholder is upset about the new features in the current iteration for the product. The key stakeholder was unavailable…|
|`pmp-p4-12`|pmp-lectures-1-11|single|predictive|3|An external stakeholder has a significant impact on the project, as they have the ability to approve or reject changes to the…|
|`pmp-p4-13`|pmp-lectures-1-11|single|predictive|3|A project manager is working on a project charter and discovers that two key stakeholders are transferring to other departments…|
|`pmp-p4-14`|pmp-lectures-1-11|single|predictive|3|During project execution, a key stakeholder is replaced with a new team member who has approval authority. The new member states…|
|`pmp-p4-15`|pmp-lectures-1-11|single|predictive|3|A five-year highway construction project is in its third year of execution when the project manager receives a complaint from a…|
|`pmp-p4-16`|pmp-lectures-1-11|single|predictive|3|During a kick-off meeting with the project sponsor and various project members, it became clear that it was not necessary for…|
|`pmp-p4-17`|pmp-lectures-1-11|single|predictive|3|During project implementation, a stakeholder is questioning the purpose of the project and wants to pause the project to assess a…|
|`pmp-p4-18`|pmp-lectures-1-11|single|agile|3|A company is creating a new application for its users. The agile team completed the initiating stage and is performing iterations…|
|`pmp-p4-19`|pmp-lectures-1-11|single|predictive|2|An experienced project manager is mentoring a new project manager assigned to their first project. As the new project manager…|
|`pmp-p4-201`|pmp-mocks-1-10|single|predictive|3|A project manager in the execution phase of a project discovers several stakeholders identified at project initiation are no…|
|`pmp-p4-202`|pmp-mocks-1-10|single|hybrid|3|An ongoing project has a high degree of change.|
|`pmp-p4-203`|pmp-mocks-1-10|single|predictive|3|A project manager has been working on an internal project for the organization's project management office (PMO). The project was…|
|`pmp-p4-204`|pmp-mocks-1-10|single|predictive|3|A project manager is in the execution phase of a project. A newly identified stakeholder continuously bypasses the project…|
|`pmp-p4-205`|pmp-mocks-1-10|single|predictive|3|A project is in the planning phase and requires tremendous time and effort for data collection and analysis. The project sponsor…|
|`pmp-p4-206`|pmp-mocks-1-10|single|agile|2|A senior stakeholder was invited to a sprint review meeting and asked a project team member to describe the final product that…|
|`pmp-p4-207`|pmp-mocks-1-10|single|hybrid|3|A business environment has become unstable because a stakeholder has altered the project deliverables and outcomes in the middle…|
|`pmp-p4-208`|pmp-mocks-1-10|single|predictive|2|A project stakeholder has been missing scheduled facilitation workshops and has delegated assigned tasks to another member of the…|
|`pmp-p4-209`|pmp-mocks-1-10|single|predictive|2|A project manager is developing a project charter, but a key stakeholder is failing to provide information about the expected…|
|`pmp-p4-210`|pmp-mocks-1-10|single|agile|3|A critical software development project has been in development for the past six sprints. A demonstration for stakeholders took…|
|`pmp-p4-211`|pmp-mocks-1-10|single|predictive|2|A complex project has multiple participants. The project manager agrees with the team that they need to better understand the…|
|`pmp-p4-212`|pmp-mocks-1-10|single|predictive|2|A key stakeholder refuses to sign off on a product because it does not meet the expectations outlined in the project scope.|
|`pmp-p4-213`|pmp-mocks-1-10|single|predictive|3|While executing a project that is nearly behind schedule, a project manager must commission a new piece of equipment. Two local…|
|`pmp-p4-214`|pmp-mocks-1-10|single|predictive|1|A project manager compiling a list of project stakeholders discovers an interested stakeholder with little to no authority.|
|`pmp-p4-215`|pmp-mocks-1-10|single|predictive|2|During a project's execution, a previously unidentified stakeholder within the customer's organization requests a change that…|
|`pmp-p4-216`|pmp-mocks-1-10|single|predictive|2|A new project manager joins a project during the delivery of a critical milestone. This project has multiple stakeholders, and…|
|`pmp-p4-217`|pmp-mocks-1-10|single|predictive|2|A project manager must ensure stakeholder responsibilities are clear, concise, and promote successful completion while…|
|`pmp-p4-218`|pmp-mocks-1-10|single|hybrid|3|A project manager is working on a hybrid project. The team performance has been suffering and multiple team members have…|
|`pmp-p4-219`|pmp-mocks-1-10|single|agile|3|A key project stakeholder showed interest in the beginning of a complex agile project but has become less involved as the sprint…|
|`pmp-p4-220`|pmp-mocks-1-10|single|predictive|2|A key stakeholder's input is essential to a project's success. However, the stakeholder is always late on critical project…|
|`pmp-p4-221`|pmp-mocks-1-10|single|predictive|3|During the initial planning phase of a complex development project, a project team has identified several stakeholders, including…|
|`pmp-p4-222`|pmp-mocks-1-10|single|hybrid|2|A project manager is managing a hybrid project. After attending an iteration review, a stakeholder tells the project manager that…|
|`pmp-p4-223`|pmp-mocks-1-10|single|predictive|2|An executive stakeholder has crucial decision power on the execution and success of a project.|
|`pmp-p4-224`|pmp-mocks-1-10|single|predictive|1|After stakeholder data and information collection is complete, an analysis of the collected data begins. The power/interest…|
|`pmp-p4-225`|pmp-mocks-1-10|single|hybrid|2|A project manager is leading an environmental project and realizes that they missed an influential foreign stakeholder. After the…|
|`pmp-p4-226`|pmp-mocks-1-10|single|predictive|1|A project manager is leading a project with seven key stakeholders.|
|`pmp-p4-227`|pmp-mocks-1-10|single|hybrid|2|A project manager sends out requests for regular status meetings and daily standup meetings during the project setup phase.…|
|`pmp-p4-228`|pmp-mocks-1-10|single|predictive|2|During a project steering committee meeting, the project manager reports that a formerly supportive key team member has become…|
|`pmp-p4-229`|pmp-mocks-1-10|single|predictive|2|A project manager has just completed the initiation phase of a project and needs to identify and analyze the stakeholders. The…|
|`pmp-p4-230`|pmp-mocks-1-10|single|agile|2|A long-term agile project has just reached its midway mark and a new project manager is appointed to take over. Additional to the…|
|`pmp-p4-231`|pmp-mocks-1-10|single|hybrid|2|A project manager is leading a complex initiative with diverse stakeholders and must cultivate strong stakeholder relationships.…|
|`pmp-p4-232`|pmp-mocks-1-10|single|predictive|2|A project manager has identified stakeholders for a software development project: the sales team will be the primary users of the…|
|`pmp-p4-233`|pmp-mocks-1-10|single|predictive|2|A project is experiencing delays because cultural differences are causing misunderstandings among some stakeholders.|
|`pmp-p4-234`|pmp-mocks-1-10|single|agile|3|A project manager for an important agile project has a key stakeholder who was actively involved at the beginning of the project.…|
|`pmp-p4-235`|pmp-mocks-1-10|single|predictive|2|Midway through a project, the project manager identifies new stakeholders. Each of these new stakeholders plays a different…|
|`pmp-p4-236`|pmp-mocks-1-10|single|predictive|1|A project is in development when a functional manager leaves the company and is no longer involved with the project.|
|`pmp-p4-237`|pmp-mocks-1-10|single|predictive|1|A project manager in a global organization is responsible for a project involving six different companies. Midway through the…|
|`pmp-p4-238`|pmp-mocks-1-10|single|predictive|2|A project manager is having difficulty managing stakeholders because the stakeholder register does not distinguish how aware of…|
|`pmp-p4-239`|pmp-mocks-1-10|single|agile|2|A project manager is attempting to engage project stakeholders in a new project and has invited them to daily standup meetings.…|
|`pmp-p4-240`|pmp-mocks-1-10|single|agile|2|An agile project that the current project manager has been managing for three years is replacing one of the stakeholders with one…|
|`pmp-p4-241`|pmp-mocks-1-10|single|predictive|2|A project manager is leading a complex infrastructure project that is vital for the company's growth. During the project's…|
|`pmp-p4-242`|pmp-mocks-1-10|single|agile|3|During the execution of a project, the finance team identified that they were not involved in building the business case for the…|
|`pmp-p4-243`|pmp-mocks-1-10|single|agile|2|An agile project manager is leading a new software system development project that is in the user acceptance testing phase. The…|
|`pmp-p4-244`|pmp-mocks-1-10|multi|hybrid|2|A large transformation project that requires cross-functional effort from various business teams is in progress. (Choose two)|
|`pmp-p4-245`|pmp-mocks-1-10|single|agile|2|A director is acting as the product owner for a government software project. The director is repeatedly unable to engage with the…|
|`pmp-p4-246`|pmp-mocks-1-10|multi|predictive|2|A project is close to completion when an unidentified and influential stakeholder appears and threatens to put the project in…|
|`pmp-p4-247`|pmp-mocks-1-10|single|agile|1|At what point in an agile project should a project manager engage stakeholders?|
|`pmp-p4-248`|pmp-mocks-1-10|single|predictive|2|A project manager is managing a construction project that is 40% through the execution phase. The project sponsor has asked the…|
|`pmp-p4-249`|pmp-mocks-1-10|single|predictive|1|A project team is creating a stakeholder engagement assessment matrix to govern stakeholder management activities during project…|
|`pmp-p4-250`|pmp-mocks-1-10|single|agile|2|A project manager is concerned about delivering the planned business value because the product owner is not engaged.|
|`pmp-p4-251`|pmp-mocks-1-10|single|hybrid|3|Several items remain in the Kanban blocked column despite the team’s efforts to address them. The retrospective reveals that all…|
|`pmp-p4-252`|pmp-mocks-1-10|single|predictive|2|A functional manager who is also a project stakeholder has time constraints and delegates project responsibilities to someone…|
|`pmp-p4-253`|pmp-mocks-1-10|single|agile|3|An R&D executive, who is a project stakeholder, occasionally visits the agile team’s work area to give instructions about urgent…|
|`pmp-p4-254`|pmp-mocks-1-10|single|predictive|2|A new project team is setting early priorities, and they plan to gain the customer's trust by delivering value as quickly as…|
|`pmp-p4-255`|pmp-mocks-1-10|single|predictive|2|A project manager overseeing the launch of a new e-commerce platform discovers that the finance department has concerns about not…|
|`pmp-p4-256`|pmp-mocks-1-10|single|predictive|3|A project manager is overseeing the construction of a new office building. A key stakeholder complains that they never received…|
|`pmp-p4-257`|pmp-mocks-1-10|single|predictive|2|During a pivotal project phase, it was revealed that critical stakeholders did not attend a meeting focused on confirming the…|
|`pmp-p4-258`|pmp-mocks-1-10|single|predictive|2|At the beginning of a new environmental conservation project, a key stakeholder expresses concerns over the project's approach,…|
|`pmp-p4-259`|pmp-mocks-1-10|single|hybrid|2|In the development phase of an international public health project, a project manager faces skepticism from government partners…|
|`pmp-p4-260`|pmp-mocks-1-10|single|predictive|2|As a project manager overseeing the development of a new line of eco-friendly packaging materials, you encounter pushback from…|
|`pmp-p4-261`|pmp-mocks-1-10|single|predictive|3|Your stakeholders seem to be engaged, however there are frequent changes to scope and requirements, causing delays. Issues are…|
|`pmp-p4-262`|pmp-mocks-1-10|single|predictive|2|A project manager is leading the development of a new public park in a densely populated urban area. Community leaders have…|
|`pmp-p4-263`|pmp-mocks-1-10|single|predictive|2|A project manager is leading the development of a new public library. During the planning phase, it comes to light that the…|
|`pmp-p4-264`|pmp-mocks-1-10|single|predictive|2|At the outset of an initiative to introduce smart city technologies, it's discovered that the input from a crucial environmental…|
|`pmp-p4-265`|pmp-mocks-1-10|single|predictive|2|You are delivering a critical change and several key stakeholders in your organisation have added seemingly unnecessary steps for…|
|`pmp-p4-266`|pmp-mocks-1-10|single|predictive|2|You are analysing your stakeholder through an impact over influence chart and have found more than 100 different groups of…|
|`pmp-p4-267`|pmp-mocks-1-10|single|predictive|2|You are working on a change that includes stakeholders at all levels from front-line workers to senior executives. After…|
|`pmp-p4-268`|pmp-mocks-1-10|single|predictive|2|The functional manager raises a concern that they have not been engaged correctly and the program is missing some key…|
|`pmp-p4-269`|pmp-mocks-1-10|single|predictive|2|Your project has been in progress for the past six months. Someone you have not met before approaches you and says they have not…|
|`pmp-p4-270`|pmp-mocks-1-10|single|predictive|2|You have been working with the stakeholders on your project for some time, and the communication has been getting more and more…|
|`pmp-p4-271`|pmp-mocks-1-10|single|predictive|2|You have been working on a project in your organisation for some time. In the latest working group meeting, the project sponsor…|
|`pmp-p4-272`|pmp-mocks-1-10|single|predictive|1|You are creating a high level project charter which has now been approved. The Project Sponsor expects you to get to work…|
|`pmp-p4-273`|pmp-mocks-1-10|single|predictive|2|The project charter has been approved, you have identified the stakeholders you will be working with and who will be impacted. As…|
|`pmp-p4-274`|pmp-mocks-1-10|single|predictive|2|Your project charter has been approved and your stakeholder analysis reveals multiple impacted highly paid executives with the…|
|`pmp-p4-275`|pmp-mocks-1-10|single|agile|2|You are at the beginning of a new iteration and about to plan and decide what gets worked on in the upcoming sprint. You decide…|
|`pmp-p4-276`|pmp-mocks-1-10|single|hybrid|3|During a retrospective, your team advise that a high ranking executive from another part of the business keep approaching them to…|
|`pmp-p4-277`|pmp-mocks-1-10|single|predictive|1|You are working on a project to deliver a major system change to retail workers in a large department store. You have worked…|
|`pmp-p4-278`|pmp-mocks-1-10|single|predictive|1|You are in the initiating stages of a project, and have been given an initial project team. Your team are gathering the project…|

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
|`pmp-p5-1`|pmp-lectures-1-11|single|agile|4|An agile project is in its first of 12 iterations, and each iteration lasts two weeks. In the first iteration review, the…|
|`pmp-p5-2`|pmp-lectures-1-11|single|agile|3|The product owner has asked the project manager and the agile team to demonstrate the completed functionality to key project…|
|`pmp-p5-3`|pmp-lectures-1-11|single|agile|3|A company has started doing agile projects. The project sponsor is frustrated and does not understand the new requirements…|
|`pmp-p5-4`|pmp-lectures-1-11|single|predictive|3|A company used to deliver technical services to customers using external labor. When a new project manager arrived, the team…|
|`pmp-p5-5`|pmp-lectures-1-11|single|agile|3|The project manager of an agile project notices that many stakeholders are skipping iteration planning and review meetings. Some…|
|`pmp-p5-6`|pmp-lectures-1-11|single|predictive|3|An organizational transformation project has two workstreams running in parallel between corporate headquarters and an overseas…|
|`pmp-p5-7`|pmp-lectures-1-11|single|predictive|3|While gathering requirements for the project scope, the project manager realizes that some of the stakeholder requirements have a…|
|`pmp-p5-201`|pmp-mocks-1-10|single|agile|2|An agile team is planning for a 2-week iteration. In the middle of the last few iterations, the product owner made changes and…|
|`pmp-p5-202`|pmp-mocks-1-10|single|hybrid|2|In a software development project, a stakeholder wants to add a major requirement, which they feel will be a great benefit to the…|
|`pmp-p5-203`|pmp-mocks-1-10|single|predictive|2|A project manager has been assigned to a new project with multiple high-level stakeholders. Each stakeholder has provided their…|
|`pmp-p5-204`|pmp-mocks-1-10|single|agile|3|A senior business stakeholder with many years of experience is pressuring an agile team to deliver most of the promised…|
|`pmp-p5-205`|pmp-mocks-1-10|single|predictive|3|Midway through project execution, several stakeholders raise concerns about team performance and delivery. The project manager…|
|`pmp-p5-206`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a complex project that includes many stakeholders with conflicting priorities.|
|`pmp-p5-207`|pmp-mocks-1-10|single|agile|3|Following the start of a sprint, a key stakeholder constructed an email to a project team voicing displeasure of the work that…|
|`pmp-p5-208`|pmp-mocks-1-10|single|predictive|3|A key supplier has become unavailable and the project manager has found a replacement that will meet project approval. Bringing…|
|`pmp-p5-209`|pmp-mocks-1-10|single|predictive|2|In the early stages of a project, two key stakeholders have different opinions about a project's goals.|
|`pmp-p5-210`|pmp-mocks-1-10|single|predictive|2|While preparing a project charter for a new project, a project manager reviews lessons learned from previous projects. The…|
|`pmp-p5-211`|pmp-mocks-1-10|multi|hybrid|3|A project manager is working on a complex project. The project includes many stakeholders with conflicting priorities. (Choose…|
|`pmp-p5-212`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a complex, political project with many stakeholders having conflicting priorities. Senior…|
|`pmp-p5-213`|pmp-mocks-1-10|single|predictive|2|A project manager is working on a complex project with multiple stakeholders and competing priorities.|
|`pmp-p5-214`|pmp-mocks-1-10|single|predictive|2|A project manager is working on a complex project with multiple stakeholders and competing priorities. During a meeting with the…|
|`pmp-p5-215`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a complex project that requires expertise in negotiation skills.|
|`pmp-p5-216`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a complex project with many stakeholders that have conflicting priorities. Senior management…|
|`pmp-p5-217`|pmp-mocks-1-10|single|agile|3|After monitoring a product’s development after the first three releases, the product owner wants to launch a product even though…|
|`pmp-p5-218`|pmp-mocks-1-10|single|predictive|3|During a project status meeting, the project manager discovers that stakeholders have different expectations that are not…|
|`pmp-p5-219`|pmp-mocks-1-10|single|predictive|3|The company's top executives are embarking on a pivotal project that holds immense importance for the organization's future. This…|
|`pmp-p5-220`|pmp-mocks-1-10|single|predictive|2|A project manager has been assigned to a new project. The client has mentioned that the project's inception is based on ideas and…|
|`pmp-p5-221`|pmp-mocks-1-10|single|agile|2|A stakeholder that is not experienced with agile has emailed the project team expressing frustration and displeasure with their…|
|`pmp-p5-222`|pmp-mocks-1-10|single|predictive|2|A multinational company with a cross-border project wants to implement new software that will impact several departments. The…|
|`pmp-p5-223`|pmp-mocks-1-10|single|predictive|3|A project team lead meets with the project manager before a scheduled kick-off meeting. The team lead is worried that…|
|`pmp-p5-224`|pmp-mocks-1-10|single|predictive|3|A project manager is working on an complex project with multiple stakeholders and competing priorities. Each stakeholder wants to…|
|`pmp-p5-225`|pmp-mocks-1-10|single|agile|3|A project manager is leading a key flagship solution initiative prioritizing agility, market adaptation, and rapid response to…|
|`pmp-p5-226`|pmp-mocks-1-10|single|predictive|2|A project manager learns that a few stakeholders have a disagreement regarding the results of one project deliverable.|
|`pmp-p5-227`|pmp-mocks-1-10|single|predictive|3|A project manager is planning the construction of a new public library. The community and project stakeholders are having…|
|`pmp-p5-228`|pmp-mocks-1-10|single|agile|2|You are working on an Agile project where the product business representative is pushing for more and more features to be…|
|`pmp-p5-229`|pmp-mocks-1-10|single|agile|2|While meeting with the customers or business owners, they introduce several pieces of new functionality into the product backlog…|
|`pmp-p5-230`|pmp-mocks-1-10|single|hybrid|2|Your project has just had its first release of a deliverable, which has taken 4 months to deliver. The team showcased it to the…|

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
|`pmp-p6-1`|pmp-lectures-1-11|single|agile|3|A company has hired a project manager to implement an adaptive project framework. The project has made some progress, but the…|
|`pmp-p6-2`|pmp-lectures-1-11|single|agile|3|An agile team is developing a product with strict budget constraints. The customer is requesting a new independent feature be…|
|`pmp-p6-3`|pmp-lectures-1-11|single|agile|3|Even though the customer is receiving sprint velocity and results reports from the project manager by email, escalations to the…|
|`pmp-p6-4`|pmp-lectures-1-11|single|agile|3|A client has contracted a company to manage a large project in an agile environment with many subcontractors. The client is…|
|`pmp-p6-201`|pmp-mocks-1-10|single|agile|2|A project manager is involved in a project where requirements are constantly evolving.|
|`pmp-p6-202`|pmp-mocks-1-10|single|predictive|2|As part of a project deliverable, a project manager demonstrates how to use one of the products to a subset of a client's…|
|`pmp-p6-203`|pmp-mocks-1-10|single|hybrid|2|In a hybrid project, the need for customer involvement throughout the project is not understood by the customer. As a…|
|`pmp-p6-204`|pmp-mocks-1-10|single|hybrid|3|During one of the final incremental deliveries of a project, the end user informed the project leader that they are not satisfied…|
|`pmp-p6-205`|pmp-mocks-1-10|single|predictive|2|A project has been closed and customer care needs to measure customer satisfaction with the deliverables.|
|`pmp-p6-206`|pmp-mocks-1-10|single|agile|3|A customer is concerned that the current project deliverable will not fulfill stated project requirements.|
|`pmp-p6-207`|pmp-mocks-1-10|single|agile|3|A complex technology project is in the execution phase. New technologies are added to the backlog based on customer requirements,…|
|`pmp-p6-208`|pmp-mocks-1-10|single|agile|3|During a daily meeting, the project team raised an impediment that one of the business subject matter experts is frequently…|
|`pmp-p6-209`|pmp-mocks-1-10|single|hybrid|3|The team informed the project leader that they are not sure of the stakeholder needs, despite the multiple collection…|
|`pmp-p6-210`|pmp-mocks-1-10|single|predictive|2|An operational support project is running smoothly, but a customer consistently complains about a lack of visibility into the…|
|`pmp-p6-211`|pmp-mocks-1-10|single|predictive|2|A customer requires knowing exactly the state of a product, the overall project, and the next steps planned for the next month…|
|`pmp-p6-212`|pmp-mocks-1-10|single|predictive|2|Multiple stakeholders express dissatisfaction with a project deliverable.|
|`pmp-p6-213`|pmp-mocks-1-10|single|agile|3|A project manager is assigned a project for a customer with a history of being demanding and unclear about requirements. The…|
|`pmp-p6-214`|pmp-mocks-1-10|single|hybrid|2|A project manager is on a project with a very involved customer. The customer demands regular updates on the project, including…|
|`pmp-p6-215`|pmp-mocks-1-10|single|predictive|3|During a meeting with stakeholders, the project manager received multiple concerns about the quality of the project’s…|
|`pmp-p6-216`|pmp-mocks-1-10|single|agile|2|A project manager receives an email from a customer detailing reservations about the project deliverables.|
|`pmp-p6-217`|pmp-mocks-1-10|single|agile|2|A customer is not satisfied with the delivered product, saying that it was not what they expected. The project manager is…|
|`pmp-p6-218`|pmp-mocks-1-10|single|agile|3|A customer repeatedly complains, saying that a function developed by the development team does not seem to align with the…|
|`pmp-p6-219`|pmp-mocks-1-10|single|hybrid|3|During a challenging shareholder meeting, the customer indicates that they will reject an iteration's deliverable.|
|`pmp-p6-220`|pmp-mocks-1-10|single|predictive|2|A customer's resources have not completed the required training. The project manager has to consider the risk it poses to the…|
|`pmp-p6-221`|pmp-mocks-1-10|single|predictive|2|A customer is performing a weekly visit to a project site to inspect the final stage of product development. The quality manager…|
|`pmp-p6-222`|pmp-mocks-1-10|single|agile|2|During a one-year project, a team delivered everything as requested and defined by the product owner and met all of the sprint…|
|`pmp-p6-223`|pmp-mocks-1-10|single|agile|2|While working on a long project with many changes, a project manager is informed by a team member that some users are…|
|`pmp-p6-224`|pmp-mocks-1-10|multi|agile|3|A stakeholder on an agile project has attended the sprint demo where the team shows the minimum viable product (MVP) as defined…|

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
|`pmp-p7-1`|pmp-lectures-1-11|single|predictive|3|A project manager is working on a new healthcare management application. One of the team members will be leaving the organization…|
|`pmp-p7-2`|pmp-lectures-1-11|single|predictive|2|During project execution, the project sponsor decides to replace a subject matter expert (SME) on the project.|
|`pmp-p7-3`|pmp-lectures-1-11|single|agile|4|A group of agile team members have just completed in-person training for a number of critical and complex organizational…|
|`pmp-p7-201`|pmp-mocks-1-10|single|predictive|2|A multi-year project is underway when a key resource announces that they are retiring. The project manager is concerned about the…|
|`pmp-p7-202`|pmp-mocks-1-10|single|predictive|3|A project manager has just taken over a 5-year project that is at the midpoint. At the steering committee meeting, the CEO asked…|
|`pmp-p7-203`|pmp-mocks-1-10|single|agile|2|A project manager working in an agile environment realizes that some new team members are having difficulty understanding how…|
|`pmp-p7-204`|pmp-mocks-1-10|single|predictive|2|In a project where a third-party provider is implementing a custom solution, the project manager must ensure that the local team…|
|`pmp-p7-205`|pmp-mocks-1-10|single|agile|2|A dispersed project team has been working together on a long-term development project. The team just finished an iteration when a…|
|`pmp-p7-206`|pmp-mocks-1-10|single|predictive|3|A new project manager is hired to replace a project manager who was recently transferred to another project. The team members…|
|`pmp-p7-207`|pmp-mocks-1-10|single|predictive|2|An events company is planning a new event for a client that holds biannual conferences. The project is being managed by a new…|
|`pmp-p7-208`|pmp-mocks-1-10|single|predictive|2|In a functional organization, a product analyst is assigned to a short-duration application development effort. The analyst…|
|`pmp-p7-209`|pmp-mocks-1-10|single|predictive|2|A project manager needs to close out a project and does not have time to update the lessons learned register.|
|`pmp-p7-210`|pmp-mocks-1-10|single|hybrid|3|A project manager recognizes the need to improve team dynamics and knowledge sharing during a megaproject.|
|`pmp-p7-211`|pmp-mocks-1-10|single|predictive|1|A pilot project is ready to close and the project team wants to capture the challenges that were overcome and the opportunities…|
|`pmp-p7-212`|pmp-mocks-1-10|single|predictive|2|A project manager just completed a project in an organization that values lessons learned. Leadership encourages all the project…|
|`pmp-p7-213`|pmp-mocks-1-10|single|predictive|2|After an annual staff review, discussing project management and soft skills knowledge, the company made a decision to train all…|
|`pmp-p7-214`|pmp-mocks-1-10|single|predictive|1|A project manager is consolidating project documentation such as videos, pictures, audio, to ensure the team is sharing out…|
|`pmp-p7-215`|pmp-mocks-1-10|multi|predictive|3|A project manager is assigned to a project that is already in progress because the original project manager left the organization…|
|`pmp-p7-216`|pmp-mocks-1-10|single|agile|3|Operations for a new technology-based project are being transitioned to a new project team. Early in the execution phase, the…|
|`pmp-p7-217`|pmp-mocks-1-10|single|predictive|2|A project manager has been assigned to a new project that revisits a previously completed project.|
|`pmp-p7-218`|pmp-mocks-1-10|single|hybrid|2|A project manager is new to the organization and is assigned to replace the project manager of an active project.|
|`pmp-p7-219`|pmp-mocks-1-10|single|predictive|1|Upon completion of a long-term contract, the project manager confirms with the release manager that all planned features are…|
|`pmp-p7-220`|pmp-mocks-1-10|single|predictive|2|A project manager realizes that some newly hired team members have not been as productive as expected. The project has a very…|
|`pmp-p7-221`|pmp-mocks-1-10|single|predictive|3|A project manager is preparing for an organization-wide software system replacement project that will have a significant impact…|
|`pmp-p7-222`|pmp-mocks-1-10|single|predictive|2|A project manager is overseeing the development of a new healthcare facility. The project team is struggling to understand some…|
|`pmp-p7-223`|pmp-mocks-1-10|single|predictive|2|During a crucial phase of the project, a pivotal team member informs the project manager of their impending resignation,…|
|`pmp-p7-224`|pmp-mocks-1-10|single|agile|2|During a critical phase of an agile development project, a specialist responsible for designing a key component is unexpectedly…|
|`pmp-p7-225`|pmp-mocks-1-10|single|hybrid|3|During the critical phase of a remote-operated environmental research project, a key analyst departs, necessitating the swift…|
|`pmp-p7-226`|pmp-mocks-1-10|multi|hybrid|3|In the midst of deploying a major IT infrastructure update, a project manager identifies a significant knowledge gap within the…|
|`pmp-p7-227`|pmp-mocks-1-10|single|predictive|2|In finalizing an infrastructure upgrade project, the project manager needs to ensure that all critical operational knowledge has…|
|`pmp-p7-228`|pmp-mocks-1-10|single|agile|2|You are working on an agile project where there are vastly differing levels of product knowledge within your team. The scrum…|
|`pmp-p7-229`|pmp-mocks-1-10|single|agile|1|As an Agile project manager, you understand the need to operate with openness and transparency with your team and stakeholders.|
|`pmp-p7-230`|pmp-mocks-1-10|single|predictive|2|You are nearing the end of your project which has delivered a brand new system to the organization. There have been many…|

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

## Process

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

## Business Environment

### business-1 · Governance — 80 items

*Set the structures, thresholds and escalation paths the project runs under.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b1-001`|core|matching|hybrid|3|Prompts: Moving 15,000 between two work packages,… / Approving a change request that would ta… / Releasing funds held outside the cost ba…|
|`pmp-b1-002`|core|single|predictive|3|Your project's approved schedule tolerance is two weeks. Your current forecast shows a slip of nine days, and your mitigation…|
|`pmp-b1-003`|core|multi|predictive|3|Your project has a steering committee that meets monthly, a technical design authority that meets fortnightly, and a sponsor…|
|`pmp-b1-004`|core|single|hybrid|4|Your hybrid programme's governance requires board approval for any scope change above £25,000. The product teams make small scope…|
|`pmp-b1-005`|core|single|predictive|3|Your governance framework requires steering group approval for any change over £50,000 or affecting the delivery date. A proposed…|
|`pmp-b1-006`|core|single|hybrid|3|Your programme's steering group has not met for four months, as members keep withdrawing. In that period three decisions have…|
|`pmp-b1-007`|core|single|predictive|3|Your project is delivered for an external client and runs under two governance frameworks. Your own organisation requires a…|
|`pmp-b1-021`|core|single|agile|3|Your product team is funded quarterly against agreed outcomes and guardrails. Mid-quarter, the product owner wants to pivot the…|
|`pmp-b1-022`|core|single|agile|2|A controlling project office requires every project to submit a weekly Gantt chart. Your team works in two-week sprints from a…|
|`pmp-b1-023`|core|single|agile|4|One team serves two business units, and each unit has named its own product owner. Every sprint planning session ends in a…|
|`pmp-b1-024`|core|single|hybrid|3|A hybrid medical device programme has a gate that requires a complete design before manufacturing tooling is ordered. The device…|
|`pmp-b1-025`|core|multi|agile|3|A product team is funded for a year to deliver an agreed outcome, within a fixed budget and release date. Select two.|
|`pmp-b1-026`|core|single|hybrid|4|You learn that a member of your hybrid programme's steering group has been telling the integration supplier directly to add…|
|`pmp-b1-061`|core|graphic|predictive|3|Under your organisation's delegation policy, any contract above 250,000 is awarded on the sponsor's authority, and the…|
|`pmp-case3-001`|case-03|single|hybrid|4|The integration team's product owner wants to reorder the interface backlog so that ward requesting is delivered before the…|
|`pmp-b1-101`|business-1|single|predictive|3|You are managing a £3.2m replacement of a ferry operator's online booking platform, now in its fourth month of build. The…|
|`pmp-b1-102`|business-1|single|agile|1|A regional director at an online grocery retailer messages one of your Scrum team's developers directly, asking him to build a…|
|`pmp-b1-103`|business-1|single|hybrid|4|Your hybrid programme for an energy supplier's customer portal produces tested increments every two weeks. Programme governance…|
|`pmp-b1-104`|business-1|single|predictive|3|You take over a hospital estates project midway through construction. Reviewing the files, you find that your predecessor…|
|`pmp-b1-105`|business-1|multi|agile|3|Three Scrum teams are rebuilding a public benefits agency's online claims service. The steering committee, used to predictive…|
|`pmp-b1-106`|business-1|single|predictive|5|A water utility's steering group terms of reference set a quorum of four members, including the finance director, and allow…|
|`pmp-b1-107`|business-1|single|agile|3|An airline's loyalty platform is funded through a lean portfolio. Its guardrails let product teams progress any epic forecast…|
|`pmp-b1-108`|business-1|single|hybrid|2|You act as secretary to a bus operator's ticketing programme board. Its terms of reference give you, on the chair's behalf,…|
|`pmp-b1-109`|business-1|single|predictive|3|Your project is refurbishing a station concourse while trains continue to run. The governance plan allows the project manager to…|
|`pmp-b1-110`|business-1|single|agile|4|A retail bank's governance framework requires a full business case, approved at a gate, for any initiative costing over £30,000.…|
|`pmp-b1-111`|business-1|single|predictive|4|Last month the project board for a pharmaceutical distributor's warehouse upgrade chose to refurbish the existing racking for…|
|`pmp-b1-112`|business-1|single|agile|3|The named product owner for a logistics firm's route-planning app is the operations director, who can give the team one hour a…|
|`pmp-b1-113`|business-1|multi|hybrid|3|A city council and a university are jointly funding a €1.2m air-quality sensor network, half each, which you will deliver with a…|
|`pmp-b1-114`|business-1|matching|agile|2|Prompts: Business stakeholders want to try out th… / The team wants to change how it hands wo… / Midway through the sprint, a developer f…|
|`pmp-b1-115`|business-1|matching|predictive|3|Prompts: Board members argue at each meeting abou… / Two workstreams each assumed the other w… / The board approves each stage plan witho…|
|`pmp-b1-116`|business-1|pulldown|hybrid|3|Prompts: To save cost, the sponsor suggests that… / A board member discloses that her brothe…|
|`pmp-b1-117`|business-1|fillblank|predictive|2|The project board for a rail signalling upgrade set the current stage a budget of €840,000 with a cost tolerance of +8%. To date…|
|`pmp-b1-118`|business-1|hotspot|hybrid|4|On a hybrid programme, a supplier-insolvency risk has been scored 20 out of 25 on a team log for three months, and the PMO…|
|`pmp-b1-119`|business-1|single|predictive|1|Your governance plan for a city library service's self-service kiosk rollout requires a highlight report to the project board on…|
|`pmp-b1-120`|business-1|single|predictive|5|Last week a port authority's steering group, which holds authority for contract awards, approved awarding a £2.4m…|
|`pmp-b1-121`|business-1|single|predictive|3|You are preparing the business case for a parcel carrier's depot rostering system, due at next week's gate. It claims £400,000 a…|
|`pmp-b1-122`|business-1|multi|predictive|3|A museum's gallery redevelopment board has grown to fourteen regular attendees as departments asked to be included. At the last…|
|`pmp-b1-123`|business-1|matching|predictive|3|Prompts: All exit criteria are met, the business… / All criteria are met except the fire eng… / The planning condition that must be disc…|
|`pmp-b1-124`|business-1|fillblank|predictive|3|The governance plan for a stadium floodlight upgrade lets the project manager approve changes of up to 20,000 each, to a…|
|`pmp-b1-125`|business-1|hotspot|predictive|3|On a project installing a new production line at a food factory, three delivered equipment modules have waited six weeks for…|
|`pmp-b1-126`|business-1|single|agile|3|A building society's security standard, owned by its chief information security officer, requires penetration testing before any…|
|`pmp-b1-127`|business-1|single|agile|4|Your Scrum team at a travel start-up wants to integrate a flight-pricing API this sprint. The provider's terms require a…|
|`pmp-b1-128`|business-1|single|agile|3|At a games studio, finance capitalises development costs on new titles, and the external auditors require records showing the…|
|`pmp-b1-129`|business-1|single|agile|2|Your team's release burn-up for a broadband provider's self-install app is published on the company wiki, as the programme's…|
|`pmp-b1-130`|business-1|multi|agile|2|A garden-centre chain is moving its e-commerce team to Scrum. Company policy says hiring sits with line managers, any purchase…|
|`pmp-b1-131`|business-1|single|agile|3|A car-sharing company's app is built by three Scrum teams, each with its own product owner ordering a separate backlog. An…|
|`pmp-b1-132`|business-1|single|hybrid|1|You have just been appointed to lead a hybrid programme building a visitor-booking system for a national park authority, using…|
|`pmp-b1-133`|business-1|single|hybrid|4|On a hybrid programme replacing a national charity's donor database, the governance plan sets your approval limit at £10,000 and…|
|`pmp-b1-134`|business-1|multi|hybrid|4|On a hybrid programme at a regional airport, the board has failed at three consecutive meetings to choose between two…|
|`pmp-b1-135`|business-1|pulldown|hybrid|3|Prompts: At the stage review, the board must judg… / The board must judge whether the supplie…|
|`pmp-b1-1`|pmp-lectures-1-11|single|hybrid|3|A project manager in a quickly growing hybrid organization is becoming increasingly frustrated at the rapid amount of change in…|
|`pmp-b1-2`|pmp-lectures-1-11|single|agile|3|A project manager has been assigned to an existing agile team and is preparing for an upcoming project. The last project that the…|
|`pmp-b1-3`|pmp-lectures-1-11|single|agile|4|An organization is trying to adopt agile approaches to execute projects for their customers. During project planning, the product…|
|`pmp-b1-4`|pmp-lectures-1-11|single|predictive|4|A project can gain downstream marketing benefits if additional equipment is included in the project scope. However, this…|
|`pmp-b1-201`|pmp-mocks-1-10|single|hybrid|3|As part of a temporary assignment, a project manager is leading a business project. During a daily status meeting, it was…|
|`pmp-b1-202`|pmp-mocks-1-10|single|predictive|1|After completing the project charter, the project manager sends it to the project stakeholders.|
|`pmp-b1-203`|pmp-mocks-1-10|single|predictive|3|During a kick-off meeting, the sponsor informs the project team that they can not attend any meetings for the first half of the…|
|`pmp-b1-204`|pmp-mocks-1-10|single|predictive|4|After several interviews with key stakeholders and checking the project charter, a project manager found that the current project…|
|`pmp-b1-205`|pmp-mocks-1-10|single|predictive|2|A project manager is working on the development of a business case to secure funding and resources.|
|`pmp-b1-206`|pmp-mocks-1-10|single|predictive|3|A project manager is tracking an infrastructure project that is dependent on a service provider. The service provider informed…|
|`pmp-b1-207`|pmp-mocks-1-10|single|predictive|1|A project team member assigned to a new project will be required to report on all project tasks progress to the project manager…|
|`pmp-b1-208`|pmp-mocks-1-10|single|predictive|2|A project manager is leading a project to develop a new system for a government agency using a predictive approach. The project's…|
|`pmp-b1-209`|pmp-mocks-1-10|single|hybrid|2|A project manager is on a hybrid project. The project manager meets with the sponsor to get the project milestones. The sponsor…|
|`pmp-b1-210`|pmp-mocks-1-10|single|predictive|4|A project manager is assigned to join a several year project mid-way. The project was stopped years ago due to organizational…|
|`pmp-b1-211`|pmp-mocks-1-10|single|predictive|3|A megaproject had reached 10% completion when there was a sudden change of company ownership. After being put on hold for a few…|
|`pmp-b1-212`|pmp-mocks-1-10|single|predictive|3|Leading a significant initiative to overhaul a corporation's IT infrastructure, the project manager has already navigated through…|
|`pmp-b1-213`|pmp-mocks-1-10|single|predictive|2|During one of your working group meetings, a stakeholder raises the concern that the product you are working on will be…|
|`pmp-b1-214`|pmp-mocks-1-10|single|predictive|2|You are working directly with the project sponsor, in the initial phase of a brand new project. The project sponsor asks for your…|
|`pmp-b1-215`|pmp-mocks-1-10|single|agile|2|Your organisation is starting to move to an agile way of work and they are setting up a Value Delivery Office (VDO) to complement…|
|`pmp-b1-216`|pmp-mocks-1-10|single|predictive|2|Your project team have planned the requirements, scope, schedule and cost for constructing a new building in your organisation.…|
|`pmp-b1-217`|pmp-mocks-1-10|single|predictive|1|You are new to a project and are approached by the functional manager. No one has been able to tell them exactly what the project…|
|`pmp-b1-218`|pmp-mocks-1-10|single|predictive|2|You are working as a project manager in an organisation with a new Project Management Office. They advise all project managers to…|
|`pmp-b1-219`|pmp-mocks-1-10|single|predictive|2|You have just joined an organisation, and are about to be allocated to an existing project, delivering a new Human Resources…|
|`pmp-b1-220`|pmp-mocks-1-10|single|predictive|2|You have just started work in a project that has been in development for the past six months. You notice it has been through a…|
|`pmp-b1-221`|pmp-mocks-1-10|single|predictive|1|You are a project manager brought on during the very early phase of the project. The project sponsor has asked you to create a…|
|`pmp-b1-222`|pmp-mocks-1-10|single|predictive|1|You have recently become a project manager for a large organisation and you are not sure what to expect. Your colleague tells you…|
|`pmp-b1-223`|pmp-mocks-1-10|single|hybrid|2|You are a Project Manager who has been tasked with initiating a new project for your area. You review the current organizational…|
|`pmp-b1-224`|pmp-mocks-1-10|single|predictive|1|You are working as a project manager, asking for proposals from a Third Parties to complete the work. Your brother works for one…|
|`pmp-b1-225`|pmp-mocks-1-10|single|predictive|2|You are working on a global project in other country, where it is customary to honour the project manager of a successful project…|
|`pmp-b1-226`|pmp-mocks-1-10|single|predictive|1|You have been asked to prepare a document to gain the project sponsor's sign off to access their function's resources, that…|

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
|`pmp-b2-1`|pmp-lectures-1-11|single|predictive|3|A project manager is assigned to a project in a country where the company has not previously done business. At the beginning of…|
|`pmp-b2-2`|pmp-lectures-1-11|single|predictive|4|A project manager is implementing a phase of an IT project in a location that is prone to security issues. A team member, working…|
|`pmp-b2-3`|pmp-lectures-1-11|single|predictive|3|A project is about to begin and a new regulation has imposed a deadline that will adversely impact the project. Failing to comply…|
|`pmp-b2-4`|pmp-lectures-1-11|single|predictive|3|An organization is halfway through a five year project and everything has progressed as scheduled. The latest project assessment…|
|`pmp-b2-5`|pmp-lectures-1-11|single|predictive|3|A compliance issue was found during the course of a project. This caused an inspection of the product by the internal audit team.…|
|`pmp-b2-6`|pmp-lectures-1-11|single|agile|4|At the start of a project, the team chose to leverage an agile approach to deliver the project, using user stories to capture…|
|`pmp-b2-7`|pmp-lectures-1-11|single|predictive|3|A team member raises a concern that some deliverables may not be compliant with certain regulations.|
|`pmp-b2-8`|pmp-lectures-1-11|single|predictive|3|During project execution, the audit team determined that critical regulatory requirements are not being met.|
|`pmp-b2-9`|pmp-lectures-1-11|single|predictive|4|A project team is installing a fiscal metering system for a crude oil terminal. The project manager has requested a metering…|
|`pmp-b2-10`|pmp-lectures-1-11|single|agile|4|A company must launch a global product to respond to a competition threat. As part of an agile approach, the team chooses Region…|
|`pmp-b2-11`|pmp-lectures-1-11|single|agile|4|A team is working in a highly regulated industry and the compliance department keeps pushing documentation and process steps that…|
|`pmp-b2-12`|pmp-lectures-1-11|single|predictive|3|A project manager is contracted to manage a project for a startup with grants from a foreign source. Midway into this project,…|
|`pmp-b2-13`|pmp-lectures-1-11|single|predictive|2|A project manager was recently assigned to an investigation project. Due to the complexity of the project, the project manager is…|
|`pmp-b2-14`|pmp-lectures-1-11|single|predictive|4|An IT contract for a project states that all team members must be certified in IT security. The project manager discovers that…|
|`pmp-b2-201`|pmp-mocks-1-10|single|hybrid|3|While planning a global solution delivery in a multinational organization, the project manager and team drafted all work items…|
|`pmp-b2-202`|pmp-mocks-1-10|single|hybrid|3|A project manager is assigned a complex project with significant legal, regulatory, and contractual requirements. However, these…|
|`pmp-b2-203`|pmp-mocks-1-10|single|predictive|2|A new project manager takes over a construction project. Upon reviewing the project management plan, the project manager realizes…|
|`pmp-b2-204`|pmp-mocks-1-10|single|predictive|2|A project manager for a building project has been informed that a required permit from the local government has not yet been…|
|`pmp-b2-205`|pmp-mocks-1-10|single|agile|3|A project manager is working in a regulated industry, and the team is working on a project where the requirements are prioritized…|
|`pmp-b2-206`|pmp-mocks-1-10|single|predictive|2|During a design workshop for a new robot, the engineers ask the project manager about safety and environmental protocols for its…|
|`pmp-b2-207`|pmp-mocks-1-10|single|predictive|2|While attending a governance meeting with upper management, a project manager is verbally informed about an impending external…|
|`pmp-b2-208`|pmp-mocks-1-10|single|predictive|2|During the execution phase of a project, a new director joined the management team and introduced a mandatory compliance…|
|`pmp-b2-209`|pmp-mocks-1-10|single|predictive|3|A company initiated a project to build a new hydropower plant. Analysis was performed to determine regulatory, safety, and…|
|`pmp-b2-210`|pmp-mocks-1-10|single|predictive|2|A gas pipeline construction project was planned for completion in 36 months. After 24 months, the project was suspended by the…|
|`pmp-b2-211`|pmp-mocks-1-10|single|predictive|3|While planning a new project, a new project manager finds that the request for proposal (RFP) references several government…|
|`pmp-b2-212`|pmp-mocks-1-10|single|agile|3|A new Scrum project manager at a company is contracted to build a traffic measurement and control application for a municipality.…|
|`pmp-b2-213`|pmp-mocks-1-10|single|hybrid|2|A project manager is working on a hybrid (predictive-agile) project. During a standup meeting, a team member mentions that…|
|`pmp-b2-214`|pmp-mocks-1-10|single|predictive|2|A majority portion of a local company was recently sold to an international company. The new parent company is mandating the use…|
|`pmp-b2-215`|pmp-mocks-1-10|multi|hybrid|4|A department store chain is in the implementation stage of a project to replace the payment systems across all of its stores in…|
|`pmp-b2-216`|pmp-mocks-1-10|single|agile|2|During the last iteration of planning in an agile project, the team discovers that mandatory regulations were not included in the…|
|`pmp-b2-217`|pmp-mocks-1-10|single|predictive|2|A project manager identifies that the work started by the project team is inconsistent with industry regulations.|
|`pmp-b2-218`|pmp-mocks-1-10|single|agile|3|During a product backlog refinement meeting, a stakeholder from the legal department and the product owner disagree on the…|
|`pmp-b2-219`|pmp-mocks-1-10|single|predictive|2|A software development project is in the testing phase. New government regulation requires that additional documentation be…|
|`pmp-b2-220`|pmp-mocks-1-10|single|predictive|2|What should a project manager do first when people are not following the project compliance process?|
|`pmp-b2-221`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a complex project with diverse stakeholders and a strict regulatory environment. The project team is…|
|`pmp-b2-222`|pmp-mocks-1-10|single|hybrid|3|A project manager has made compliance-related changes by adopting a new project delivery approach. During external validation, an…|
|`pmp-b2-223`|pmp-mocks-1-10|single|agile|3|A project manager considers compliance to be part of the organization's process assets. The project team, however, has decided to…|
|`pmp-b2-224`|pmp-mocks-1-10|single|predictive|3|An organization conducted an external audit in an Offshore Data Centre (ODC). The audit team noticed that the project manager is…|
|`pmp-b2-225`|pmp-mocks-1-10|single|predictive|3|A project manager has engaged the sponsor and discussed concerns regarding noncompliance. The project manager understands that…|
|`pmp-b2-226`|pmp-mocks-1-10|single|hybrid|3|A project manager is working on a new compliance project that is different from regular projects as it will involve multinational…|
|`pmp-b2-227`|pmp-mocks-1-10|single|predictive|2|A complex construction project entails the meeting of several regulations throughout project execution. The project team is…|
|`pmp-b2-228`|pmp-mocks-1-10|single|agile|3|An agile project team has implemented design changes over several iterations. The data privacy officer is concerned because their…|
|`pmp-b2-229`|pmp-mocks-1-10|single|predictive|2|A new project is initiated to develop several new financial products and services for a company with offices in several…|
|`pmp-b2-230`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a development project for an external customer in another country. The project manager's company is…|
|`pmp-b2-231`|pmp-mocks-1-10|single|agile|2|On an agile project, a team member raises concerns about health and safety issues related to the project deliverable.|
|`pmp-b2-232`|pmp-mocks-1-10|single|predictive|3|A project manager needs to ensure that all data security requirements are covered during a data warehouse development project’s…|
|`pmp-b2-233`|pmp-mocks-1-10|multi|agile|3|A project manager is leading an agile project developing a new customer relationship management (CRM) system. Midway through the…|
|`pmp-b2-234`|pmp-mocks-1-10|single|hybrid|3|In anticipation of new environmental regulations, a manufacturing project involving two distinct operational teams-one using…|
|`pmp-b2-235`|pmp-mocks-1-10|single|predictive|2|A project manager is working on a government infrastructure project that requires strict compliance with company standards.…|
|`pmp-b2-236`|pmp-mocks-1-10|single|predictive|3|In the rollout of a critical update to an e-commerce platform, the project manager is presented with several pressing issues:…|
|`pmp-b2-237`|pmp-mocks-1-10|single|predictive|2|During an environmental impact review of an energy project, the project manager discovers that the environmental plan does not…|
|`pmp-b2-238`|pmp-mocks-1-10|single|predictive|3|Launching a project to introduce a new health information system across multiple clinics, the project manager realizes there are…|
|`pmp-b2-239`|pmp-mocks-1-10|single|predictive|2|A project manager is tasked with launching a new drug product, which requires adherence to stringent health and safety…|
|`pmp-b2-240`|pmp-mocks-1-10|single|predictive|2|You are halfway through a large scale project for your organization when you are approached by the internal Audit department.…|
|`pmp-b2-241`|pmp-mocks-1-10|single|predictive|1|You are working as a project manager in a foreign country. It is customary that gifts be made to the government to obtain the…|

### business-3 · Change control — 115 items

*Route changes through a controlled process and keep records aligned.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b3-001`|core|single|predictive|2|On a warehouse fit-out, the change control board has approved a request from the client's operations director to add a second…|
|`pmp-b3-002`|core|single|agile|3|A finance director on the steering committee of an agile customer-portal rebuild asks why the project needs change control at…|
|`pmp-b3-003`|core|hotspot|predictive|2|The diagram shows your project's change control sequence. A developer has begun building a requested change as soon as the impact…|
|`pmp-b3-004`|core|single|agile|3|Your team works from a prioritised backlog under a contract that pays for a fixed number of sprints. The client's product owner…|
|`pmp-b3-005`|core|multi|hybrid|2|Your programme has a plan-driven hardware workstream under formal change control and a software team working from a backlog the…|
|`pmp-b3-006`|core|single|predictive|2|A failure in a live payroll interface will stop three thousand staff being paid on Friday unless a configuration change is made…|
|`pmp-b3-007`|core|single|hybrid|2|An internal audit of your programme finds three changes that were properly requested, assessed and approved by the change board…|
|`pmp-b3-021`|core|single|agile|3|Your fixed-price agile contract lets the client swap backlog items of equal estimated size. At sprint planning, the client's…|
|`pmp-b3-022`|core|single|agile|2|Mid-sprint, a marketing manager asks a developer directly to change how a promotion banner behaves. The developer agrees and…|
|`pmp-b3-023`|core|single|agile|4|Your product owner accepts a major change of direction that would make three months of completed work obsolete. The budget and…|
|`pmp-b3-024`|core|single|hybrid|3|An approved change altered your platform's external interface. The team implemented and released it, but the interface…|
|`pmp-b3-025`|core|multi|agile|3|A stakeholder asks your product team to add a new payment method to an online shop before the next release. Select two.|
|`pmp-b3-026`|core|single|hybrid|4|The change board has rejected a request to extend a hybrid programme's data warehouse. A week later, the sponsor's deputy asks…|
|`pmp-case1-003`|case-01|single|predictive|4|The civil contractor has started additional piling under filter bank 3 on your site supervisor's instruction, and has submitted a…|
|`pmp-b3-101`|business-3|single|predictive|4|You manage a billing replacement for a regional water utility. The change management plan lets you approve changes under £15,000;…|
|`pmp-b3-102`|business-3|single|agile|1|Your agile team builds a claims-handling app for an insurer, and a product owner orders its backlog. The sales director has…|
|`pmp-b3-103`|business-3|single|hybrid|4|A logistics programme combines a predictive warehouse-automation build with three agile software teams. The change management…|
|`pmp-b3-104`|business-3|single|predictive|3|On a €9 million school extension, the cladding subcontractor proposes replacing the specified façade fixings with another brand.…|
|`pmp-b3-105`|business-3|single|agile|3|Three sprints into a six-sprint release of a payments app, a near miss leads the team and product owner to add automated security…|
|`pmp-b3-106`|business-3|matching|predictive|2|Prompts: The client's engineer asks which revisio… / The change board wants to know, before i… / The sponsor asks how many change request…|
|`pmp-b3-107`|business-3|single|hybrid|3|Your hybrid project for a county council is implementing a cloud HR platform. The configuration design was signed off under a…|
|`pmp-b3-108`|business-3|multi|predictive|3|You are the supplier's project manager on a fixed-price contract to build a booking system for a ferry operator. In a design…|
|`pmp-b3-109`|business-3|single|agile|3|An online grocer's mobile team releases every two weeks, and a national launch campaign begins in six weeks. The developers ask…|
|`pmp-b3-110`|business-3|hotspot|predictive|2|A telecoms fibre-rollout project has revised its plan twice. CR-07, approved in March, re-issued the schedule and cost baselines.…|
|`pmp-b3-111`|business-3|fillblank|hybrid|2|On a hybrid airport baggage-system upgrade, product owners may approve changes within a release from a €120,000 change pool. Any…|
|`pmp-b3-112`|business-3|pulldown|agile|3|Prompts: Mid-sprint, the developers find that the… / On day three of a sprint, a new fares re… / Customers using a journey-planner screen…|
|`pmp-b3-113`|business-3|matching|hybrid|4|Prompts: Seat-frame welds from the new jig are al… / The entertainment team's burn-up shows i… / The seat supplier has renumbered the sea…|
|`pmp-b3-114`|business-3|multi|agile|3|Your Scrum team is rebuilding an online donations platform for a national charity. Over the last four sprints, the product owner…|
|`pmp-b3-115`|business-3|single|predictive|5|You are installing a packaging line for a pharmaceutical manufacturer. The change management plan lets you approve changes up to…|
|`pmp-b3-116`|business-3|single|agile|4|Your team at a video-streaming company releases fortnightly and freezes a tested release candidate 48 hours before each release.…|
|`pmp-b3-117`|business-3|single|hybrid|3|A housing association is replacing its repairs system in a hybrid project: a fixed go-live date for the core system, with later…|
|`pmp-b3-118`|business-3|single|predictive|3|Two weeks ago, the change board of a £22 million university laboratory build approved adding a cleanroom suite, based on an…|
|`pmp-b3-119`|business-3|single|predictive|1|You are refurbishing a borough council's leisure centre. Last week the change board rejected a request from the centre's…|
|`pmp-b3-120`|business-3|single|agile|3|Your Scrum team is building a mortgage-application app for a building society. Midway through a release, the developers conclude…|
|`pmp-b3-121`|business-3|single|hybrid|2|A hospital trust is replacing its pharmacy stock system in a hybrid project. The vendor's team builds approved changes as stories…|
|`pmp-b3-122`|business-3|single|predictive|3|You are refurbishing forty rooms for a hotel group. The change management plan names the sponsor as the approver for any change…|
|`pmp-b3-123`|business-3|multi|agile|3|Two Scrum teams build a travel-booking platform. The search team owns a pricing interface that the checkout team calls. Yesterday…|
|`pmp-b3-124`|business-3|hotspot|predictive|2|You manage the extension of a rail maintenance depot. Before CR-22 goes to Thursday's change board, you review its change request…|
|`pmp-b3-125`|business-3|single|hybrid|3|An energy company runs a hybrid programme: meter installation is plan-driven, while a customer app is built in sprints. The…|
|`pmp-b3-126`|business-3|multi|predictive|4|You are building a secondary school for a multi-academy trust. Two change requests reach you in the same week. The head of…|
|`pmp-b3-127`|business-3|single|agile|3|You support a Scrum team building a language-learning app, where the product owner is accountable for the product goal. This…|
|`pmp-b3-128`|business-3|fillblank|predictive|3|An offshore substation project has a cost baseline of €48.0 million, which includes €3.2 million of contingency reserve. A…|
|`pmp-b3-129`|business-3|pulldown|agile|2|Prompts: A merchandiser asks for a new gift-card… / Card payments have failed on the website… / While an item adding delivery-slot booki…|
|`pmp-b3-130`|business-3|single|hybrid|3|You lead a hybrid project moving a bank's card-servicing platform to a new data centre. The change board has just approved a…|
|`pmp-b3-131`|business-3|single|predictive|4|Your team has delivered a new contact-centre telephony system for a regional bus operator. Acceptance tests against the agreed…|
|`pmp-b3-132`|business-3|multi|agile|4|Your agile team builds a public-sector benefits portal and can deploy small, fully tested changes several times a day. IT…|
|`pmp-b3-133`|business-3|matching|hybrid|3|Prompts: A lift in the gatehouse would open the u… / Audio-guide downloads in the app have st… / A themed courtyard café would cost €140,…|
|`pmp-b3-134`|business-3|single|agile|3|Your Scrum team builds the companion app for an insulin pump, which is regulated as medical-device software. An auditor has asked…|
|`pmp-b3-135`|business-3|single|predictive|5|You are fourteen months into building a fire station for a fire and rescue service. No scope has changed, but poor groundworks…|
|`pmp-b3-1`|pmp-lectures-1-11|single|predictive|3|During a predictive project, the project manager is in the process of finalizing the exit criteria for a significant system…|
|`pmp-b3-2`|pmp-lectures-1-11|single|predictive|3|A major change in a government policy may cause a project's completion date to be delayed. The project is 90% complete.|
|`pmp-b3-3`|pmp-lectures-1-11|single|hybrid|4|A company that has traditionally used predictive approaches has been experimenting with the use of agile in portions of a…|
|`pmp-b3-4`|pmp-lectures-1-11|single|predictive|2|In the middle of executing an IT project, two project team members discover a new feature that will change the appearance of the…|
|`pmp-b3-5`|pmp-lectures-1-11|single|predictive|4|A project manager is working on a project that has been ongoing for some time and is currently at the 70% completion mark. The…|
|`pmp-b3-6`|pmp-lectures-1-11|single|predictive|3|Halfway through implementation, the project co-sponsor, who is contributing a significant amount of capital to the project,…|
|`pmp-b3-7`|pmp-lectures-1-11|single|predictive|2|A project manager is working on a project to modernize a bank's information technology (IT) infrastructure. The project manager…|
|`pmp-b3-8`|pmp-lectures-1-11|single|predictive|5|A project is facing a one-month delay, and the project manager realizes that a change request is necessary. The change control…|
|`pmp-b3-9`|pmp-lectures-1-11|single|predictive|3|A project manager is responsible for a 12-month waterfall project to create a new product. The project is nine months into…|
|`pmp-b3-10`|pmp-lectures-1-11|single|predictive|4|During execution, the customer submits a change request to maintain the project's business value, which has been threatened by a…|
|`pmp-b3-11`|pmp-lectures-1-11|single|predictive|4|A new solution is being deployed in several countries. Country A asked for a change in the solution in order to comply with its…|
|`pmp-b3-201`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a project that follows the company's predictive approach. The project manager discovers that the…|
|`pmp-b3-202`|pmp-mocks-1-10|single|predictive|3|While validating deliverables, a project manager realizes that one of the project team members used an older version of the scope…|
|`pmp-b3-203`|pmp-mocks-1-10|single|predictive|3|The sponsor for a multimillion-dollar project has just called the project manager for a crisis meeting. The client submitted a…|
|`pmp-b3-204`|pmp-mocks-1-10|single|predictive|2|During a product acceptance test for a software project, the project sponsor tells the project manager that the software will…|
|`pmp-b3-205`|pmp-mocks-1-10|single|predictive|3|A stakeholder has suggested using an off-the-shelf, open-source product to speed up development. The team has analyzed the…|
|`pmp-b3-206`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to redesign a store's point-of-sale system. The team is currently testing the new functionality…|
|`pmp-b3-207`|pmp-mocks-1-10|single|predictive|2|During project execution, a customer asks to replace a piece of planned hardware with one that has more features and a lower…|
|`pmp-b3-208`|pmp-mocks-1-10|single|predictive|3|A project manager is reviewing work performance data in preparation for the close project process. The data reveals that one of…|
|`pmp-b3-209`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a critical project to deliver a new solution. The CEO is highly invested in the project, and the CFO…|
|`pmp-b3-210`|pmp-mocks-1-10|multi|hybrid|4|A project manager is leading a project that received a request for additional scope caused by changes in market demand. The…|
|`pmp-b3-211`|pmp-mocks-1-10|single|predictive|2|A project team discovers an opportunity to enhance software without impacting the budget or schedule. The team makes the…|
|`pmp-b3-212`|pmp-mocks-1-10|single|predictive|2|A functional manager has been making changes to a project outside of the established management plan. The functional manager…|
|`pmp-b3-213`|pmp-mocks-1-10|single|predictive|3|The change control board (CCB) has evaluated and approved several change requests in a timely manner in the last several weeks.…|
|`pmp-b3-214`|pmp-mocks-1-10|single|predictive|2|In the middle of project execution, the project sponsor leaves, and a new sponsor is assigned. The new sponsor is requesting…|
|`pmp-b3-215`|pmp-mocks-1-10|single|predictive|4|A client requests a major scope change due to a change in the market environment, which will significantly add to the project…|
|`pmp-b3-216`|pmp-mocks-1-10|single|predictive|3|During the development of a project management plan, the project team inadvertently omitted an essential component required for a…|
|`pmp-b3-217`|pmp-mocks-1-10|single|predictive|2|While reviewing project documentation, a project manager discovers that modifications were made without proper approvals.|
|`pmp-b3-218`|pmp-mocks-1-10|single|hybrid|3|A project manager is managing a technology project that is in the implementation phase. A key stakeholder communicates the need…|
|`pmp-b3-219`|pmp-mocks-1-10|single|predictive|2|A key stakeholder requests new features to be added to a product during the project implementation phase. The stakeholder insists…|
|`pmp-b3-220`|pmp-mocks-1-10|single|predictive|2|While working on a project, the project team realizes that recent data center upgrades could impact the project's outcome.…|
|`pmp-b3-221`|pmp-mocks-1-10|single|agile|2|An agile team is working on a new product development project. The product owner has requested a new change to accommodate recent…|
|`pmp-b3-222`|pmp-mocks-1-10|single|predictive|3|A customer's requested design change is approved by the change control board (CCB). The project manager then discovers that the…|
|`pmp-b3-223`|pmp-mocks-1-10|single|predictive|2|A project team has identified a number of defects with some deliverables and intends to resolve these as soon as possible. The…|
|`pmp-b3-224`|pmp-mocks-1-10|single|predictive|2|During nondestructive testing at a construction site, it was discovered that certain components are failing workability tests.…|
|`pmp-b3-225`|pmp-mocks-1-10|single|predictive|2|During the planning phase for the expansion of a services company, stakeholders conducted a feasibility study to establish the…|
|`pmp-b3-226`|pmp-mocks-1-10|single|hybrid|3|A new project is about to start and is subject to strict time and budget constraints. When reviewing the lessons learned…|
|`pmp-b3-227`|pmp-mocks-1-10|single|predictive|2|During project execution, key stakeholders request a change to ensure that the final product will satisfy their requirements.|
|`pmp-b3-228`|pmp-mocks-1-10|single|predictive|2|While reviewing the approved project schedule, the project manager realizes that the milestones are unrealistic due to…|
|`pmp-b3-229`|pmp-mocks-1-10|single|predictive|2|During a status review meeting, it was determined that the product will not meet project scope requirements in a particular…|
|`pmp-b3-230`|pmp-mocks-1-10|single|predictive|2|A customer wants to add new features to the project deliverable.|
|`pmp-b3-231`|pmp-mocks-1-10|single|predictive|2|A key stakeholder requests a change that will solve a critical issue that might affect continuity of operations. However, the…|
|`pmp-b3-232`|pmp-mocks-1-10|single|predictive|2|A stakeholder learns that the equipment purchased for a project is of higher quality than originally agreed upon. The stakeholder…|
|`pmp-b3-233`|pmp-mocks-1-10|single|predictive|2|A project manager is leading a project to deliver a new product to the market. The project sponsor has asked for a broadened…|
|`pmp-b3-234`|pmp-mocks-1-10|single|predictive|2|The development phase of a new product has concluded. The validation test is about to start, but the test lead disagrees with the…|
|`pmp-b3-235`|pmp-mocks-1-10|single|predictive|2|After a new project is executed, an executive suggests incorporating a new addon to provide more function to the product.|
|`pmp-b3-236`|pmp-mocks-1-10|single|predictive|2|As a critical update to an e-commerce platform nears completion, the project manager receives a last-minute request from a…|
|`pmp-b3-237`|pmp-mocks-1-10|single|predictive|3|As part of an initiative to upgrade an educational institution's digital infrastructure, a department head proposes the…|
|`pmp-b3-238`|pmp-mocks-1-10|single|agile|2|While working on an innovative web platform, a project manager receives a request from a crucial stakeholder for the inclusion of…|
|`pmp-b3-239`|pmp-mocks-1-10|single|predictive|2|During a significant landscape redesign project, the project manager finds a cutting-edge irrigation system that could…|
|`pmp-b3-240`|pmp-mocks-1-10|single|predictive|2|As the project manager for an innovative smart home device project, you receive a request from the stakeholder to incorporate…|
|`pmp-b3-241`|pmp-mocks-1-10|single|predictive|2|The project manager for a renewable energy initiative receives a suggestion from an environmental consultant to incorporate a new…|
|`pmp-b3-242`|pmp-mocks-1-10|single|agile|1|While implementing an enterprise resource planning (ERP) system, the agile project manager receives several requests for…|
|`pmp-b3-243`|pmp-mocks-1-10|single|predictive|2|At the conclusion of an enterprise software implementation project, a department head suggests the addition of a new analytics…|
|`pmp-b3-244`|pmp-mocks-1-10|single|agile|2|You are half way through your agile project when there is a significant change to the project scope, after it was found that the…|
|`pmp-b3-245`|pmp-mocks-1-10|single|hybrid|3|You are working on a hybrid project, where the majority of the work has been planned up front. The team manages their work via a…|
|`pmp-b3-246`|pmp-mocks-1-10|single|predictive|1|One of the stakeholders in your project has raised a change request, after some of the deliverables contained defects and a…|
|`pmp-b3-247`|pmp-mocks-1-10|single|agile|1|You are a project manager in an agile team developing new features for an app. An executive stakeholder with a high influence in…|
|`pmp-b3-248`|pmp-mocks-1-10|single|predictive|2|During User Acceptance Testing a senior user points out a major change needed to the project scope for it to be successful. You…|
|`pmp-b3-249`|pmp-mocks-1-10|single|predictive|1|One of the senior users testing your product found is was missing a key requirement that he had assumed would be there. It was…|
|`pmp-b3-250`|pmp-mocks-1-10|single|predictive|2|A significant change has been made to the project scope after finding a serious system flaw. This scope change will impact the…|
|`pmp-b3-251`|pmp-mocks-1-10|single|predictive|1|Five major defects have been found during quality testing, resulting in the scope changing significantly and affecting your…|
|`pmp-b3-252`|pmp-mocks-1-10|single|predictive|3|You have just taken over as project manager on a difficult waterfall project. Multiple project stakeholders have approached you…|
|`pmp-b3-253`|pmp-mocks-1-10|single|predictive|2|One of your stakeholders with a very high influence within the company has raised an urgent change that they want to be…|
|`pmp-b3-254`|pmp-mocks-1-10|single|predictive|2|Your deliverables have recently been quality checked by a specialist testing team, and passed with flying colours. You take them…|
|`pmp-b3-255`|pmp-mocks-1-10|single|agile|2|You are working on a new project in your organization, where the team is trying to move to an Agile way of work. The team are not…|

### business-4 · Impediments and issues — 89 items

*Spot blockers, prioritise them, and clear them before the team stalls.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b4-001`|core|hotspot|hybrid|3|The board below shows how many items are sitting in each stage of the team's workflow this morning. Select the stage where work…|
|`pmp-b4-002`|core|single|agile|2|Your team maintains an impediment log. Reviewing it, you find eleven open items, of which nine have been open for more than three…|
|`pmp-b4-003`|core|multi|agile|3|Your team's board shows six items in progress and four of them blocked, three waiting on the same external security review that…|
|`pmp-b4-004`|core|single|hybrid|3|A dependency owner in another division has missed two agreed dates for an interface your programme needs in five weeks. She has…|
|`pmp-b4-005`|core|hotspot|agile|2|The diagram shows the levels your organisation uses for clearing blockers. Your team cannot test because the shared environment…|
|`pmp-b4-006`|core|single|agile|2|Your team's impediment board carries seven items. Two are stopping work today. The other five read as "the vendor may not deliver…|
|`pmp-b4-007`|core|hotspot|agile|2|The diagram shows the average time a work item spends at each stage of your team's delivery flow, measured over the last two…|
|`pmp-b4-021`|core|single|agile|2|At the daily scrum, a developer mentions in passing that she has been stuck for two days waiting for a firewall rule. She did not…|
|`pmp-b4-022`|core|single|agile|3|Your team's impediment board holds nine items, and every one is assigned to you as scrum master. Several are things the team…|
|`pmp-b4-023`|core|single|agile|4|Every Friday afternoon your team's deployments fail, because operations runs an infrastructure freeze that nobody told the team…|
|`pmp-b4-024`|core|single|hybrid|3|On a hybrid programme, an agile software team is waiting for a hardware prototype from the predictive workstream, which has…|
|`pmp-b4-025`|core|multi|agile|3|Developers mention four things before the daily scrum. Select two.|
|`pmp-b4-026`|core|single|hybrid|4|A dispute with a vendor has blocked integration on your hybrid programme for six weeks. The issue is owned by the vendor manager,…|
|`pmp-case5-008`|case-05|single|agile|3|For two sprints the agile team has been unable to test telephony features because the telephony supplier has not given it access…|
|`pmp-b4-101`|business-4|single|predictive|1|You are managing construction of a new outpatient wing for a hospital trust. The risk register lists the insolvency of the…|
|`pmp-b4-102`|business-4|single|agile|2|At the daily stand-up of a team building a mobile banking app, a developer says her payment-sandbox credentials expired overnight…|
|`pmp-b4-103`|business-4|single|hybrid|3|Your hybrid project is replacing an insurer's claims system. On Monday the licence for the automated regression-testing tool…|
|`pmp-b4-104`|business-4|multi|predictive|3|Your company is installing a new filling line at a customer's bottling plant under a fixed-price contract that obliges the…|
|`pmp-b4-105`|business-4|matching|predictive|2|Prompts: Two members of the project team have eac… / Finance and Operations each insist the n… / A sister project in the same programme h…|
|`pmp-b4-106`|business-4|single|agile|3|A team building an online grocery checkout keeps losing time to its own automated test suite, which fails at random about one run…|
|`pmp-b4-107`|business-4|single|hybrid|4|You are leading a hybrid project to move a pension administrator onto a new platform. Three issues have been open for a week,…|
|`pmp-b4-108`|business-4|single|predictive|5|Your project is moving a building society's core systems to a new data centre. A high-severity issue, nightly backups failing,…|
|`pmp-b4-109`|business-4|hotspot|predictive|4|You manage construction of a secondary school's sports hall. The diagram lists four open issues. Each label gives the activity…|
|`pmp-b4-110`|business-4|fillblank|hybrid|3|Midway through an iteration, a hybrid logistics software project loses its shared integration environment. Four developers are…|
|`pmp-b4-111`|business-4|single|agile|3|Three stories in a travel-booking team's current sprint are waiting on decisions about how refunds should work for partly used…|
|`pmp-b4-112`|business-4|pulldown|predictive|2|Prompts: The steel erector's only crane broke dow… / Ground surveys suggest unrecorded utilit… / The client has asked for a second loadin…|
|`pmp-b4-113`|business-4|matching|hybrid|4|Prompts: The build pipeline fails about one run i… / A security sign-off that only the inform… / The shared design-system library has a l…|
|`pmp-b4-114`|business-4|multi|agile|3|A games studio team's velocity has looked steady for six sprints, and management considers the team healthy. In a one-to-one, a…|
|`pmp-b4-115`|business-4|single|predictive|3|On a multi-storey car park build, three of the last five scheduled concrete pours were cancelled on the morning. Your site…|
|`pmp-b4-116`|business-4|single|hybrid|3|Your hybrid project is building a customer portal for a water utility, with development done by a team in another country. The…|
|`pmp-b4-117`|business-4|single|agile|4|On an energy retailer's billing team, one developer is the only person who understands the legacy tariff engine. Production…|
|`pmp-b4-118`|business-4|single|agile|3|Your team works in a streaming company's scaled agile setup. This morning another team released a change to its recommendations…|
|`pmp-b4-119`|business-4|single|predictive|1|You are managing the refurbishment of a city museum's galleries. At the weekly issue review you notice that an open issue, a…|
|`pmp-b4-120`|business-4|single|agile|1|On a team building a charity's online donations platform, a developer says at stand-up that the head of fundraising has come to…|
|`pmp-b4-121`|business-4|single|predictive|2|You are upgrading the passenger walkways at a ferry terminal. Piling has struck an uncharted concrete obstruction, and removing…|
|`pmp-b4-122`|business-4|pulldown|agile|2|Prompts: At two in the afternoon a tester finds t… / Over several sprints the team has quietl… / Changes to the shared address-lookup ser…|
|`pmp-b4-123`|business-4|single|predictive|4|Your company is building a water treatment works for a regional utility to drawings the utility has approved. The…|
|`pmp-b4-124`|business-4|multi|predictive|3|You are installing an automated dispensing system in a hospital pharmacy. The project management plan lets you resolve issues…|
|`pmp-b4-125`|business-4|single|predictive|3|Your firm is refurbishing a hotel's bedrooms floor by floor while the hotel stays open. Each floor needs a permit to work from…|
|`pmp-b4-126`|business-4|single|predictive|3|You are managing an upgrade to a regional airport's baggage handling system. A software fault is misrouting one bag in fifty…|
|`pmp-b4-127`|business-4|matching|predictive|3|Prompts: The case sealer stops on every night shi… / The commissioning log holds 140 faults a… / Operators believe conveyor jams rise on…|
|`pmp-b4-128`|business-4|hotspot|agile|3|Your team builds a recruitment platform. The stand-up notes for the last sprint record no blockers raised on any day. The diagram…|
|`pmp-b4-129`|business-4|fillblank|agile|3|A team at a telecoms provider finished 40 work items last quarter. Summed across those items, they spent 600 item-days between…|
|`pmp-b4-130`|business-4|multi|agile|3|A team building a public library's e-lending app has a sprint goal that depends on one story, loan renewals. That story has been…|
|`pmp-b4-131`|business-4|single|agile|4|You facilitate a team building an ordering app for a farmers' cooperative. Three months ago you asked each developer to fill in a…|
|`pmp-b4-132`|business-4|single|hybrid|5|Your hybrid project is building a patient booking portal for a private healthcare provider. The synthetic test data generator…|
|`pmp-b4-133`|business-4|single|hybrid|3|Your hybrid project is building a dealer portal for a car manufacturer. The team cannot test the parts-ordering feature until the…|
|`pmp-b4-134`|business-4|multi|hybrid|3|A hybrid team is replacing a housing association's repairs system. Four months ago, when an interface from the contractor…|
|`pmp-b4-135`|business-4|single|hybrid|4|Your hybrid project is building a research data platform for a university. Finance requires three quotes and a five-working-day…|
|`pmp-b4-1`|pmp-lectures-1-11|single|predictive|4|A project manager was recently assigned to an information technology project. The project is in the initiation stage, and the…|
|`pmp-b4-2`|pmp-lectures-1-11|single|predictive|3|A project manager is managing an upgrade from an older system to a new version. Halfway through the upgrade, the older system…|
|`pmp-b4-3`|pmp-lectures-1-11|single|predictive|3|A project manager has been informed that the first of several major deliveries from another country will be delayed. These…|
|`pmp-b4-4`|pmp-lectures-1-11|single|agile|3|During a retrospective meeting, project team members complain that open issues that were not fixed during the iteration are…|
|`pmp-b4-5`|pmp-lectures-1-11|single|agile|3|A project team realizes that the product's architecture will not support the growing number of users.|
|`pmp-b4-6`|pmp-lectures-1-11|single|agile|3|An agile project manager is overseeing a Kanban project and wants to assess task completion roadblocks for an upcoming major…|
|`pmp-b4-7`|pmp-lectures-1-11|single|predictive|3|Project team members inform their project manager of a challenge they are facing. This challenge will likely impact project…|
|`pmp-b4-8`|pmp-lectures-1-11|single|predictive|3|During the testing phase of a project, a team member finds that some requirements were not met. If the project now adds work to…|
|`pmp-b4-9`|pmp-lectures-1-11|single|agile|3|One of the project vendors is late in providing a related product solution, which is affecting the release due dates.|
|`pmp-b4-201`|pmp-mocks-1-10|single|agile|2|While executing a critical iterative phase of a project, a problem previously identified as a risk just became a confirmed issue,…|
|`pmp-b4-202`|pmp-mocks-1-10|single|predictive|3|A technology project for a multinational bank is in the testing stage. The project manager learns that a security breach has…|
|`pmp-b4-203`|pmp-mocks-1-10|single|predictive|1|A project manager notices that a project is not running well and deliverables are falling behind.|
|`pmp-b4-204`|pmp-mocks-1-10|single|agile|2|A project manager is managing an agile project which has many risks identified in the risk register. The current sprint deals…|
|`pmp-b4-205`|pmp-mocks-1-10|single|agile|2|An organization decides to implement the Scrum delivery framework. The project manager is using an impediment board with the…|
|`pmp-b4-206`|pmp-mocks-1-10|single|predictive|3|A project manager is managing the build of an off-shore data center. In addition, the project manager is the custodian of the…|
|`pmp-b4-207`|pmp-mocks-1-10|single|predictive|2|Midway through a project, a project manager learns that the project management planning application is failing to record updates…|
|`pmp-b4-208`|pmp-mocks-1-10|single|agile|2|During a daily standup, a stakeholder unexpectedly joins the meeting and criticizes the performance of one of the team members.…|
|`pmp-b4-209`|pmp-mocks-1-10|single|agile|3|A project manager is part of a project that has a scrum team. The team members are unfamiliar with the distinction between risks…|
|`pmp-b4-210`|pmp-mocks-1-10|single|predictive|2|During project initiation, the project manager learns that a key stakeholder will leave the project for a month to consult on…|
|`pmp-b4-211`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a complex system improvement project. During the integration phase, an upgrade to one component…|
|`pmp-b4-212`|pmp-mocks-1-10|single|predictive|3|A looming fuel price increase was identified as a risk during the planning stages of a large construction project. The identified…|
|`pmp-b4-213`|pmp-mocks-1-10|single|predictive|2|A manufacturing project is in the implementation phase. During a status meeting, a team member tells the project manager that a…|
|`pmp-b4-214`|pmp-mocks-1-10|single|agile|3|A product owner presented the backlog to the team, and the team raised concerns over implementing some of the features due to…|
|`pmp-b4-215`|pmp-mocks-1-10|multi|agile|3|A customer is upset because a critical issue for a project deliverable has not been resolved by the agile team, even though…|
|`pmp-b4-216`|pmp-mocks-1-10|single|agile|1|At daily standup meetings, several team members complain that they cannot concentrate due to a noisy work environment.|
|`pmp-b4-217`|pmp-mocks-1-10|multi|predictive|2|During a project review meeting, an end user reports that the personal computers supplied for testing are experiencing…|
|`pmp-b4-218`|pmp-mocks-1-10|single|predictive|1|A project team in a digital marketing project discovers that their key design software license has expired, halting the team work…|
|`pmp-b4-219`|pmp-mocks-1-10|single|predictive|2|During the execution phase of a software development project, the team encounters a technical challenge that jeopardizes the…|
|`pmp-b4-220`|pmp-mocks-1-10|single|predictive|2|During a large-scale event planning project, the project manager notices a growing list of logistical challenges reported by…|
|`pmp-b4-221`|pmp-mocks-1-10|single|hybrid|3|A project manager notices that the integration process is more cumbersome than anticipated, significantly slowing down the…|
|`pmp-b4-222`|pmp-mocks-1-10|single|agile|2|A team member has been reporting no progress on a crucial task for several days during the daily stand-up meetings, causing a…|
|`pmp-b4-223`|pmp-mocks-1-10|single|predictive|2|During a critical software implementation project, the project manager identifies a significant slowdown in the integration…|
|`pmp-b4-224`|pmp-mocks-1-10|single|agile|2|You are working on an agile project as a scrum master, where the functional manager has worked with some people in your…|
|`pmp-b4-225`|pmp-mocks-1-10|single|hybrid|1|You are working on a hybrid project to replace an aging customer system that has just had a feature upgrade release. The senior…|
|`pmp-b4-226`|pmp-mocks-1-10|single|agile|1|You are working through the iteration with your Agile team, and are having trouble completing one of the story cards in the…|
|`pmp-b4-227`|pmp-mocks-1-10|single|predictive|2|Your project team have been complaining about poor stakeholder engagement in a certain part of the organisation for some time. It…|
|`pmp-b4-228`|pmp-mocks-1-10|single|predictive|2|You are working on a very high risk infrastructure project that is critical to your organisation. The team are experienced in…|
|`pmp-b4-229`|pmp-mocks-1-10|single|agile|1|You are running the daily stand-up as an Agile project lead and neutral facilitator for the team. Work seems to be being…|
|`pmp-b4-230`|pmp-mocks-1-10|single|predictive|1|You are working as a Project Manager and one of the stakeholders of the project raises a problem with the product requirements,…|
|`pmp-b4-231`|pmp-mocks-1-10|single|agile|2|Your project team are gathering solution ideas for a particular customer requirement, which is prioritised to be worked on next.…|

### business-5 · Risk — 144 items

*Identify, analyse, respond to and monitor risk across the project.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b5-001`|core|fillblank|predictive|3|A project carries two quantified risks: a threat with a 30 percent probability and a 250,000 impact, and an opportunity with a 40…|
|`pmp-b5-002`|core|multi|agile|3|An agile programme building a benefits-claims portal releases to production every two weeks, depends on two external suppliers…|
|`pmp-b5-003`|core|single|predictive|4|You have transferred a threat of data loss to a specialist backup provider under contract. In review, a team member points out…|
|`pmp-b5-004`|core|fillblank|predictive|4|You are sizing contingency for three independent uncertainties on a bridge refurbishment. A ground condition risk has a 30 per…|
|`pmp-b5-005`|core|multi|predictive|4|Reviewing your risk register after fourteen months, you find that eleven of the nineteen open risks have carried the same…|
|`pmp-b5-006`|core|matching|predictive|4|Prompts: A remote chance of catastrophic third-pa… / A significant threat that exists only be… / A threat with low probability and an imp…|
|`pmp-b5-007`|core|multi|predictive|4|A supplier building a subsystem for your plant upgrade tells you a different client has cancelled, freeing its production line.…|
|`pmp-b5-021`|core|single|agile|3|Your team plans to build a new feature on a framework nobody has used at scale. If its performance is poor, much of the next…|
|`pmp-b5-022`|core|single|agile|2|Your team discusses risks at each retrospective and lists them on the board. Several have stayed on the list for weeks with…|
|`pmp-b5-023`|core|single|agile|4|An open-source library released this month could replace a component your team planned to spend four sprints building. It is new…|
|`pmp-b5-024`|core|single|hybrid|3|On a hybrid programme, the predictive construction workstream keeps a formal risk register, while the agile teams track risks on…|
|`pmp-b5-025`|core|multi|agile|3|A key engineer, the only person who understands your payments service, may leave in the next few months. Select two.|
|`pmp-b5-026`|core|single|hybrid|4|Your hybrid programme is installing lifts in an occupied hospital. The sponsor's stated risk appetite is very low for patient…|
|`pmp-b5-061`|core|graphic|predictive|3|The diagram shows the six open threats on your warehouse automation project, each with its probability and cost impact. The…|
|`pmp-case3-004`|case-03|single|hybrid|4|Exhibit 3 shows Site B at 40 per cent of validation tests passed, 35 per cent of staff trained and 60 per cent of interfaces…|
|`pmp-b5-101`|business-5|single|predictive|1|During planning for a rural mobile-mast rollout, a site acquisition surveyor mentions that two landowners have talked about…|
|`pmp-b5-102`|business-5|single|agile|2|A developer on your agile team building a mobile banking app learns that the third-party payments interface the app depends on…|
|`pmp-b5-103`|business-5|single|hybrid|3|Your data-centre build has a fit-out baselined in a fixed plan and monitoring software delivered in sprints. The risk register…|
|`pmp-b5-104`|business-5|single|predictive|3|Your weekend rail possession plan includes a contingency response: if the new crossover is not fully laid by 02:00 on Sunday, the…|
|`pmp-b5-105`|business-5|single|agile|4|Your agile team is building a delivery-route optimisation service for a parcel carrier. A spike shows the open-source solver you…|
|`pmp-b5-106`|business-5|single|predictive|4|A national museum is relocating 40,000 objects to a new storage centre. The risk register holds 38 open risks; none is rated red,…|
|`pmp-b5-107`|business-5|single|hybrid|3|On a bank's hybrid migration of customer records to a cloud platform, the named owner of the risk 'privileged access not revoked…|
|`pmp-b5-108`|business-5|single|agile|3|Your agile team is three weeks from submitting a console game for platform certification. The platform holder has just announced…|
|`pmp-b5-109`|business-5|single|predictive|2|Your laboratory relocation's risk register includes 'Specialist freezer contractor may arrive late, delaying sample transfer',…|
|`pmp-b5-110`|business-5|single|predictive|5|A water utility's new 4 km tunnel must be in service before a regulatory permit deadline, with no float left. Ground…|
|`pmp-b5-111`|business-5|single|agile|4|Your agile team at an insurer keeps a risk board scored by probability times impact, and reserves about 15% of each sprint for…|
|`pmp-b5-112`|business-5|multi|predictive|3|You are facilitating a risk assessment workshop for an aircraft landing-gear redesign. On each risk, the chief engineer gives his…|
|`pmp-b5-113`|business-5|multi|agile|3|Your agile team is building an online donations platform for a children's charity, releasing every two weeks. At release planning…|
|`pmp-b5-114`|business-5|matching|agile|2|Prompts: Nobody knows whether the chosen database… / Caseworkers may reject the new workflow… / Integration with the council's mainframe…|
|`pmp-b5-115`|business-5|matching|hybrid|3|Prompts: A register risk, conveyor parts held at… / A sinkhole opens under the loading yard;… / The operations director asks for a retur…|
|`pmp-b5-116`|business-5|pulldown|hybrid|3|Prompts: The risk that winter storms would delay… / The risk of the ticketing app's payment… / The possibility that the floating crane…|
|`pmp-b5-117`|business-5|fillblank|predictive|3|A Monte Carlo simulation of your hospital imaging-suite refurbishment ran 5,000 iterations. Finish-week counts were: week 30,…|
|`pmp-b5-118`|business-5|hotspot|hybrid|4|Your electric-vehicle charger project builds hardware through stage gates and firmware in sprints. The heat map shows the five…|
|`pmp-b5-119`|business-5|single|predictive|1|You are managing a €6 million secondary-school extension. The change control board has just approved a change request adding a…|
|`pmp-b5-120`|business-5|single|predictive|2|On a stadium roof replacement, the register entry 'Steel fabricator delivers the roof trusses late, delaying the crane programme'…|
|`pmp-b5-121`|business-5|single|predictive|3|Your airport baggage-hall refit is 35% complete and has used £420,000 of its £700,000 contingency reserve, all on identified…|
|`pmp-b5-122`|business-5|single|predictive|3|Your library building's approved design specifies a new green-roof membrane that is still awaiting national fire certification,…|
|`pmp-b5-123`|business-5|single|predictive|4|On a food-processing line installation, the risk register holds 46 entries, every one raised by you or the two engineering leads.…|
|`pmp-b5-124`|business-5|single|predictive|5|Your schedule risk analysis for an offshore wind cable-laying campaign gives an 82% probability of finishing by the grid…|
|`pmp-b5-125`|business-5|single|agile|2|At release planning for a music festival's new ticketing app, you ask the agile team what could stop a smooth launch in eight…|
|`pmp-b5-126`|business-5|single|agile|3|Your agile team is building a repeat-prescription feature for a chain of community pharmacies. Its risk board lists fourteen…|
|`pmp-b5-127`|business-5|single|agile|4|Your agile team is two days from releasing a university's new student-records portal. A penetration test has found a…|
|`pmp-b5-128`|business-5|single|hybrid|4|Your retail chain's hybrid programme refits 30 stores to a fixed schedule while a loyalty app is built in sprints. Six risks were…|
|`pmp-b5-129`|business-5|multi|agile|3|Your agile team is building a crew-rostering module for an airline. It depends on a pay-rules service that another team has…|
|`pmp-b5-130`|business-5|multi|agile|3|Your agile team runs a grocery delivery platform. Quick fixes over the past year have left the order service fragile: two outages…|
|`pmp-b5-131`|business-5|multi|hybrid|3|Your hybrid project is building a hospital wing to a fixed plan while an agile team delivers its patient-flow software. The…|
|`pmp-b5-132`|business-5|matching|hybrid|2|Prompts: A permit amendment allows panels to go i… / A neighbouring developer is building a s… / A sprint review suggests that adding bat…|
|`pmp-b5-133`|business-5|pulldown|hybrid|3|Prompts: Hull welding output varies from week to… / Nobody can yet say how the scientists wi… / Earlier first-of-a-kind vessels met prob…|
|`pmp-b5-134`|business-5|fillblank|agile|3|Your agile team building a payroll app tracks risk exposure as probability × impact in days, summed across open risks, and plots…|
|`pmp-b5-135`|business-5|hotspot|predictive|3|Your refinery turnaround's risk analysis produced this sensitivity chart, with five inputs shown in register order. Each bar…|
|`pmp-b5-1`|pmp-lectures-1-11|single|predictive|3|A project manager is working on a deployment project to replace some equipment that will soon become obsolete. A review of the…|
|`pmp-b5-2`|pmp-lectures-1-11|single|predictive|2|During a project team meeting, team members have identified a list of risks that may occur during the project. The project…|
|`pmp-b5-3`|pmp-lectures-1-11|single|predictive|4|A new project has a team divided between onsite and offshore locations. The project manager observed a rising conflict between…|
|`pmp-b5-4`|pmp-lectures-1-11|single|predictive|3|A project manager has just received news that a new regulation has been adopted and the expectation is that the project will be…|
|`pmp-b5-5`|pmp-lectures-1-11|single|predictive|3|The project manager of a software development project for a financial institution realizes that team members are unusually…|
|`pmp-b5-6`|pmp-lectures-1-11|single|agile|2|While reviewing progress in the middle of an iteration, the scrum master points out a major risk due to a user story dependency.|
|`pmp-b5-7`|pmp-lectures-1-11|single|predictive|3|Some of the project's materials have been ordered from overseas. The supplier notifies the project team that the materials will…|
|`pmp-b5-8`|pmp-lectures-1-11|single|hybrid|3|A company is about to start a project in a remote place known for extreme weather. It was agreed to implement the project using a…|
|`pmp-b5-9`|pmp-lectures-1-11|single|predictive|4|A supplier of a critical component for a construction project notifies the project manager that the component will be…|
|`pmp-b5-10`|pmp-lectures-1-11|single|predictive|2|A project manager was just given a new project to handle. She is working on the project documents and, after reading the Project…|
|`pmp-b5-11`|pmp-lectures-1-11|single|predictive|3|A project manager is leading a project that is vital to their organization and the local environmental and energy authority.…|
|`pmp-b5-12`|pmp-lectures-1-11|single|predictive|3|A complex project is in the planning phase. The sponsor assigns a newly certified project manager to the project. Upon review of…|
|`pmp-b5-13`|pmp-lectures-1-11|single|predictive|3|During a high-risk project, several risks have been materialized and responses have been implemented, However, the project still…|
|`pmp-b5-201`|pmp-mocks-1-10|single|agile|3|A risk was identified at the beginning of an agile project and a mitigation plan was established. The risk event occurs but the…|
|`pmp-b5-202`|pmp-mocks-1-10|single|predictive|2|During the execution phase of a project, a project team identifies a previously unforeseen risk that could delay the project for…|
|`pmp-b5-203`|pmp-mocks-1-10|single|predictive|2|The opening of a new overseas office faces potential delays as the required permit has not yet been issued by the local…|
|`pmp-b5-204`|pmp-mocks-1-10|single|predictive|3|At the end of the first phase of a stadium expansion project, the project manager realizes they will be running behind schedule…|
|`pmp-b5-205`|pmp-mocks-1-10|single|hybrid|3|During a meeting, a team member on a construction project presents a proposal to the project manager. The proposal states that…|
|`pmp-b5-206`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a hybrid project. The organization uses an agile approach and a risk register, but the development…|
|`pmp-b5-207`|pmp-mocks-1-10|single|predictive|3|In the planning stage of an upgrade project, a project manager benchmarked other recent projects that use the same technology.…|
|`pmp-b5-208`|pmp-mocks-1-10|single|predictive|1|A project manager calls a meeting to identify risks for a complicated project. Several subject matter experts are invited to this…|
|`pmp-b5-209`|pmp-mocks-1-10|single|predictive|3|A company has undertaken a large transformation project to prepare itself for the changing economy. During the execution of the…|
|`pmp-b5-210`|pmp-mocks-1-10|single|predictive|2|A project manager learns that in-house resources are insufficient to produce a critical component of a deliverable. This poses a…|
|`pmp-b5-211`|pmp-mocks-1-10|single|predictive|2|A project manager has been made aware of a recent weather event that could potentially impact the project. Forecasters are unable…|
|`pmp-b5-212`|pmp-mocks-1-10|single|predictive|3|A project manager completed plans for a project and submitted all documents for approval but did not allocate time for delays in…|
|`pmp-b5-213`|pmp-mocks-1-10|single|predictive|2|A new customer relations database is being deployed. The schedule calls for migration from the previous system to occur over one…|
|`pmp-b5-214`|pmp-mocks-1-10|single|predictive|2|During project execution, an issue arises that requires additional budget and time to resolve the issue. The project team did not…|
|`pmp-b5-215`|pmp-mocks-1-10|single|predictive|3|During the design process for a multinational construction project, the project manager conducted a cost-benefit analysis based…|
|`pmp-b5-216`|pmp-mocks-1-10|single|predictive|2|During project implementation, an issue arises that requires the execution of the project's contingency plan, which will impact…|
|`pmp-b5-217`|pmp-mocks-1-10|single|agile|2|In an agile development environment, when should a risk analysis be performed?|
|`pmp-b5-218`|pmp-mocks-1-10|single|predictive|2|A project manager decided to involve a third party to manage project risk given that they had more experience than the current…|
|`pmp-b5-219`|pmp-mocks-1-10|multi|predictive|1|A new team member keeps mixing up the risk register and the issue log. (Choose two)|
|`pmp-b5-220`|pmp-mocks-1-10|single|agile|3|The team and product owner are reviewing user stories in the iteration planning meeting. While reviewing stories, it is…|
|`pmp-b5-221`|pmp-mocks-1-10|single|predictive|2|During an important project, one of the contractors complains that the approval process is difficult to handle and may delay the…|
|`pmp-b5-222`|pmp-mocks-1-10|single|predictive|2|A vendor informs a project manager that the project's equipment can be delivered earlier than anticipated. The project manager…|
|`pmp-b5-223`|pmp-mocks-1-10|single|predictive|3|A supplier for a regulatory project is responsible for making the component changes. The supplier is unlikely to meet the project…|
|`pmp-b5-224`|pmp-mocks-1-10|single|predictive|3|A company, which is conducting a critical bidding process, included a penalty clause in an agreement specifying that the selected…|
|`pmp-b5-225`|pmp-mocks-1-10|single|predictive|2|A project manager identifies a risk during the implementation phase of a project which was not anticipated during planning.|
|`pmp-b5-226`|pmp-mocks-1-10|single|predictive|4|After completing a project, the team meets to conduct a lessons learned session. During this meeting, the team sees that all…|
|`pmp-b5-227`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to a project that has completed the planning phase. The project sponsor is confident that the…|
|`pmp-b5-228`|pmp-mocks-1-10|single|predictive|2|A project manager and the project team identified risks, performed a qualitative risk analysis, and prepared a risk response…|
|`pmp-b5-229`|pmp-mocks-1-10|single|predictive|3|A local government provides subsidies for healthcare activities. An organization plans to leverage this subsidy for a project to…|
|`pmp-b5-230`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to a new environmental project. When reviewing a vendor's proposal, the project manager identifies…|
|`pmp-b5-231`|pmp-mocks-1-10|single|predictive|3|After comparing actual results to the performance measurement baseline, the project manager discovers that the project's work and…|
|`pmp-b5-232`|pmp-mocks-1-10|single|agile|2|During iteration planning, a project manager discusses user stories with the team. While prioritizing the user stories, some with…|
|`pmp-b5-233`|pmp-mocks-1-10|single|predictive|2|A project manager identifies new risks that might affect the completion of critical tasks. The project manager needs to present…|
|`pmp-b5-234`|pmp-mocks-1-10|single|predictive|2|A project manager recognizes that certain components must be imported to meet the project's requirements. However, due to the…|
|`pmp-b5-235`|pmp-mocks-1-10|single|predictive|2|A project manager is struggling to keep a technical system implementation on track after the loss of a key team member two months…|
|`pmp-b5-236`|pmp-mocks-1-10|single|predictive|2|A project team implements a change that was approved by the change control board (CCB). The change was intended to minimize a…|
|`pmp-b5-237`|pmp-mocks-1-10|single|hybrid|3|A project manager is planning for a project team that will soon start work on a critical subsystem. There are two technical…|
|`pmp-b5-238`|pmp-mocks-1-10|single|predictive|2|On an agricultural development project, a key team member expresses dissatisfaction with the current risk management strategy,…|
|`pmp-b5-239`|pmp-mocks-1-10|single|predictive|3|In the planning phase of a large-scale software implementation project, the project manager decides to conduct an extensive…|
|`pmp-b5-240`|pmp-mocks-1-10|single|predictive|3|A project manager is tasked with organizing risks for a new infrastructure project. To ensure comprehensive risk management, the…|
|`pmp-b5-241`|pmp-mocks-1-10|single|predictive|3|A construction project is experiencing significant delays due to many unforeseen weather conditions, causing concern about…|
|`pmp-b5-242`|pmp-mocks-1-10|single|predictive|2|As a complex project draws to a close, the project manager realizes that several risks, initially assessed as low probability and…|
|`pmp-b5-243`|pmp-mocks-1-10|single|predictive|2|You are working on a project that involves the renovation and removal of an old industrial plant. When investigating the area…|
|`pmp-b5-244`|pmp-mocks-1-10|single|predictive|2|Upon discovering a significant quality issue with a key supplier's deliverables, the project manager needs to address the problem…|
|`pmp-b5-245`|pmp-mocks-1-10|single|predictive|2|Upon discovering a critical design issue in a software project, stakeholders are uncertain about proceeding with the…|
|`pmp-b5-246`|pmp-mocks-1-10|single|predictive|2|Upon noticing that the primary technology provider for a digital transformation project will not meet the deadline for delivering…|
|`pmp-b5-247`|pmp-mocks-1-10|single|agile|2|In the midst of a renewable energy project, a team member notices a significant risk related to equipment procurement that could…|
|`pmp-b5-248`|pmp-mocks-1-10|single|predictive|3|You have planned the scope, schedule, cost and resources with your team. You have also identified a large number of risks within…|
|`pmp-b5-249`|pmp-mocks-1-10|multi|predictive|3|Ahead of a crucial update meeting, a project manager learns about a potential unforeseen delay in a project segment that involves…|
|`pmp-b5-250`|pmp-mocks-1-10|single|predictive|3|You have collected the requirements for your project and are creating the scope statement and work breakdown structure. During…|
|`pmp-b5-251`|pmp-mocks-1-10|single|predictive|2|You are working through the risk assessment with your team, who are having trouble coming up with ideas for risks. You decide to…|
|`pmp-b5-252`|pmp-mocks-1-10|single|agile|2|You are working in an Agile team and the business representative would like to get an idea of the risk associated with a…|
|`pmp-b5-253`|pmp-mocks-1-10|single|predictive|2|A risk has been raised in your project around insufficient building materials. One risk response assigned to the functional…|
|`pmp-b5-254`|pmp-mocks-1-10|single|agile|3|You are working on a project where part of the project delivers a high value, high risk component. Another part of the project…|
|`pmp-b5-255`|pmp-mocks-1-10|single|hybrid|2|You are working on a project where there is a significant amount of uncertainty about the requirements. There is also a…|
|`pmp-b5-256`|pmp-mocks-1-10|single|agile|2|You are working on a project with a vendor where there is significant ambiguity involved and multiple options the team could go…|
|`pmp-b5-257`|pmp-mocks-1-10|single|hybrid|3|You are working on a project to improve some software that has been in production for over 17 years, and has been modified…|
|`pmp-b5-258`|pmp-mocks-1-10|single|predictive|3|You are working on a project that requires a very particular programming skillset that is not very common. It is also the hottest…|
|`pmp-b5-259`|pmp-mocks-1-10|single|agile|2|Your project team have found a significant data vulnerability in one of the features planned in your product backlog, where…|
|`pmp-b5-260`|pmp-mocks-1-10|single|predictive|2|You brainstorm risk responses with your project team and project sponsor, and decide you cannot transfer a certain high impact…|
|`pmp-b5-261`|pmp-mocks-1-10|single|predictive|2|You are working on a project to build a new customer database system for an engineering team. In working with the requirements,…|
|`pmp-b5-262`|pmp-mocks-1-10|single|agile|3|You are working as a product owner in an agile team, and during a recent risk review the team have uncovered various risks that…|
|`pmp-b5-263`|pmp-mocks-1-10|single|agile|2|You are working on an agile project and delivering a usable feature every few weeks. The project sponsor is from a waterfall…|
|`pmp-b5-264`|pmp-mocks-1-10|single|hybrid|3|You are a project manager assessing the risk of future releases with your team. You have a clear set of requirements from your…|
|`pmp-b5-265`|pmp-mocks-1-10|single|predictive|2|You have recently taken over a project. Your project team approach you to say there is a regulatory approval that has not been…|
|`pmp-b5-266`|pmp-mocks-1-10|single|agile|2|You are working on an agile project, where your project team have raised multiple risks to project delivery. Some of the risks…|
|`pmp-b5-267`|pmp-mocks-1-10|single|predictive|1|You are delivering a project to a functional area in your organisation. The project is highly complex and there are many risks to…|
|`pmp-b5-268`|pmp-mocks-1-10|single|predictive|3|You are working on a project where you discover a certain negative risk with a potential impact of $2,000,000. You meet with your…|
|`pmp-b5-269`|pmp-mocks-1-10|single|agile|2|You are the Agile project manager analysing the risk for each feature in your backlog by assigning a probability and an impact…|
|`pmp-b5-270`|pmp-mocks-1-10|single|predictive|2|You are working on a complex engineering project, where a significant amount of risk has been found with your team and the…|
|`pmp-b5-271`|pmp-mocks-1-10|single|predictive|1|You have been working on a large database transfer project, when the manager for data in your organisation identifies the aging…|
|`pmp-b5-272`|pmp-mocks-1-10|single|predictive|2|You are managing a project that has formed organically around an important process improvement. You have been given limited…|
|`pmp-b5-273`|pmp-mocks-1-10|single|predictive|1|You are delivering a brand new product line into your organisation, partly utilising a third party system. You have been working…|
|`pmp-b5-274`|pmp-mocks-1-10|single|predictive|2|A number of risks have been raised on your project. You perform qualitative analysis to get a subjective risk rating, but your…|
|`pmp-b5-275`|pmp-mocks-1-10|single|predictive|3|One of the risks on your project is significantly large, and could seriously impact your company if it ever came about. It has…|
|`pmp-b5-276`|pmp-mocks-1-10|single|agile|1|The Agile team is working through a particularly complex feature that is difficult for the team find a solution for. As the Agile…|
|`pmp-b5-277`|pmp-mocks-1-10|single|agile|2|You are the Agile project manager working on a software project, and the development team have identified a major risk for the…|
|`pmp-b5-278`|pmp-mocks-1-10|single|agile|1|You are an Agile project manager working closely with the Scrum Master and Agile team. The Scrum Master mentions that Agile works…|
|`pmp-b5-279`|pmp-mocks-1-10|single|predictive|2|You have been working on a project for some time, and it is only slightly behind schedule with a SPI of 0.96. Various Risks to…|
|`pmp-b5-280`|pmp-mocks-1-10|single|predictive|1|You have been working on your project for some time, when one of your project team tells you about an issue with the materials.…|
|`pmp-b5-281`|pmp-mocks-1-10|single|predictive|2|You are working through your project and performing quality inspections on the product. During inspection, you notice a key part…|

### business-6 · Continuous improvement — 88 items

*Use lessons learned and feed improvement back into the organisation.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b6-008`|core|single|agile|4|You are the scrum master for a team building a bus operator's ticketing app. Over the last fourteen sprints, customer-reported…|
|`pmp-b6-002`|core|multi|agile|3|Your team's retrospectives are well attended and people speak openly. Reviewing the last eight, you find forty-three actions…|
|`pmp-b6-003`|core|single|predictive|2|At closure you have a substantial lessons learned report. Your organisation has a lessons repository, and searching it you find…|
|`pmp-b6-004`|core|single|agile|2|Your team changed how it handles deployment eight months ago and has since reduced release failures from about one in four to…|
|`pmp-b6-009`|core|single|agile|4|Over a year, your team at a pensions administrator cut its average lead time from idea to production from eleven days to five,…|
|`pmp-b6-006`|core|fillblank|agile|3|Your team proposes automating a manual regression suite. Building the automation is estimated at 30 person-days. The manual suite…|
|`pmp-b6-007`|core|matching|agile|3|Prompts: The change to the team's own code review… / The change to the shared deployment pipe… / The recurring delay traced to the divisi…|
|`pmp-b6-021`|core|single|agile|3|Your team has used the same retrospective format for a year. Attendance is falling, the same points come up each time, and people…|
|`pmp-b6-022`|core|single|agile|2|A value stream map shows that a typical change takes twenty working days from request to release, but only three of those days…|
|`pmp-b6-023`|core|single|agile|4|A delivery director sets every team an improvement target of increasing velocity by twenty per cent over two quarters. Your team…|
|`pmp-b6-024`|core|single|hybrid|3|On a hybrid programme, an agile team has automated its regression tests and cut its test time from days to hours. The predictive…|
|`pmp-b6-025`|core|multi|agile|3|A team wants its retrospective actions to be completed rather than repeated sprint after sprint. Select two.|
|`pmp-b6-026`|core|single|hybrid|3|As part of your hybrid factory programme, a week-long kaizen event on a packaging line produces thirty improvement ideas from…|
|`pmp-b6-061`|core|graphic|hybrid|3|Your claims operations team logged every claim that needed rework last month and charted the causes, largest first, with the…|
|`pmp-case4-009`|case-04|single|predictive|3|The closure report from the museum's earlier gallery project attributed a three-month overrun to late case delivery and…|
|`pmp-b6-101`|business-6|single|predictive|3|You are managing a three-phase window and door replacement across 40 blocks for a housing association. Phase 1 has finished with…|
|`pmp-b6-102`|business-6|single|agile|1|You are the scrum master for a team building a savings app. The team missed its sprint goal, and the head of digital has asked to…|
|`pmp-b6-103`|business-6|single|hybrid|4|Your telecoms company's PMO set an improvement target for your hybrid programme: cut the average time to resolve defects raised…|
|`pmp-b6-104`|business-6|multi|predictive|3|You are managing installation of a new tablet-coating line at a pharmaceutical plant. Three successive operational qualification…|
|`pmp-b6-105`|business-6|matching|agile|2|Prompts: The team suspects most of a feature's el… / Some developers think pairing on every s… / A checkout outage on Saturday cost three…|
|`pmp-b6-106`|business-6|single|predictive|3|You manage signalling upgrades for a rail engineering consultancy. Comparing your last three projects, you find the company's…|
|`pmp-b6-107`|business-6|single|agile|3|Your team on a travel-booking platform normally reserves about 20% of each sprint for improvement work such as test clean-up and…|
|`pmp-b6-108`|business-6|fillblank|hybrid|3|An insurer's claims team handles 1,200 claims a month. Before an improvement, 15% of claims were returned for rework, each return…|
|`pmp-b6-109`|business-6|single|predictive|5|You are planning conservation surveys of six listed buildings for a city council. The lessons repository holds a lesson from a…|
|`pmp-b6-110`|business-6|hotspot|hybrid|3|A warehouse software team changed its code review process after week 8. The control chart plots escaped defects per week, with…|
|`pmp-b6-111`|business-6|single|agile|2|After a failed release corrupted appointment data at a hospital trust last quarter, your team added a step: verify that the…|
|`pmp-b6-112`|business-6|single|predictive|4|You are managing the design of a water treatment works upgrade. At the end of the detailed design phase, the lessons session…|
|`pmp-b6-113`|business-6|multi|agile|4|You coach a marketing content team that uses a Kanban board. Over two months, average lead time for a campaign asset has risen…|
|`pmp-b6-114`|business-6|matching|hybrid|3|Prompts: The pilot was given to the branch with t… / After peer checking was introduced, the… / Processing time per mortgage application…|
|`pmp-b6-115`|business-6|pulldown|predictive|2|Prompts: In month two, onboarding the first suppl… / At project closure, the lessons learned…|
|`pmp-b6-116`|business-6|single|agile|5|Your team at an energy retailer ran a four-sprint experiment doing all its work by mob programming. Escaped defects fell by 60%,…|
|`pmp-b6-117`|business-6|single|hybrid|3|You run a hybrid campus redevelopment programme for a university. Each week, four team leads spend about six hours between them…|
|`pmp-b6-118`|business-6|single|predictive|3|On an offshore wind farm construction project, you introduced full incoming inspection of every turbine component delivery after…|
|`pmp-b6-119`|business-6|single|predictive|2|You are managing a €14 million replacement of the baggage-handling system at a regional airport. After two airlines moved their…|
|`pmp-b6-120`|business-6|single|agile|1|On day three of a ten-day sprint, your logistics-app team realises that its own test-data reset script, which every developer…|
|`pmp-b6-121`|business-6|single|agile|2|The head of engineering at an online insurer wants to spread good practice across her five Scrum teams. She has noticed that Team…|
|`pmp-b6-122`|business-6|single|predictive|3|You are managing a housebuilder's 120-home development. Water tests on the first completed terrace showed leaks at a window-head…|
|`pmp-b6-123`|business-6|single|predictive|3|Your project redesigned the dispensing process in a hospital pharmacy, and dispensing errors have fallen from 1.2% to 0.5% over…|
|`pmp-b6-124`|business-6|single|predictive|5|You are closing a £38 million railway station refurbishment. Late design information caused delays on both sides, and the main…|
|`pmp-b6-125`|business-6|hotspot|predictive|4|You are managing the refurbishment of a 300-room hotel. The chart shows snagging defects from the first four floors by category,…|
|`pmp-b6-126`|business-6|fillblank|predictive|3|A council project is improving how householder planning applications are processed. Each application passes through three steps…|
|`pmp-b6-127`|business-6|multi|agile|3|A games studio's development team wants to try 'no-meeting Wednesdays' to give developers longer uninterrupted time, hoping to…|
|`pmp-b6-128`|business-6|matching|agile|2|Prompts: Each change request needs a signed paper… / Finished features pass all their tests b… / An eligibility report built at stakehold…|
|`pmp-b6-129`|business-6|pulldown|hybrid|3|Prompts: The team has designed a new loading sequ… / The four-week trial at one depot has fin…|
|`pmp-b6-130`|business-6|single|hybrid|3|On a hybrid data-migration programme for a water utility, you persuaded the workstream leads to replace monthly integration…|
|`pmp-b6-131`|business-6|single|agile|3|Your e-learning platform team has been quietly adding a day or two to most story estimates to cover refactoring and test…|
|`pmp-b6-132`|business-6|single|hybrid|4|Your medical device programme runs software in sprints and hardware through stage gates. Another programme has proven an…|
|`pmp-b6-133`|business-6|multi|predictive|3|You manage packaging changeovers for a food manufacturer's product relaunch, with 20 more deliveries due from the film supplier…|
|`pmp-b6-134`|business-6|multi|hybrid|4|On a smart-meter programme, three software squads hold fortnightly retrospectives, while the hardware workstream records lessons…|
|`pmp-b6-135`|business-6|single|agile|3|Your team on a charity's donations platform wants to reduce cycle time, which averages nine days per story, and proposes…|
|`pmp-b6-1`|pmp-lectures-1-11|single|predictive|2|During a project closing review meeting, the project manager notes that a software development task took longer to complete than…|
|`pmp-b6-2`|pmp-lectures-1-11|single|agile|2|During project closure, the team considers that the final sprint retrospective is not necessary because there are no remaining…|
|`pmp-b6-3`|pmp-lectures-1-11|single|agile|4|An approved project charter stated that an agile approach must be used. During a lessons learned session, the client indicates…|
|`pmp-b6-201`|pmp-mocks-1-10|single|agile|3|A project manager is managing a project for a successful startup. The startup has shown exponential growth with multiple benefits…|
|`pmp-b6-202`|pmp-mocks-1-10|single|agile|2|In the middle of an iteration for the development of a project deliverable, a team member suggests a new method of doing the…|
|`pmp-b6-203`|pmp-mocks-1-10|single|agile|2|An agile team consistently delivers features on time but recently identified a significant architecture gap leading to rework.|
|`pmp-b6-204`|pmp-mocks-1-10|single|predictive|2|A project is currently in progress. The project manager has been notified of a new process that may be helpful if it is…|
|`pmp-b6-205`|pmp-mocks-1-10|single|agile|3|A project team's performance has declined for the last three iterations. Some of the team members are disappointed, and raise…|
|`pmp-b6-206`|pmp-mocks-1-10|single|agile|2|A project team has recently finished a sprint for developing two features for a fleet management system. Although the team…|
|`pmp-b6-207`|pmp-mocks-1-10|single|agile|2|During a retrospective meeting, team members show disengagement, stating that they are repeating the same ideas over and over.|
|`pmp-b6-208`|pmp-mocks-1-10|single|agile|2|A newly formed team has become accustomed to agile practices. The project lead has noticed that while they are performing…|
|`pmp-b6-209`|pmp-mocks-1-10|single|agile|2|An agile project is running its fifth iteration out of 10. After the last retrospective, the team members realize that they had…|
|`pmp-b6-210`|pmp-mocks-1-10|single|agile|2|A senior manager informs a project manager that the project is not performing as well as other projects.|
|`pmp-b6-211`|pmp-mocks-1-10|single|agile|2|At the end of an iteration, a team member tells the project manager that a planned task is unfinished because of an issue that…|
|`pmp-b6-212`|pmp-mocks-1-10|single|agile|2|A product team is using a prototyping approach to deliver a multiyear business initiative. The project manager notices that a few…|
|`pmp-b6-213`|pmp-mocks-1-10|single|agile|2|In an agile environment, a project leader should always consider a time for open and free discussions between team members on how…|
|`pmp-b6-214`|pmp-mocks-1-10|single|hybrid|3|A company has been following a predictive approach and always waits for the preceding phase to be completed before evaluating the…|
|`pmp-b6-215`|pmp-mocks-1-10|single|agile|2|A project manager is running an agile project that is half completed. During the most recent sprint retrospective, the team…|
|`pmp-b6-216`|pmp-mocks-1-10|single|agile|3|A project manager typically conducts retrospective sessions at the end of a project. However, the sponsor believes that…|
|`pmp-b6-217`|pmp-mocks-1-10|multi|hybrid|3|A team member in a hybrid organization informs the project manager of a new way to execute an activity with a shorter duration.…|
|`pmp-b6-218`|pmp-mocks-1-10|single|agile|2|An agile project team is struggling to keep track of the changes being applied to the product, because its process is cumbersome…|
|`pmp-b6-219`|pmp-mocks-1-10|single|agile|2|A company is transitioning from a predictive to an agile project management approach. The project manager has noticed that team…|
|`pmp-b6-220`|pmp-mocks-1-10|single|agile|1|After a few iterations, the project manager feels that even though the team is completing the work as planned, the team is not…|
|`pmp-b6-221`|pmp-mocks-1-10|single|agile|3|The latest software build for a virtual reality headset release did NOT include urgent change requests already implemented by the…|
|`pmp-b6-222`|pmp-mocks-1-10|single|agile|2|A project manager convenes an emergency session with the software development team to address significant bugs that are delaying…|
|`pmp-b6-223`|pmp-mocks-1-10|single|agile|2|During an iterative development project for a new software tool, a developer who was recently added to the team suggests…|
|`pmp-b6-224`|pmp-mocks-1-10|single|hybrid|2|Your project is starting to go off track due to a large amount of uncertainty in the requirements and resources. At a significant…|
|`pmp-b6-225`|pmp-mocks-1-10|single|hybrid|2|You have recently taken over on a project that is behind schedule, and slightly over budget. You raise an issue at the steering…|
|`pmp-b6-226`|pmp-mocks-1-10|single|hybrid|3|Your Project Management Office (PMO) is optimising all the projects within their portfolio. They ask for your advice in tracking…|
|`pmp-b6-227`|pmp-mocks-1-10|single|agile|2|You have been working as a project manager in an organisation for some time, where an agile- centric delivery approach and way of…|
|`pmp-b6-228`|pmp-mocks-1-10|single|agile|2|You have been working on a project to deliver a new customer data system, and have noticed that the work is starting to slow…|
|`pmp-b6-229`|pmp-mocks-1-10|single|agile|2|You have been assigned to take over an existing project as the previous project manager leaves for another opportunity. In…|
|`pmp-b6-230`|pmp-mocks-1-10|single|agile|2|You are working as a Product Owner. While your sales and revenue have been increasing over the past eight months, the overall…|
|`pmp-b6-231`|pmp-mocks-1-10|single|agile|1|You are an Agile team leader and talking through the process for your first retrospective. The team is curious as to what they…|
|`pmp-b6-232`|pmp-mocks-1-10|single|agile|1|You are new to your Agile project, and the team are working through the forming stages.|
|`pmp-b6-233`|pmp-mocks-1-10|single|agile|1|You are finishing up the first iteration on your Agile project and have set a retrospective meeting for the whole team. One…|
|`pmp-b6-234`|pmp-mocks-1-10|single|agile|1|The 12th principle of Agile is "At regular intervals, the team reflects on how to become more effective, then tunes and adjusts…|
|`pmp-b6-235`|pmp-mocks-1-10|single|agile|1|Retrospectives are typically held at the end of an iteration, but an Agile team can also hold retrospectives at any other time.|

### business-7 · Organisational change — 97 items

*Read the organisation's culture and manage the change the project causes.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b7-001`|core|multi|hybrid|4|A new expenses system technically launched on time and works correctly. Six weeks later, 70 percent of claims are still being…|
|`pmp-b7-002`|core|pulldown|hybrid|3|Prompts: Depot staff believe the system will be u… / Depot staff are willing but the new proc… / Depot supervisors quietly prefer the old…|
|`pmp-b7-003`|core|single|agile|5|Six months into an agile transformation, teams run every ceremony correctly and their boards are immaculate. Yet requirements…|
|`pmp-b7-004`|core|single|agile|4|Your new case management system goes live for a frontline team in five weeks. In the same quarter that team is also absorbing a…|
|`pmp-b7-005`|core|pulldown|hybrid|3|Prompts: For the controllers using the system for… / For the drivers who will wait longer for…|
|`pmp-b7-006`|core|single|predictive|4|Six weeks after rolling a new client management system out to fourteen branches, usage varies enormously. In the five branches…|
|`pmp-b7-008`|core|single|hybrid|4|Your hybrid project is running a 12-week trial of cross-skilled cell teams at two of a food manufacturer's five plants, with the…|
|`pmp-b7-021`|core|single|agile|3|Your team releases new features to a hospital's staff rostering app every two weeks. Ward managers complain that the screens keep…|
|`pmp-b7-022`|core|single|agile|2|A pilot group of twenty staff uses each new increment of an expenses system before it reaches the whole organisation. Their…|
|`pmp-b7-023`|core|single|agile|4|During an agile transformation, middle managers are slowing approvals and quietly discouraging teams from self-organising. In…|
|`pmp-b7-024`|core|single|hybrid|3|On a hybrid warehouse programme, staff were trained on the new picking software three months before the new conveyor equipment…|
|`pmp-b7-025`|core|multi|agile|3|Six weeks after a new case recording system went live, the sponsor asks whether the change has been adopted. Select two.|
|`pmp-b7-026`|core|single|hybrid|4|Two teams merged for your hybrid programme have very different cultures. One documents every decision formally; the other works…|
|`pmp-case3-006`|case-03|single|hybrid|3|At Site B, staff have started keeping a spreadsheet of results alongside the test system just in case, and several have told…|
|`pmp-case5-001`|case-05|single|hybrid|3|Exhibit 5 shows that only 41 per cent of Calder advisers understand why the change is happening, against 78 per cent at Northway,…|
|`pmp-b7-101`|business-7|single|predictive|1|You are managing a project that moves a hospital's ward medicines from nurse-held stock to pharmacy-dispensed patient packs.…|
|`pmp-b7-102`|business-7|single|predictive|3|Your project is introducing a new structural design-checking tool at an engineering consultancy where junior engineers rarely…|
|`pmp-b7-103`|business-7|single|predictive|3|You are leading the introduction of a labour-scheduling system at a distribution centre in the Netherlands. It will move…|
|`pmp-b7-104`|business-7|single|predictive|4|A county council is rolling out a new purchasing process in three waves. Wave one went well, largely because the finance director…|
|`pmp-b7-105`|business-7|single|agile|5|An online retailer has moved six teams to persistent product teams that report progress against quarterly outcomes. The executive…|
|`pmp-b7-106`|business-7|single|agile|3|Your agile team ships an update every two weeks to the scheduling app used by 900 home-care workers. Recent updates have moved…|
|`pmp-b7-107`|business-7|single|agile|2|Your agile team at an insurer has delivered four new features to the claims assessment tool this quarter, each meeting the…|
|`pmp-b7-108`|business-7|single|agile|4|A building society's pilot agile team halved its mortgage-product lead time in nine months. The chief operating officer now wants…|
|`pmp-b7-109`|business-7|single|hybrid|3|An aircraft maintenance company is replacing paper task cards with tablets, using fixed hangar go-live dates and agile software…|
|`pmp-b7-110`|business-7|single|hybrid|4|Your hybrid project is moving a university's departments to a self-service purchasing system in which staff buy low-value items…|
|`pmp-b7-111`|business-7|single|hybrid|2|A housing association is introducing a new repairs-booking process for its 60 trades operatives. HR has nominated six change…|
|`pmp-b7-112`|business-7|multi|predictive|3|A shared services centre is automating invoice matching, which will remove much of the manual work done by its 45 accounts…|
|`pmp-b7-113`|business-7|multi|agile|4|Your agile team has launched a self-service portal for a travel company's customers to change their bookings. Uptake is far below…|
|`pmp-b7-114`|business-7|matching|hybrid|3|Prompts: A retail cooperative where significant c… / A franchise network where each store is… / A rail maintenance contractor where work…|
|`pmp-b7-115`|business-7|matching|predictive|2|Prompts: An engineer privately asks whether the n… / Engineers at one depot want someone who… / The network and customer-service directo…|
|`pmp-b7-116`|business-7|pulldown|agile|3|Prompts: Funding is still allocated each year to… / Appraisals still rank individuals on per…|
|`pmp-b7-117`|business-7|fillblank|predictive|3|A rail infrastructure company must train 340 track engineers on a new inspection procedure before it replaces the old one. Each…|
|`pmp-b7-118`|business-7|hotspot|hybrid|3|A law firm replaced its document management system three months ago. Fee earners have stopped arguing that the old system was…|
|`pmp-b7-119`|business-7|single|predictive|1|You are managing a project that replaces a university's key-and-logbook system with card-based access control across 40…|
|`pmp-b7-120`|business-7|single|predictive|2|A builders' merchant has moved its accounts receivable team from an old ledger to a new finance system. The approved transition…|
|`pmp-b7-121`|business-7|single|predictive|3|A pharmaceutical wholesaler is moving three warehouses to a new picking process. The steering committee holds the go/no-go…|
|`pmp-b7-122`|business-7|single|predictive|4|Your project gave a customs brokerage an automated declaration-checking tool. Adoption is at 92% and the project closes in four…|
|`pmp-b7-123`|business-7|single|predictive|5|Your project has replaced paper near-miss forms with a mobile app for 300 technicians at an offshore wind maintenance contractor.…|
|`pmp-b7-124`|business-7|single|agile|2|Five months into an agile transformation in a parcel carrier's IT department, early enthusiasm is fading. Sceptical business…|
|`pmp-b7-125`|business-7|single|agile|3|A national charity is trialling agile ways of working before deciding whether to adopt them more widely. The sponsor wants the…|
|`pmp-b7-126`|business-7|single|agile|3|At a fashion e-commerce company, the chief executive launched the move to empowered product teams and talks about it at every…|
|`pmp-b7-127`|business-7|single|agile|4|A public broadcaster is retiring the scheduling tool one of its teams built and maintained for twelve years, and that team now…|
|`pmp-b7-128`|business-7|single|hybrid|3|Your hybrid project is replacing a national museum group's finance system, and go-live is fixed for 1 March. HR's restructure of…|
|`pmp-b7-129`|business-7|multi|agile|3|Your agile team is about to release a knowledge base to the 220 agents at a telecoms support centre. The intent is that agents…|
|`pmp-b7-130`|business-7|multi|agile|4|A pensions administrator is moving from separate analysis, development and testing departments to cross-functional product teams.…|
|`pmp-b7-131`|business-7|multi|hybrid|3|After an energy supplier acquired a smaller rival, your hybrid project must move both customer service teams onto one complaints…|
|`pmp-b7-132`|business-7|matching|predictive|2|Prompts: A payroll change for 5,000 staff, where… / A new stock-ordering process for 240 nea… / A new interest calculation that a regula…|
|`pmp-b7-133`|business-7|pulldown|hybrid|3|Prompts: Training for all 400 staff was planned a… / The third release lets general managers…|
|`pmp-b7-134`|business-7|fillblank|predictive|3|A utility's billing team of 60 advisers each handles 40 account queries a day. After the new billing system goes live, past…|
|`pmp-b7-135`|business-7|hotspot|hybrid|3|You are planning change support for an airport ground-handling company's move to electronic rostering. The diagram places each…|
|`pmp-b7-1`|pmp-lectures-1-11|single|hybrid|3|A company has been using predictive methods for a very long time and is trying to implement a few iterative initiatives within…|
|`pmp-b7-2`|pmp-lectures-1-11|single|hybrid|3|In a company that recently adopted hybrid practices, a project is struggling to perform as expected. Neither the project manager…|
|`pmp-b7-3`|pmp-lectures-1-11|single|agile|4|Due to market changes, a company decided to speed up its product release schedule, moving from yearly cycles to quarterly cycles.…|
|`pmp-b7-4`|pmp-lectures-1-11|single|predictive|3|A new project manager joins an organization that has never had formal project management processes. Some team members refuse to…|
|`pmp-b7-201`|pmp-mocks-1-10|single|hybrid|2|A chief technology officer (CTO) is enthusiastic about an organization's recent adoption of servant leadership practices. The CTO…|
|`pmp-b7-202`|pmp-mocks-1-10|single|hybrid|2|During the project kick-off meeting, the project manager announces that a hybrid approach will be used to deliver the project.…|
|`pmp-b7-203`|pmp-mocks-1-10|single|hybrid|3|Due to the complex nature of a predictive project, an organization decides to use an agile approach for a project phase. A team…|
|`pmp-b7-204`|pmp-mocks-1-10|single|predictive|3|A project manager is handling multiple projects in five different countries and is issued a new directive on workplace attire and…|
|`pmp-b7-205`|pmp-mocks-1-10|single|agile|2|A project manager is assigned to a mobile application development project. The project manager realizes that an agile approach is…|
|`pmp-b7-206`|pmp-mocks-1-10|single|predictive|3|A company is going through an organizational change. Most of the senior management has changed, including the project manager's…|
|`pmp-b7-207`|pmp-mocks-1-10|multi|agile|4|A project manager new to an organization has been tasked with running the first agile project. The project manager has decided to…|
|`pmp-b7-208`|pmp-mocks-1-10|single|agile|3|A project manager is leading a project that entails transitioning to agile. The project will pilot the agile delivery modeled…|
|`pmp-b7-209`|pmp-mocks-1-10|single|agile|3|A company has decided to form agile teams within the organization to improve project results and take advantage of new…|
|`pmp-b7-210`|pmp-mocks-1-10|single|agile|3|A parent company announces an integration project for all of its subsidiary companies. The integration project covers the legal,…|
|`pmp-b7-211`|pmp-mocks-1-10|single|hybrid|3|An international company is implementing an innovative technology at one of its production plants. The technology has already…|
|`pmp-b7-212`|pmp-mocks-1-10|single|agile|3|A large corporation is transforming itself from a predictive to agile approach. A project team with knowledge of agile practices…|
|`pmp-b7-213`|pmp-mocks-1-10|single|hybrid|3|A project manager seeks to alleviate the IT department's workload while empowering the business to initiate new implementations…|
|`pmp-b7-214`|pmp-mocks-1-10|single|agile|2|A project manager is helping their organization transition from using predictive methods to agile methods. The leadership team…|
|`pmp-b7-215`|pmp-mocks-1-10|single|hybrid|2|A company's project management office (PMO) is incorporating more adaptive technologies and has hired a new project manager with…|
|`pmp-b7-216`|pmp-mocks-1-10|single|agile|2|A company is considering adopting the agile methodology for future projects. The project manager is leading a discussion focused…|
|`pmp-b7-217`|pmp-mocks-1-10|single|agile|3|A project manager is helping an organization transition from traditional methods to agile. The infrastructure, data warehousing,…|
|`pmp-b7-218`|pmp-mocks-1-10|single|agile|4|An agile coach is counseling a large software organization that has been using waterfall methodology and now plans to start using…|
|`pmp-b7-219`|pmp-mocks-1-10|single|agile|3|A project manager in an organization is considering implementing an agile project delivery. The organization has a strong project…|
|`pmp-b7-220`|pmp-mocks-1-10|single|predictive|2|The project manager is having difficulties persuading stakeholders to adopt new technlogy in the project.|
|`pmp-b7-221`|pmp-mocks-1-10|single|predictive|3|A project manager participates in a focus group that implements project management changes across the organization. The goal is…|
|`pmp-b7-222`|pmp-mocks-1-10|multi|agile|3|A project manager in a project management office (PMO) is tasked with implementing an agile framework. The leadership team…|
|`pmp-b7-223`|pmp-mocks-1-10|single|hybrid|2|A project manager is assigned to a new multinational project. This will be the project manager's first time working with team…|
|`pmp-b7-224`|pmp-mocks-1-10|single|agile|2|A project manager in a predictive environment has recently completed agile training and is starting to manage projects in an…|
|`pmp-b7-225`|pmp-mocks-1-10|single|hybrid|3|A project manager is halfway through a project using a predictive methodology when the organization announces a transition to a…|
|`pmp-b7-226`|pmp-mocks-1-10|single|predictive|2|A project manager has been assigned to lead a cross-functional project team. During a brainstorming session, the project manager…|
|`pmp-b7-227`|pmp-mocks-1-10|single|hybrid|3|In order to determine a new hybrid approach for a project, the project manager decides that an assessment of the organizational…|
|`pmp-b7-228`|pmp-mocks-1-10|single|agile|2|A project manager decided that using an agile approach for a new project will deliver the best outcomes. Some of the team members…|
|`pmp-b7-229`|pmp-mocks-1-10|single|agile|2|Team members are transferred from a predictive project to a newly formed agile team. The new team members are expressing…|
|`pmp-b7-230`|pmp-mocks-1-10|single|agile|3|A project manager has been engaged by a managing director to transform the project management office (PMO) from a waterfall…|
|`pmp-b7-231`|pmp-mocks-1-10|multi|agile|2|A team is transitioning from a predictive to an agile approach. (Choose two)|
|`pmp-b7-232`|pmp-mocks-1-10|single|hybrid|3|An organizational change program has met with internal resistance, a risk which was previously identified. A logistics manager is…|
|`pmp-b7-233`|pmp-mocks-1-10|single|predictive|2|In a major software development project, the project manager finds that several team members are reluctant to use new project…|
|`pmp-b7-234`|pmp-mocks-1-10|single|predictive|3|A project manager is overseeing the upgrade of an old manufacturing system to a new automated solution, aligning with the…|
|`pmp-b7-235`|pmp-mocks-1-10|single|agile|2|A firm planning to transition to lean development techniques, the project manager encounters resistance from several world wide…|
|`pmp-b7-236`|pmp-mocks-1-10|single|predictive|2|As a project manager in charge of a new sustainability initiative, you've introduced a groundbreaking environmental monitoring…|
|`pmp-b7-237`|pmp-mocks-1-10|single|predictive|1|As part of a company-wide project to enhance IT security, the project manager schedules a series of educational webinars for…|
|`pmp-b7-238`|pmp-mocks-1-10|single|predictive|2|As your project progresses, many of the business stakeholders are starting to voice their concerns, to question some of the…|
|`pmp-b7-239`|pmp-mocks-1-10|single|agile|2|Your deliverables have been signed off by the Product Owner and are ready to be released to the customer, which is the financial…|
|`pmp-b7-240`|pmp-mocks-1-10|single|predictive|2|You are leading a large organisational change that affects more than a thousand people across multiple teams. You have engaged…|
|`pmp-b7-241`|pmp-mocks-1-10|single|agile|1|You working as an Agile project manager in a team with many stakeholders who have not worked on an Agile project before. Some of…|
|`pmp-b7-242`|pmp-mocks-1-10|single|agile|2|You are an Agile project manager tasked with evolving your part of the organisation into Agile.|
|`pmp-b7-243`|pmp-mocks-1-10|single|predictive|2|You have been assigned to a project that is half way through its delivery. You analyze the stakeholders involved in the project…|

### business-8 · External environment — 84 items

*Watch regulation, technology, market and geopolitics for impacts on scope.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-b8-001`|core|single|predictive|3|Eight months into a two-year project building an invoicing tool for small accountancy firms, a competitor releases a product that…|
|`pmp-b8-002`|core|single|hybrid|4|Your hardware rollout depends on a component manufactured in a single country that has just introduced export licensing for that…|
|`pmp-b8-003`|core|single|predictive|4|A regulator has published a consultation proposing rules that, if adopted as drafted, would require a reporting capability your…|
|`pmp-b8-004`|core|single|agile|2|A third-party service your product calls for address validation announces it will retire the version you use in nine months, with…|
|`pmp-b8-005`|core|multi|predictive|3|Your project imports specialist equipment under a two-year contract priced in the supplier's currency. Since signature that…|
|`pmp-b8-006`|core|single|hybrid|5|An industry body has launched a security certification scheme for products in your sector. It carries no legal force. Within four…|
|`pmp-b8-007`|core|single|predictive|4|Your eighteen-month energy retrofit project depends on a government scheme that reimburses thirty per cent of capital cost. The…|
|`pmp-b8-021`|core|single|agile|3|Your cloud provider announces that a service your product uses heavily will cost forty per cent more from next quarter. The…|
|`pmp-b8-022`|core|single|agile|2|Your product serves a sector where the regulator publishes new guidance most months. The team only reviews external changes at…|
|`pmp-b8-023`|core|single|agile|4|Your invoicing product's main customer sector has announced it will require a new electronic invoicing standard in eighteen…|
|`pmp-b8-024`|core|single|hybrid|3|Disruption on a major shipping route will delay imported transformers for your hybrid substation project by an unknown number of…|
|`pmp-b8-025`|core|multi|agile|3|You lead a team building a personal finance app. Select two.|
|`pmp-b8-026`|core|single|hybrid|4|Your hybrid programme is building a manual document review workflow for an insurer. The platform vendor has just released an…|
|`pmp-case2-009`|case-02|multi|agile|3|The national government has announced a common data standard for parking permits that every council must adopt within eighteen…|
|`pmp-case4-007`|case-04|multi|predictive|4|The government has announced changes to the national indemnity scheme for loaned objects, which could alter the conditions the…|
|`pmp-b8-101`|business-8|single|predictive|3|You manage a rail signalling upgrade. At 14:00 the procurement team forwards a government notice: the parent company of your…|
|`pmp-b8-102`|business-8|single|agile|3|Your team's SaaS product uses an open-source search engine. Its maintainers announce that future versions will be released under…|
|`pmp-b8-103`|business-8|single|predictive|3|Your office refurbishment imports aluminium cladding. The government has announced a 25% tariff on such goods arriving after 1…|
|`pmp-b8-104`|business-8|single|agile|1|You are the agile delivery lead for a home-energy monitoring app. Over the last two quarters, 72% of new sign-ups have come…|
|`pmp-b8-105`|business-8|multi|predictive|3|A district council is building an £18m leisure centre funded by borrowing. Construction is 40% complete. A rise in interest rates…|
|`pmp-b8-106`|business-8|single|predictive|2|Your hospital wing build has an electrical subcontract with a clause adjusting labour rates in line with the national wage…|
|`pmp-b8-107`|business-8|matching|hybrid|3|Prompts: An amendment to grid connection rules ha… / The government has opened a review of pl… / A port strike that began this morning ha…|
|`pmp-b8-108`|business-8|matching|agile|3|Prompts: Cancellations have risen four points sin… / The main phone operating system's releas… / Customers have asked in interviews to ph…|
|`pmp-b8-109`|business-8|pulldown|hybrid|3|Prompts: The contract cites a pipework standard b… / The contract requires compliance with th…|
|`pmp-b8-110`|business-8|fillblank|predictive|3|Your bridge refurbishment contract is worth €2,400,000, of which 35% is subject to a steel price adjustment clause. The clause…|
|`pmp-b8-111`|business-8|hotspot|predictive|2|Your team keeps an external environment scan for a solar farm due for handover in March 2028. The grid operator has announced…|
|`pmp-b8-112`|business-8|single|agile|5|Your team is building a payments app that connects to a partner bank. The bank will accept only the new version of the industry…|
|`pmp-b8-113`|business-8|single|hybrid|3|Your hybrid project is fitting out a data centre hall, with its monitoring software built in sprints. The test plan your team…|
|`pmp-b8-114`|business-8|single|predictive|4|You are managing a primary school extension. Planning consent was granted on flood data placing the 1-in-100-year flood level 0.3…|
|`pmp-b8-115`|business-8|single|agile|2|Mid-sprint, a critical vulnerability is published in a logging library your e-commerce platform uses in production. It is being…|
|`pmp-b8-116`|business-8|single|hybrid|4|Your hybrid programme at a streaming company relies on 40 freelance developers engaged through an online platform. A court has…|
|`pmp-b8-117`|business-8|multi|agile|4|Your team builds a cloud HR platform. A country that provides 12% of your revenue has passed a law, effective in ten months,…|
|`pmp-b8-118`|business-8|single|hybrid|5|Your hybrid programme is building a route-planning platform for courier firms, launching in five months. The sales director tells…|
|`pmp-b8-119`|business-8|single|predictive|3|You are managing a €6m resurfacing contract on a regional motorway. The road-space permit covers July and August only and allows…|
|`pmp-b8-120`|business-8|single|agile|1|Your agile team runs the website and app for an online rail-ticket retailer. The company's legal team has confirmed that a new…|
|`pmp-b8-121`|business-8|single|predictive|2|Your project is installing a new packaging line at a dairy. The equipment is supplied under a supply-and-commission contract that…|
|`pmp-b8-122`|business-8|single|agile|3|You are the agile delivery lead for a language-learning app. Three weeks ago the most widely used web browser began blocking…|
|`pmp-b8-123`|business-8|single|hybrid|5|Your hybrid project is redeveloping a maritime museum: the building works are fixed-price and the digital exhibits are built in…|
|`pmp-b8-124`|business-8|single|predictive|4|You are the client-side project manager for a seven-storey student residence designed in cross-laminated timber, due to start on…|
|`pmp-b8-125`|business-8|single|agile|3|Your team at a freight forwarder has four sprints of backlog to build a tool that extracts data from scanned customs documents.…|
|`pmp-b8-126`|business-8|single|predictive|2|You manage a council project to redesign a town-centre junction. A six-week public consultation, with leaflets and drop-in…|
|`pmp-b8-127`|business-8|single|agile|4|You coach a nine-person team building an insurance claims portal. Four developers work from a city abroad where, during political…|
|`pmp-b8-128`|business-8|single|hybrid|3|Your hybrid project is moving a ferry operator's booking system to a new platform; the migration tooling is built in sprints and…|
|`pmp-b8-129`|business-8|multi|predictive|3|You manage the construction of a district heating network that will need 60 qualified pipe welders from September, four months…|
|`pmp-b8-130`|business-8|multi|agile|3|Your team builds a fitness streaming app sold by monthly subscription. A consumer protection law, effective in five months,…|
|`pmp-b8-131`|business-8|multi|hybrid|4|Your hybrid project is delivering remote monitoring for a water company's 140 pumping stations: the field hardware is installed…|
|`pmp-b8-132`|business-8|matching|predictive|3|Prompts: The government announces an enhanced tax… / A revised national standard for laborato… / The country where your fume cupboards ar…|
|`pmp-b8-133`|business-8|pulldown|predictive|3|Prompts: After the contract was signed, a new nat… / A volcanic eruption closed the region's…|
|`pmp-b8-134`|business-8|fillblank|hybrid|3|Your hybrid project is replacing a distribution warehouse, with its stock-control software built in sprints. Demolition will send…|
|`pmp-b8-135`|business-8|hotspot|agile|2|Your agile team ships releases of a pharmacy dispensing system on fixed dates. A national rule takes effect on 1 November:…|
|`pmp-b8-1`|pmp-lectures-1-11|single|predictive|3|An oil refinery project is in the middle of the execution phase. The project team found that the project is delayed because of an…|
|`pmp-b8-2`|pmp-lectures-1-11|single|predictive|3|Organizations face different challenges within their operational areas due to external factors. This has required companies to…|
|`pmp-b8-3`|pmp-lectures-1-11|single|agile|3|A project manager is overseeing an agile project that has finalized scope and compliance requirements. During a regular project…|
|`pmp-b8-4`|pmp-lectures-1-11|single|hybrid|4|A project manager is currently overseeing a hybrid project that has specific scope and quality requirements. However, due to…|
|`pmp-b8-5`|pmp-lectures-1-11|single|predictive|4|A project manager is leading a project that requires approval from a government agency. The agency is currently drafting…|
|`pmp-b8-201`|pmp-mocks-1-10|single|agile|3|During the daily standup meeting, one of the team members informs the team that a user story that was initially part of the…|
|`pmp-b8-202`|pmp-mocks-1-10|single|predictive|3|A project manager had just started a long-term project when the government implemented new legislation that all engineers working…|
|`pmp-b8-203`|pmp-mocks-1-10|single|agile|4|An agile practitioner has been assigned to lead a software development project in a relatively new industry where the external…|
|`pmp-b8-204`|pmp-mocks-1-10|single|hybrid|3|A new regulation has been approved that will impact the deliverables in the fourth phase of the project. The delivery is…|
|`pmp-b8-205`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a wastewater treatment project for a factory. During project execution, the government unexpectedly…|
|`pmp-b8-206`|pmp-mocks-1-10|single|hybrid|3|A team is working on a new product using a gated approach. After the third sprint they noticed that the market needs may be…|
|`pmp-b8-207`|pmp-mocks-1-10|single|predictive|3|A project manager is assigned to a water treatment investment project. The local government has recently passed a law that…|
|`pmp-b8-208`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a project that will deliver a solution in country A. Due to the potential for a security breach,…|
|`pmp-b8-209`|pmp-mocks-1-10|single|predictive|3|During a business status meeting, the project manager acknowledges that several market conditions have recently changed…|
|`pmp-b8-210`|pmp-mocks-1-10|single|predictive|2|A project is under scrutiny for its amount of water usage. The government's water licensing branch is changing its policy to be…|
|`pmp-b8-211`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a project in its implementation phase when a stakeholder informs them that sales have suddenly…|
|`pmp-b8-212`|pmp-mocks-1-10|single|predictive|3|At the midway point of a project, the project manager was informed of a market movement that would make the project results…|
|`pmp-b8-213`|pmp-mocks-1-10|single|predictive|3|A project manager learns that a new law will go into effect that will regulate the type of products the company can manufacture.…|
|`pmp-b8-214`|pmp-mocks-1-10|single|predictive|3|A company has an existing contract to maintain and enhance a client's customer database. The company's legal department informs…|
|`pmp-b8-215`|pmp-mocks-1-10|single|predictive|3|A local government passes a law that affects the scope of a project that is currently in the execution phase. The regulation…|
|`pmp-b8-216`|pmp-mocks-1-10|single|agile|3|An organization is launching a new product that is critical to the company's success. The stakeholders have identified three key…|
|`pmp-b8-217`|pmp-mocks-1-10|single|predictive|3|As a predictive project enters the execution phase, a functional manager discovers that one of the materials to be used in the…|
|`pmp-b8-218`|pmp-mocks-1-10|single|predictive|2|A project is nearing completion when the project manager learns that a change in market regulations may impact the project.|
|`pmp-b8-219`|pmp-mocks-1-10|single|predictive|2|A country implements new labor laws with specific requirements for labor health and safety. The company's existing procedures…|
|`pmp-b8-220`|pmp-mocks-1-10|single|agile|3|A company is losing market share to disruptive competitors with new business models. A cross-functional agile team has been…|
|`pmp-b8-221`|pmp-mocks-1-10|single|predictive|2|A project manager is overseeing the construction of a new manufacturing facility when an economic downturn may suddenly threatens…|
|`pmp-b8-222`|pmp-mocks-1-10|single|predictive|2|In a large infrastructure project, the project manager is faced with the challenge of sudden regulatory changes that could…|
|`pmp-b8-223`|pmp-mocks-1-10|single|agile|2|During the rollout of a new e-commerce platform, an agile project manager observes a trend of customers gravitating towards a…|
|`pmp-b8-224`|pmp-mocks-1-10|single|predictive|2|During the execution phase of a large-scale environmental restoration project, the project manager encounters a debate among team…|
|`pmp-b8-225`|pmp-mocks-1-10|single|predictive|2|During the planning of a new development project, a recently enacted zoning law changes the permissible building heights within…|
|`pmp-b8-226`|pmp-mocks-1-10|single|predictive|1|You have been asked to help facilitate a senior planning meeting in your organisation. High level executives have made a…|
|`pmp-b8-227`|pmp-mocks-1-10|single|predictive|2|You are in the initiating stages of a project working for a car manufacturer. Their vehicles have been losing market share due to…|
|`pmp-b8-228`|pmp-mocks-1-10|single|predictive|2|You have been assigned to address the fast-moving competition that is rapidly taking market share from your company. Your…|
|`pmp-b8-229`|pmp-mocks-1-10|single|agile|2|You are a project manager on a project using Agile to deliver a change to a busy team in your organization. Recently there have…|

---

2992 items listed.
