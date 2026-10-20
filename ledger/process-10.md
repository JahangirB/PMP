# Question ledger — process-10 · Closure

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
|`pmp-pr10-1`|export-batch-1|single|predictive|3|A project was undertaken to set up a new business function in the company. Several months after completion, the project manager…|
|`pmp-pr10-2`|export-batch-1|single|predictive|2|A junior project manager approaches a senior project manager and asks for assistance in initiating the final closeout process for…|
|`pmp-pr10-3`|export-batch-1|single|predictive|3|A project manager works at a construction company. The current project is a three year, multimillion-dollar initiative to rebuild…|
|`pmp-pr10-201`|export-batch-2|single|hybrid|3|A project is delivering incremental business value to a customer. The success criteria for the project is a 20% increase in…|
|`pmp-pr10-202`|export-batch-2|single|predictive|3|A project team has completed project deliverables, but the project manager would like to evaluate whether the project has…|
|`pmp-pr10-203`|export-batch-2|single|predictive|2|During a project governance assessment, and after expending 40% of the approved project budget, management approved the auditor's…|
|`pmp-pr10-204`|export-batch-2|single|predictive|2|A project to develop a new product is nearing its closing phase after 6 months of contributions from the project team and vendor.…|
|`pmp-pr10-205`|export-batch-2|single|hybrid|2|A project manager is involved in an e-commerce development project. The project has been completed successfully using the hybrid…|
|`pmp-pr10-206`|export-batch-2|single|predictive|2|A project manager needs to close a project phase.|
|`pmp-pr10-207`|export-batch-2|single|predictive|2|A project was cancelled during its initiation phase due to lack of adequate resources.|
|`pmp-pr10-208`|export-batch-2|single|predictive|3|Due to a recent incident, a dissatisfied stakeholder files a complaint about a project that has been closed.|
|`pmp-pr10-209`|export-batch-2|single|predictive|2|During project closure, the project manager finds it difficult to obtain successful project completion feedback. Some…|
|`pmp-pr10-210`|export-batch-2|single|predictive|2|A group of stakeholders at a financial institution are conducting a project closure review. Some of the stakeholders are…|
|`pmp-pr10-211`|export-batch-2|single|predictive|2|A large project involving functional groups in four countries is nearing completion. The project manager is meeting with the…|
|`pmp-pr10-212`|export-batch-2|single|predictive|2|After all deliverables have been completed, a project manager needs to formally close a project.|
|`pmp-pr10-213`|export-batch-2|single|predictive|3|A project manager is concerned that team members spend too much time assisting operations and maintenance staff with a recently…|
|`pmp-pr10-214`|export-batch-2|single|predictive|1|Your team have completed the work on your project and the customer has confirmed the formal acceptance of the product that has…|
|`pmp-pr10-215`|export-batch-2|single|predictive|2|You are nearing the end of a difficult project where the vendor providing the system to your company has taken longer than…|
|`pmp-pr10-216`|export-batch-2|single|predictive|2|During your project, your company was taken over by another, larger company, and the project management office and its policies…|
|`pmp-pr10-217`|export-batch-2|single|predictive|1|You are in the closing phase of your project, after having delivered a large change for employees in the HR department.|
|`pmp-pr10-218`|export-batch-2|single|predictive|1|You as a project manager are listing the things that must be done to close out the project.|
