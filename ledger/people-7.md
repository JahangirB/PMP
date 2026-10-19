# Question ledger — people-7 · Knowledge transfer

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
