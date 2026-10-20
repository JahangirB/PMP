# Question ledger — business-1 · Governance

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
|`pmp-b1-1`|export-batch-1|single|hybrid|3|A project manager in a quickly growing hybrid organization is becoming increasingly frustrated at the rapid amount of change in…|
|`pmp-b1-2`|export-batch-1|single|agile|3|A project manager has been assigned to an existing agile team and is preparing for an upcoming project. The last project that the…|
|`pmp-b1-3`|export-batch-1|single|agile|4|An organization is trying to adopt agile approaches to execute projects for their customers. During project planning, the product…|
|`pmp-b1-4`|export-batch-1|single|predictive|4|A project can gain downstream marketing benefits if additional equipment is included in the project scope. However, this…|
|`pmp-b1-201`|export-batch-2|single|hybrid|3|As part of a temporary assignment, a project manager is leading a business project. During a daily status meeting, it was…|
|`pmp-b1-202`|export-batch-2|single|predictive|1|After completing the project charter, the project manager sends it to the project stakeholders.|
|`pmp-b1-203`|export-batch-2|single|predictive|3|During a kick-off meeting, the sponsor informs the project team that they can not attend any meetings for the first half of the…|
|`pmp-b1-204`|export-batch-2|single|predictive|4|After several interviews with key stakeholders and checking the project charter, a project manager found that the current project…|
|`pmp-b1-205`|export-batch-2|single|predictive|2|A project manager is working on the development of a business case to secure funding and resources.|
|`pmp-b1-206`|export-batch-2|single|predictive|3|A project manager is tracking an infrastructure project that is dependent on a service provider. The service provider informed…|
|`pmp-b1-207`|export-batch-2|single|predictive|1|A project team member assigned to a new project will be required to report on all project tasks progress to the project manager…|
|`pmp-b1-208`|export-batch-2|single|predictive|2|A project manager is leading a project to develop a new system for a government agency using a predictive approach. The project's…|
|`pmp-b1-209`|export-batch-2|single|hybrid|2|A project manager is on a hybrid project. The project manager meets with the sponsor to get the project milestones. The sponsor…|
|`pmp-b1-210`|export-batch-2|single|predictive|4|A project manager is assigned to join a several year project mid-way. The project was stopped years ago due to organizational…|
|`pmp-b1-211`|export-batch-2|single|predictive|3|A megaproject had reached 10% completion when there was a sudden change of company ownership. After being put on hold for a few…|
|`pmp-b1-212`|export-batch-2|single|predictive|3|Leading a significant initiative to overhaul a corporation's IT infrastructure, the project manager has already navigated through…|
|`pmp-b1-213`|export-batch-2|single|predictive|2|During one of your working group meetings, a stakeholder raises the concern that the product you are working on will be…|
|`pmp-b1-214`|export-batch-2|single|predictive|2|You are working directly with the project sponsor, in the initial phase of a brand new project. The project sponsor asks for your…|
|`pmp-b1-215`|export-batch-2|single|agile|2|Your organisation is starting to move to an agile way of work and they are setting up a Value Delivery Office (VDO) to complement…|
|`pmp-b1-216`|export-batch-2|single|predictive|2|Your project team have planned the requirements, scope, schedule and cost for constructing a new building in your organisation.…|
|`pmp-b1-217`|export-batch-2|single|predictive|1|You are new to a project and are approached by the functional manager. No one has been able to tell them exactly what the project…|
|`pmp-b1-218`|export-batch-2|single|predictive|2|You are working as a project manager in an organisation with a new Project Management Office. They advise all project managers to…|
|`pmp-b1-219`|export-batch-2|single|predictive|2|You have just joined an organisation, and are about to be allocated to an existing project, delivering a new Human Resources…|
|`pmp-b1-220`|export-batch-2|single|predictive|2|You have just started work in a project that has been in development for the past six months. You notice it has been through a…|
|`pmp-b1-221`|export-batch-2|single|predictive|1|You are a project manager brought on during the very early phase of the project. The project sponsor has asked you to create a…|
|`pmp-b1-222`|export-batch-2|single|predictive|1|You have recently become a project manager for a large organisation and you are not sure what to expect. Your colleague tells you…|
|`pmp-b1-223`|export-batch-2|single|hybrid|2|You are a Project Manager who has been tasked with initiating a new project for your area. You review the current organizational…|
|`pmp-b1-224`|export-batch-2|single|predictive|1|You are working as a project manager, asking for proposals from a Third Parties to complete the work. Your brother works for one…|
|`pmp-b1-225`|export-batch-2|single|predictive|2|You are working on a global project in other country, where it is customary to honour the project manager of a successful project…|
|`pmp-b1-226`|export-batch-2|single|predictive|1|You have been asked to prepare a document to gain the project sponsor's sign off to access their function's resources, that…|
