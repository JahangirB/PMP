# Question ledger — Business Environment

804 questions across the 8 Business Environment tasks.

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
