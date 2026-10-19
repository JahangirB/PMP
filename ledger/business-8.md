# Question ledger — business-8 · External environment

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
