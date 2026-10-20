# Question ledger — business-6 · Continuous improvement

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
|`pmp-b6-1`|export-batch-1|single|predictive|2|During a project closing review meeting, the project manager notes that a software development task took longer to complete than…|
|`pmp-b6-2`|export-batch-1|single|agile|2|During project closure, the team considers that the final sprint retrospective is not necessary because there are no remaining…|
|`pmp-b6-3`|export-batch-1|single|agile|4|An approved project charter stated that an agile approach must be used. During a lessons learned session, the client indicates…|
|`pmp-b6-201`|export-batch-2|single|agile|3|A project manager is managing a project for a successful startup. The startup has shown exponential growth with multiple benefits…|
|`pmp-b6-202`|export-batch-2|single|agile|2|In the middle of an iteration for the development of a project deliverable, a team member suggests a new method of doing the…|
|`pmp-b6-203`|export-batch-2|single|agile|2|An agile team consistently delivers features on time but recently identified a significant architecture gap leading to rework.|
|`pmp-b6-204`|export-batch-2|single|predictive|2|A project is currently in progress. The project manager has been notified of a new process that may be helpful if it is…|
|`pmp-b6-205`|export-batch-2|single|agile|3|A project team's performance has declined for the last three iterations. Some of the team members are disappointed, and raise…|
|`pmp-b6-206`|export-batch-2|single|agile|2|A project team has recently finished a sprint for developing two features for a fleet management system. Although the team…|
|`pmp-b6-207`|export-batch-2|single|agile|2|During a retrospective meeting, team members show disengagement, stating that they are repeating the same ideas over and over.|
|`pmp-b6-208`|export-batch-2|single|agile|2|A newly formed team has become accustomed to agile practices. The project lead has noticed that while they are performing…|
|`pmp-b6-209`|export-batch-2|single|agile|2|An agile project is running its fifth iteration out of 10. After the last retrospective, the team members realize that they had…|
|`pmp-b6-210`|export-batch-2|single|agile|2|A senior manager informs a project manager that the project is not performing as well as other projects.|
|`pmp-b6-211`|export-batch-2|single|agile|2|At the end of an iteration, a team member tells the project manager that a planned task is unfinished because of an issue that…|
|`pmp-b6-212`|export-batch-2|single|agile|2|A product team is using a prototyping approach to deliver a multiyear business initiative. The project manager notices that a few…|
|`pmp-b6-213`|export-batch-2|single|agile|2|In an agile environment, a project leader should always consider a time for open and free discussions between team members on how…|
|`pmp-b6-214`|export-batch-2|single|hybrid|3|A company has been following a predictive approach and always waits for the preceding phase to be completed before evaluating the…|
|`pmp-b6-215`|export-batch-2|single|agile|2|A project manager is running an agile project that is half completed. During the most recent sprint retrospective, the team…|
|`pmp-b6-216`|export-batch-2|single|agile|3|A project manager typically conducts retrospective sessions at the end of a project. However, the sponsor believes that…|
|`pmp-b6-217`|export-batch-2|multi|hybrid|3|A team member in a hybrid organization informs the project manager of a new way to execute an activity with a shorter duration.…|
|`pmp-b6-218`|export-batch-2|single|agile|2|An agile project team is struggling to keep track of the changes being applied to the product, because its process is cumbersome…|
|`pmp-b6-219`|export-batch-2|single|agile|2|A company is transitioning from a predictive to an agile project management approach. The project manager has noticed that team…|
|`pmp-b6-220`|export-batch-2|single|agile|1|After a few iterations, the project manager feels that even though the team is completing the work as planned, the team is not…|
|`pmp-b6-221`|export-batch-2|single|agile|3|The latest software build for a virtual reality headset release did NOT include urgent change requests already implemented by the…|
|`pmp-b6-222`|export-batch-2|single|agile|2|A project manager convenes an emergency session with the software development team to address significant bugs that are delaying…|
|`pmp-b6-223`|export-batch-2|single|agile|2|During an iterative development project for a new software tool, a developer who was recently added to the team suggests…|
|`pmp-b6-224`|export-batch-2|single|hybrid|2|Your project is starting to go off track due to a large amount of uncertainty in the requirements and resources. At a significant…|
|`pmp-b6-225`|export-batch-2|single|hybrid|2|You have recently taken over on a project that is behind schedule, and slightly over budget. You raise an issue at the steering…|
|`pmp-b6-226`|export-batch-2|single|hybrid|3|Your Project Management Office (PMO) is optimising all the projects within their portfolio. They ask for your advice in tracking…|
|`pmp-b6-227`|export-batch-2|single|agile|2|You have been working as a project manager in an organisation for some time, where an agile- centric delivery approach and way of…|
|`pmp-b6-228`|export-batch-2|single|agile|2|You have been working on a project to deliver a new customer data system, and have noticed that the work is starting to slow…|
|`pmp-b6-229`|export-batch-2|single|agile|2|You have been assigned to take over an existing project as the previous project manager leaves for another opportunity. In…|
|`pmp-b6-230`|export-batch-2|single|agile|2|You are working as a Product Owner. While your sales and revenue have been increasing over the past eight months, the overall…|
|`pmp-b6-231`|export-batch-2|single|agile|1|You are an Agile team leader and talking through the process for your first retrospective. The team is curious as to what they…|
|`pmp-b6-232`|export-batch-2|single|agile|1|You are new to your Agile project, and the team are working through the forming stages.|
|`pmp-b6-233`|export-batch-2|single|agile|1|You are finishing up the first iteration on your Agile project and have set a retrospective meeting for the whole team. One…|
|`pmp-b6-234`|export-batch-2|single|agile|1|The 12th principle of Agile is "At regular intervals, the team reflects on how to become more effective, then tunes and adjusts…|
|`pmp-b6-235`|export-batch-2|single|agile|1|Retrospectives are typically held at the end of an iteration, but an Agile team can also hold retrospectives at any other time.|
