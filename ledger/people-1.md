# Question ledger — people-1 · Shared purpose

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
