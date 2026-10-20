# Question ledger — business-4 · Impediments and issues

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
|`pmp-b4-1`|export-batch-1|single|predictive|4|A project manager was recently assigned to an information technology project. The project is in the initiation stage, and the…|
|`pmp-b4-2`|export-batch-1|single|predictive|3|A project manager is managing an upgrade from an older system to a new version. Halfway through the upgrade, the older system…|
|`pmp-b4-3`|export-batch-1|single|predictive|3|A project manager has been informed that the first of several major deliveries from another country will be delayed. These…|
|`pmp-b4-4`|export-batch-1|single|agile|3|During a retrospective meeting, project team members complain that open issues that were not fixed during the iteration are…|
|`pmp-b4-5`|export-batch-1|single|agile|3|A project team realizes that the product's architecture will not support the growing number of users.|
|`pmp-b4-6`|export-batch-1|single|agile|3|An agile project manager is overseeing a Kanban project and wants to assess task completion roadblocks for an upcoming major…|
|`pmp-b4-7`|export-batch-1|single|predictive|3|Project team members inform their project manager of a challenge they are facing. This challenge will likely impact project…|
|`pmp-b4-8`|export-batch-1|single|predictive|3|During the testing phase of a project, a team member finds that some requirements were not met. If the project now adds work to…|
|`pmp-b4-9`|export-batch-1|single|agile|3|One of the project vendors is late in providing a related product solution, which is affecting the release due dates.|
|`pmp-b4-201`|export-batch-2|single|agile|2|While executing a critical iterative phase of a project, a problem previously identified as a risk just became a confirmed issue,…|
|`pmp-b4-202`|export-batch-2|single|predictive|3|A technology project for a multinational bank is in the testing stage. The project manager learns that a security breach has…|
|`pmp-b4-203`|export-batch-2|single|predictive|1|A project manager notices that a project is not running well and deliverables are falling behind.|
|`pmp-b4-204`|export-batch-2|single|agile|2|A project manager is managing an agile project which has many risks identified in the risk register. The current sprint deals…|
|`pmp-b4-205`|export-batch-2|single|agile|2|An organization decides to implement the Scrum delivery framework. The project manager is using an impediment board with the…|
|`pmp-b4-206`|export-batch-2|single|predictive|3|A project manager is managing the build of an off-shore data center. In addition, the project manager is the custodian of the…|
|`pmp-b4-207`|export-batch-2|single|predictive|2|Midway through a project, a project manager learns that the project management planning application is failing to record updates…|
|`pmp-b4-208`|export-batch-2|single|agile|2|During a daily standup, a stakeholder unexpectedly joins the meeting and criticizes the performance of one of the team members.…|
|`pmp-b4-209`|export-batch-2|single|agile|3|A project manager is part of a project that has a scrum team. The team members are unfamiliar with the distinction between risks…|
|`pmp-b4-210`|export-batch-2|single|predictive|2|During project initiation, the project manager learns that a key stakeholder will leave the project for a month to consult on…|
|`pmp-b4-211`|export-batch-2|single|predictive|3|A project manager is leading a complex system improvement project. During the integration phase, an upgrade to one component…|
|`pmp-b4-212`|export-batch-2|single|predictive|3|A looming fuel price increase was identified as a risk during the planning stages of a large construction project. The identified…|
|`pmp-b4-213`|export-batch-2|single|predictive|2|A manufacturing project is in the implementation phase. During a status meeting, a team member tells the project manager that a…|
|`pmp-b4-214`|export-batch-2|single|agile|3|A product owner presented the backlog to the team, and the team raised concerns over implementing some of the features due to…|
|`pmp-b4-215`|export-batch-2|multi|agile|3|A customer is upset because a critical issue for a project deliverable has not been resolved by the agile team, even though…|
|`pmp-b4-216`|export-batch-2|single|agile|1|At daily standup meetings, several team members complain that they cannot concentrate due to a noisy work environment.|
|`pmp-b4-217`|export-batch-2|multi|predictive|2|During a project review meeting, an end user reports that the personal computers supplied for testing are experiencing…|
|`pmp-b4-218`|export-batch-2|single|predictive|1|A project team in a digital marketing project discovers that their key design software license has expired, halting the team work…|
|`pmp-b4-219`|export-batch-2|single|predictive|2|During the execution phase of a software development project, the team encounters a technical challenge that jeopardizes the…|
|`pmp-b4-220`|export-batch-2|single|predictive|2|During a large-scale event planning project, the project manager notices a growing list of logistical challenges reported by…|
|`pmp-b4-221`|export-batch-2|single|hybrid|3|A project manager notices that the integration process is more cumbersome than anticipated, significantly slowing down the…|
|`pmp-b4-222`|export-batch-2|single|agile|2|A team member has been reporting no progress on a crucial task for several days during the daily stand-up meetings, causing a…|
|`pmp-b4-223`|export-batch-2|single|predictive|2|During a critical software implementation project, the project manager identifies a significant slowdown in the integration…|
|`pmp-b4-224`|export-batch-2|single|agile|2|You are working on an agile project as a scrum master, where the functional manager has worked with some people in your…|
|`pmp-b4-225`|export-batch-2|single|hybrid|1|You are working on a hybrid project to replace an aging customer system that has just had a feature upgrade release. The senior…|
|`pmp-b4-226`|export-batch-2|single|agile|1|You are working through the iteration with your Agile team, and are having trouble completing one of the story cards in the…|
|`pmp-b4-227`|export-batch-2|single|predictive|2|Your project team have been complaining about poor stakeholder engagement in a certain part of the organisation for some time. It…|
|`pmp-b4-228`|export-batch-2|single|predictive|2|You are working on a very high risk infrastructure project that is critical to your organisation. The team are experienced in…|
|`pmp-b4-229`|export-batch-2|single|agile|1|You are running the daily stand-up as an Agile project lead and neutral facilitator for the team. Work seems to be being…|
|`pmp-b4-230`|export-batch-2|single|predictive|1|You are working as a Project Manager and one of the stakeholders of the project raises a problem with the product requirements,…|
|`pmp-b4-231`|export-batch-2|single|agile|2|Your project team are gathering solution ideas for a particular customer requirement, which is prioritised to be worked on next.…|
