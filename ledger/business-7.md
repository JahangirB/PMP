# Question ledger — business-7 · Organisational change

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
