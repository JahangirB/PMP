# Question ledger — process-5 · Procurement

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
|`pmp-pr5-1`|export-batch-1|single|predictive|3|Production of a specific machine in a factory was contracted through a foreign supplier. Prior to delivery of the final two…|
|`pmp-pr5-2`|export-batch-1|single|predictive|3|A company is expanding, and the project manager is contemplating whether to hire new resources or outsource resources from an…|
|`pmp-pr5-3`|export-batch-1|single|agile|3|An agile project to implement a new automation process in a factory is underway. The project manager needs a vendor to help with…|
|`pmp-pr5-4`|export-batch-1|single|predictive|3|A two-year project was awarded to a company six months ago, but the procurement process has not yet been initiated.|
|`pmp-pr5-5`|export-batch-1|single|predictive|4|A project manager is meeting with a critical third-party supplier that provides discounted services. During the meeting, the…|
|`pmp-pr5-6`|export-batch-1|single|predictive|3|A member of the project team who was hired for six months to complete a specific task has decided to leave the project in the…|
|`pmp-pr5-7`|export-batch-1|single|predictive|4|A vendor for a large and complex project has been performing more work than what was originally anticipated in the time and…|
|`pmp-pr5-8`|export-batch-1|single|predictive|3|The project manager is responsible for a major project that involves working with many vendors. A key project concern is related…|
|`pmp-pr5-9`|export-batch-1|single|predictive|4|During project closure, the project sponsor requests that a deliverable, developed by a contractor, be transferred to a nonprofit…|
|`pmp-pr5-10`|export-batch-1|single|predictive|4|A complex, multimillion-dollar, multi-year product development project requiring unique knowledge is 70% complete. Contract…|
|`pmp-pr5-11`|export-batch-1|single|predictive|2|A project manager walked into a project office and found a team member and a supplier representative engaged in a disagreement…|
|`pmp-pr5-12`|export-batch-1|single|predictive|3|In order to reduce procurement time during a bidding process, a project manager decided to contract the additional work to a…|
|`pmp-pr5-13`|export-batch-1|single|predictive|4|During the execution of repair work, the root cause of the damage was discovered to be failures that occurred during the…|
|`pmp-pr5-14`|export-batch-1|single|predictive|4|During the testing phase of a technical project, the project manager discovers some issues with the final product and…|
|`pmp-pr5-15`|export-batch-1|single|predictive|3|A project manager is overseeing a procurement project for a company that has a dedicated procurement management team that handles…|
|`pmp-pr5-16`|export-batch-1|single|predictive|3|The development of a final project schedule is delayed due to the inability to negotiate an agreement with one of the vendors…|
|`pmp-pr5-17`|export-batch-1|single|predictive|3|A project manager has received quotes from two vendors for the same parts. Vendor A has informed the project manager that they…|
|`pmp-pr5-201`|export-batch-2|single|predictive|3|A new project manager has taken over an ongoing project. Progress reports show that the project is within the allocated budget…|
|`pmp-pr5-202`|export-batch-2|single|hybrid|4|The service provider and their client agree on about 80% of the scope of a project contract they are working on for the delivery…|
|`pmp-pr5-203`|export-batch-2|single|predictive|3|The construction of a building is well-defined through plans and written specifications. The service is to be delivered in two…|
|`pmp-pr5-204`|export-batch-2|single|predictive|4|A project manager has just taken over an ongoing project. The previous project manager resigned from the organization. During a…|
|`pmp-pr5-205`|export-batch-2|single|predictive|3|A project manager for a delivery project has engaged a vendor to provide testing services.|
|`pmp-pr5-206`|export-batch-2|single|predictive|2|Halfway through a year-long project, a hardware supplier sends a project manager notice of an intent to increase prices due to…|
|`pmp-pr5-207`|export-batch-2|single|predictive|2|A project is in the testing phase, and the quality control team informs the project manager that the quality of the deliverables…|
|`pmp-pr5-208`|export-batch-2|single|predictive|2|Project completion will be delayed because an essential component will not be delivered on time.|
|`pmp-pr5-209`|export-batch-2|single|predictive|2|A project has components that are outsourced and the project manager is in the control phase of the procurement process.|
|`pmp-pr5-210`|export-batch-2|single|predictive|2|A project has begun to develop a new software application that will be used by the company's sales team. The project is expected…|
|`pmp-pr5-211`|export-batch-2|single|predictive|3|A project integrates with several systems developed by various vendors and one of these vendors has gone out of business.|
|`pmp-pr5-212`|export-batch-2|single|predictive|3|A project manager is managing a project with a very aggressive delivery schedule and a firm completion date. The project requires…|
|`pmp-pr5-213`|export-batch-2|single|predictive|3|A project manager oversees a complex project in which a vendor is responsible for delivering several essential components. The…|
|`pmp-pr5-214`|export-batch-2|single|predictive|2|A project manager learns that the project team is involved in an ongoing conflict with an overseas supplier team. The conflict is…|
|`pmp-pr5-215`|export-batch-2|multi|predictive|3|A project steering group has made a decision to change the vendor that will deliver a component of a project. (Choose 2).|
|`pmp-pr5-216`|export-batch-2|single|predictive|3|A project manager is on a project in collaboration with a vendor. The vendor responded to all the Request for Proposal (RFP)…|
|`pmp-pr5-217`|export-batch-2|single|predictive|2|A supplier has been awarded a firm fixed price (FFP) contract to provide various materials for a construction project. The…|
|`pmp-pr5-218`|export-batch-2|single|predictive|3|The production of a key component in a construction project is outsourced to a third-party vendor. The vendor informs the project…|
|`pmp-pr5-219`|export-batch-2|single|predictive|2|A project manager needs to contract with a provider for a large government project. Project guidelines mandate that all potential…|
|`pmp-pr5-220`|export-batch-2|multi|predictive|3|In two months, there will be a delivery of project machines from a factory. The project team is in the process of performing a…|
|`pmp-pr5-221`|export-batch-2|single|predictive|3|A project manager is leading a project where a vendor is engaged. The project team has a dependency on the vendor to complete…|
|`pmp-pr5-222`|export-batch-2|single|predictive|2|After consultation with executive stakeholders, it was decided that the project manager would replace a vendor while the project…|
|`pmp-pr5-223`|export-batch-2|single|predictive|3|A project vendor has informed a project manager that an outsourced project deliverable will be available 6 months later than the…|
|`pmp-pr5-224`|export-batch-2|single|hybrid|3|A project manager is leading a project with a scope partially delivered by a vendor. The vendor's processes and tools differ from…|
|`pmp-pr5-225`|export-batch-2|single|predictive|3|Due to increased price volatility, a vendor asks the project manager of a large development project to make a price change that…|
|`pmp-pr5-226`|export-batch-2|single|predictive|2|A team receives outsourced deliverables and determines that they do not meet the agreed-upon acceptance criteria.|
|`pmp-pr5-227`|export-batch-2|single|predictive|1|A project manager is managing a project involving a few vendors. The project sponsor asks how the project will acquire goods and…|
|`pmp-pr5-228`|export-batch-2|single|predictive|2|A project manager needs to hire suppliers for an international project that has a performance period of several years. The…|
|`pmp-pr5-229`|export-batch-2|single|predictive|2|During project execution, the team finds that changes must be made to a component sourced from an outside supplier. The component…|
|`pmp-pr5-230`|export-batch-2|single|predictive|3|A project manager has been awarded a firm-fixed-price (FFP) contract to develop a new mobile app for a client. The contract scope…|
|`pmp-pr5-231`|export-batch-2|single|predictive|3|The supplier of a tool for a manufacturing project informs the project manager that delivery of the tool will be late. The…|
|`pmp-pr5-232`|export-batch-2|single|predictive|3|A contractor is late with a critical project deliverable due to resource challenges. There is a 22% buffer in the budget for cost…|
|`pmp-pr5-233`|export-batch-2|multi|hybrid|4|The government of a country intends to construct a multibillion-dollar railway across three major cities and hires a contractor…|
|`pmp-pr5-234`|export-batch-2|single|predictive|2|A member of a development project team informs the project manager that the operating system is not going to be installed on time…|
|`pmp-pr5-235`|export-batch-2|single|predictive|3|For the implementation of a sophisticated security software across multiple departments, the selected external provider asserts…|
|`pmp-pr5-236`|export-batch-2|single|predictive|3|For an upcoming critical initiative, a project manager must engage a vendor notorious for not meeting deadlines, yet uniquely…|
|`pmp-pr5-237`|export-batch-2|single|predictive|2|At the beginning of a green building project, the project manager is tasked by the lead architect to engage a key supplier known…|
|`pmp-pr5-238`|export-batch-2|single|predictive|2|You are working on a high value construction project to include smart-home components within multi-million dollar buildings.…|
|`pmp-pr5-239`|export-batch-2|single|predictive|2|A missing item has been uncovered in the project scope that your project team cannot deliver. The item is not complex and seems…|
|`pmp-pr5-240`|export-batch-2|single|hybrid|3|You are working on a project where part of the work is planned carefully upfront, and another part is subject to change. You…|
|`pmp-pr5-241`|export-batch-2|single|predictive|2|You have completed a make-or-buy analysis in your project and found that part of your deliverable is best created by an external…|
|`pmp-pr5-242`|export-batch-2|single|agile|2|Your agile project needs a third party vendor to help complete the work. The product owner has multiple features on their product…|
|`pmp-pr5-243`|export-batch-2|single|predictive|2|You have completed a make-or-buy analysis and you agree to hire a third-party vendor to complete part of your product. You create…|
|`pmp-pr5-244`|export-batch-2|single|predictive|2|You are the project manager on a high profile software project that involves automation technology not used before in your…|
|`pmp-pr5-245`|export-batch-2|single|predictive|2|You are working on a project charter for the initiating stage of a new project that will deliver a new data system. You approach…|
|`pmp-pr5-246`|export-batch-2|single|predictive|2|You have planned, executed and monitored a project to completion. The customer has just received the finished product and you…|
|`pmp-pr5-247`|export-batch-2|single|predictive|2|You are working on a complex, high-value project to replace the telecommunications system across your organisation. You have…|
|`pmp-pr5-248`|export-batch-2|single|predictive|2|Part of your project work needs to be outsourced to a supplier. You are asked to ensure that cost for the outsourced portion of…|
|`pmp-pr5-249`|export-batch-2|single|agile|2|You are a project manager on an Agile project that requires an external third party to develop software for one of your features.…|
|`pmp-pr5-250`|export-batch-2|single|predictive|2|You are working on a project where a vendor is performing a large amount of the work. Lately they have not been performing well,…|
|`pmp-pr5-251`|export-batch-2|single|predictive|2|A recent news article spread a rumour about a supplier who is providing services for the project you are working on. They are…|
|`pmp-pr5-252`|export-batch-2|single|predictive|1|You are working on a project and it has become apparent that part of the product your company was creating they won't be able to…|
