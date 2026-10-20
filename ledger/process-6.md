# Question ledger — process-6 · Project finance

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
|`pmp-pr6-1`|export-batch-1|single|predictive|4|After the transition and completion of a large equipment installation project, the project manager receives a request from the…|
|`pmp-pr6-2`|export-batch-1|single|predictive|4|During the development phase of a construction project, key stakeholders approved a project budget. The team identified risks and…|
|`pmp-pr6-3`|export-batch-1|single|agile|5|An innovative project is leveraging artificial intelligence (AI) to identify chemical compounds for the development of new…|
|`pmp-pr6-4`|export-batch-1|single|predictive|2|A project manager is appointed to design and develop a strategy for a government organization. The project manager recognizes…|
|`pmp-pr6-5`|export-batch-1|single|predictive|3|A team is working on a new technology deployment project. This product has never been implemented before so there is no past data…|
|`pmp-pr6-6`|export-batch-1|single|predictive|4|A project manager has been assigned to a new, multimillion-dollar project that is similar to a project the company executed three…|
|`pmp-pr6-7`|export-batch-1|single|predictive|4|During a progress meeting, the project team learns that an identified risk materialized. The project manager decides to execute…|
|`pmp-pr6-8`|export-batch-1|single|predictive|3|A construction company is implementing a new strategic project with a fixed budget. While monitoring the project, it was…|
|`pmp-pr6-9`|export-batch-1|single|predictive|3|At project initiation, a quote for the required services was requested from a vendor, and the work was estimated at US$100,000.…|
|`pmp-pr6-201`|export-batch-2|single|hybrid|4|A team is working on a new product development project using a hybrid approach. After the fourth sprint, they learn that the…|
|`pmp-pr6-202`|export-batch-2|single|predictive|4|A project manager is developing a project budget. After estimating the cost of each activity, the manager adds a contingency…|
|`pmp-pr6-203`|export-batch-2|single|agile|3|A team is working on a new project and the sponsor refuses to approve a budget increase needed for the current iteration.|
|`pmp-pr6-204`|export-batch-2|single|hybrid|2|A project manager is managing a hybrid project with a fixed time and fixed price agreement. The project manager is having…|
|`pmp-pr6-205`|export-batch-2|single|agile|2|A project manager is managing an agile project. In order to make sure there is enough funding available for the project, the…|
|`pmp-pr6-206`|export-batch-2|single|predictive|2|A new project has high uncertainty in the cost required for completion.|
|`pmp-pr6-207`|export-batch-2|single|predictive|3|A project is set to install three MRI units in a hospital; one unit has passed acceptance testing and two are under construction.…|
|`pmp-pr6-208`|export-batch-2|single|predictive|1|A project manager is assigned to join a mega industrial project already underway. The latest performance indices are CPI=0.42 and…|
|`pmp-pr6-209`|export-batch-2|single|agile|3|During an iteration the team realizes that the project will overrun the budget. This is due to the support resources being…|
|`pmp-pr6-210`|export-batch-2|single|agile|3|A project manager secures project approval with a constrained budget due to fiscal limitations. The sponsor emphasizes no…|
|`pmp-pr6-211`|export-batch-2|single|predictive|2|An approved change request adds an incentive to a lump sum contract if the contractor is able to maintain the current baseline…|
|`pmp-pr6-212`|export-batch-2|single|predictive|3|A project manager discovers that recent legislative updates necessitate additional security enhancements to the project. Although…|
|`pmp-pr6-213`|export-batch-2|single|agile|3|A project manager needs to prioritize multiple projects within a portfolio and requires accurate budget estimates. The process…|
|`pmp-pr6-214`|export-batch-2|single|predictive|2|A project manager is overseeing the construction of a new office building. The project is running over budget due to the high…|
|`pmp-pr6-215`|export-batch-2|single|predictive|2|In a construction project, the project manager decides to implement Value Engineering (VE) to enhance the project's value. The…|
|`pmp-pr6-216`|export-batch-2|single|predictive|2|A project manager is conducting a Value Engineering (VE) workshop to identify ways to improve a project's value. During the…|
|`pmp-pr6-217`|export-batch-2|single|predictive|2|During the construction of a new office building, the project manager learns that unforeseen geological issues will significantly…|
|`pmp-pr6-218`|export-batch-2|single|predictive|2|Faced with a constrained budget and a tight deadline for launching a new product development project, the project manager…|
|`pmp-pr6-219`|export-batch-2|single|predictive|2|You are working on a business case where you know that the industry is undergoing a significant change at the moment, and there…|
|`pmp-pr6-220`|export-batch-2|single|predictive|3|When reviewing the current progress with your project sponsor, they notice that the system solution is missing a critical piece…|
|`pmp-pr6-221`|export-batch-2|single|predictive|2|The project has faced serious impacts in getting materials, with supply chains being disrupted and a global pandemic, and is…|
|`pmp-pr6-222`|export-batch-2|single|predictive|2|You are planning a project where supply chains have been impacted, and the requirements are complex and intertwined with other…|
|`pmp-pr6-223`|export-batch-2|single|predictive|2|You are working as a Project Manager and your team is about to deliver the completed system to the customer. During the final…|
|`pmp-pr6-224`|export-batch-2|single|predictive|3|You are the project manager on a waterfall project, and you have planned the requirements, scope and schedule with your team. You…|
|`pmp-pr6-225`|export-batch-2|single|predictive|2|The project you are managing has defined the scope and created the Scope Baseline with the Scope Statement, WBS and WBS…|
|`pmp-pr6-226`|export-batch-2|single|predictive|2|Multiple unforeseen events have happened that have set your project behind schedule, and in trying to bring it back on track you…|
|`pmp-pr6-227`|export-batch-2|single|predictive|2|Your project plan includes an estimate of how long the project will take (via a project schedule), how much it will cost and the…|
|`pmp-pr6-228`|export-batch-2|single|predictive|2|You are planning your project and finalizing the project costs. You have broken down the scope into work packages and estimated…|
|`pmp-pr6-229`|export-batch-2|single|predictive|2|You are working as a project manager and created the cost estimate to bid on a government contract, where the scope was set by…|
