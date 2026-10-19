# Question ledger — people-5 · Expectation alignment

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
