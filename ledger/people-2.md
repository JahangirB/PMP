# Question ledger — people-2 · Conflict

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

### people-2 · Conflict — 116 items

*Trace disagreement to its source and work it through to an agreed resolution.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p2-001`|core|matching|agile|3|Prompts: A production outage is live, two enginee… / Two team members disagree about a coding… / A designer and a developer disagree abou…|
|`pmp-p2-002`|core|single|predictive|3|In a matrix organisation, a functional manager has twice reassigned your lead analyst mid-sprint without telling you, and the…|
|`pmp-p2-003`|core|single|hybrid|4|Two technical leads on your programme have been arguing for a month about whether to build a shared service or let each stream…|
|`pmp-p2-004`|core|single|predictive|4|Two department heads both need the same test rig in the final fortnight before your go-live. Each has emailed you separately…|
|`pmp-p2-005`|core|single|agile|2|Two developers on your team have started leaving blunt comments on each other's pull requests. Nothing is abusive, but the tone…|
|`pmp-p2-006`|core|single|predictive|5|Your client's project manager insists that a data migration capability is within the fixed-price scope; you read the…|
|`pmp-p2-007`|core|matching|hybrid|3|Prompts: Two teams each believe the other is resp… / A business analyst and a developer disag… / Two senior engineers who have worked tog…|
|`pmp-p2-008`|core|single|predictive|3|A senior analyst allocated to your project half-time keeps missing your deadlines. Her functional manager tells you she is fully…|
|`pmp-p2-009`|core|pulldown|agile|3|Prompts: Two developers disagree about a library… / A tester has twice made remarks about a… / Two team members disagree about who shou…|
|`pmp-p2-010`|core|single|hybrid|4|Two workstream leads on your distributed programme have been in unresolved disagreement for months about interface ownership. In…|
|`pmp-p2-011`|core|single|agile|2|A developer tells you that a business analyst on the team is impossible to work with, changes her mind constantly, and is making…|
|`pmp-p2-012`|core|single|predictive|4|Your integration vendor and your internal infrastructure team each insist the other is responsible for a firewall configuration…|
|`pmp-p2-013`|core|single|agile|2|In yesterday's retrospective, two developers disagreed sharply about whether the team should keep pairing on complex work. The…|
|`pmp-p2-014`|core|single|predictive|4|Six weeks before a regulatory milestone, a senior engineer submits a formal grievance to human resources about a colleague on…|
|`pmp-p2-061`|core|graphic|hybrid|3|Your two most senior engineers disagree about how the interface between the billing and scheduling platforms should be…|
|`pmp-case2-003`|case-02|single|agile|3|Ewan and Lior, the two senior developers, disagree sharply about code review standards, and the tension has started to show in…|
|`pmp-case4-006`|case-04|single|predictive|3|The curators say the contractor's staff ignore the dust and vibration rules on site. The contractor's site manager says the…|
|`pmp-p2-101`|people-2|single|predictive|3|On a hospital extension, the main contractor's site manager and the mechanical and electrical subcontractor's foreman have argued…|
|`pmp-p2-102`|people-2|single|agile|1|During the daily stand-up of a mobile banking team, two developers start debating whether to adopt a new state-management…|
|`pmp-p2-103`|people-2|single|hybrid|5|Your firm is delivering a telemetry upgrade for a water company. The client's records manager, who owns its document standards,…|
|`pmp-p2-104`|people-2|single|predictive|4|On a line-automation project at a food plant near Lyon, the production planning lead and the quality lead clash at every weekly…|
|`pmp-p2-105`|people-2|single|agile|3|Your payments team is split between Leeds and Pune. Pune developers have started posting terse comments and missing refinement…|
|`pmp-p2-106`|people-2|single|hybrid|3|On a council housing-repairs system rollout, the test lead and the business analyst are in a running dispute about whether failed…|
|`pmp-p2-107`|people-2|single|predictive|3|On a claims-reserving project for a motor insurer, two actuarial analysts present reserve forecasts for the same portfolio that…|
|`pmp-p2-108`|people-2|single|predictive|2|On a substation replacement project for an electricity network operator, an email thread between the asset manager and your…|
|`pmp-p2-109`|people-2|multi|agile|3|On a retail e-commerce team, retrospectives have been short and positive for four sprints. Yet you have heard in corridors that…|
|`pmp-p2-110`|people-2|multi|agile|3|A public-sector digital team has been formed by merging four in-house developers with four from a contracted supplier. In the…|
|`pmp-p2-111`|people-2|matching|predictive|2|Prompts: The airport's operations lead and your d… / The conveyor supplier and your test engi… / The installation supervisor says the nig…|
|`pmp-p2-112`|people-2|matching|hybrid|3|Prompts: Two software teams disagree about which… / A developer and a tester on one software… / The mechanical contractor's supervisor a…|
|`pmp-p2-113`|people-2|pulldown|agile|3|Prompts: The team keeps reopening its decision on… / In refinement, two senior developers arg… / The team has argued about a level-loadin…|
|`pmp-p2-114`|people-2|fillblank|predictive|4|On a museum fit-out in Rotterdam, the joinery subcontractor claims €90,000 for work it says was added after award, and has…|
|`pmp-p2-115`|people-2|hotspot|hybrid|2|On a university research-data platform, the data architect and the data engineering lead have clashed three times this month,…|
|`pmp-p2-116`|people-2|single|predictive|4|On a packaging-line upgrade for a pharmaceutical manufacturer, your validation lead and the client's engineering lead disagreed…|
|`pmp-p2-117`|people-2|single|agile|3|On a charity's online donations platform, the head of fundraising and the digital accessibility lead have each been approaching…|
|`pmp-p2-118`|people-2|single|agile|2|During a sprint planning call for a travel-booking team, a tester asks how the new fare-caching service will handle currency…|
|`pmp-p2-119`|people-2|single|hybrid|5|Your hybrid programme is delivering connected e-bike docks for a city. The hardware lead wants the next firmware drop moved from…|
|`pmp-p2-120`|people-2|multi|predictive|3|On an ERP rollout for a logistics firm, the project charter sets a conflict escalation path: parties try to resolve a dispute…|
|`pmp-p2-121`|people-2|single|predictive|1|On a train maintenance depot upgrade, you are showing a delegation from the rail operator's board around the site. From the site…|
|`pmp-p2-122`|people-2|single|agile|2|A fully remote data-platform team at an online grocer has started arguing about cameras in meetings. Three developers keep their…|
|`pmp-p2-123`|people-2|single|predictive|3|On a flood defence scheme for a county council, the hydraulic modelling lead and the environmental assessment lead are openly…|
|`pmp-p2-124`|people-2|single|hybrid|4|On a hybrid programme building firmware for an infusion pump, your most experienced firmware engineer asks to see you and says:…|
|`pmp-p2-125`|people-2|single|predictive|3|Your company is implementing a purchasing system for a furniture retailer. In a requirements workshop, the retailer's finance…|
|`pmp-p2-126`|people-2|single|agile|3|On a parcel-tracking app team, a developer tells you in a one-to-one that she has picked up almost all the production support…|
|`pmp-p2-127`|people-2|single|predictive|5|On a regional museum's gallery refit, the lead curator and the exhibition designer have disliked each other for a year. You have…|
|`pmp-p2-128`|people-2|single|hybrid|3|On a housing association's tenant portal programme, a junior analyst tells you privately that the data lead has repeatedly mocked…|
|`pmp-p2-129`|people-2|single|agile|3|A developer who joined your energy-billing team two months ago keeps proposing contract testing between services. Each time, two…|
|`pmp-p2-130`|people-2|single|predictive|2|You manage a pension administration system replacement and prepare the monthly steering group pack. At this month's meeting, the…|
|`pmp-p2-131`|people-2|single|predictive|4|On a new secondary school building, the structural engineer and the architect have argued at the last three design meetings about…|
|`pmp-p2-132`|people-2|single|hybrid|4|You are leading a hybrid warehouse-system rollout for a Belgian distributor. The software supplier's account manager and your…|
|`pmp-p2-133`|people-2|multi|predictive|3|On a container terminal automation project, the crane-controls lead and the yard-systems lead agreed in a facilitated meeting two…|
|`pmp-p2-134`|people-2|multi|agile|3|Two developers on your video-streaming team were close friends until a falling-out outside work last month. Since then they have…|
|`pmp-p2-135`|people-2|multi|hybrid|4|Your supplier team is building a grants management system for a government department using Scrum, inside the department's…|
|`pmp-p2-136`|people-2|matching|agile|2|Prompts: Head of marketing: 'The virtual queue mu… / Lead developer: 'We are not shipping the… / Customer service manager: 'No release at…|
|`pmp-p2-137`|people-2|matching|predictive|3|Prompts: Two installation team leaders are giving… / In a weekly review attended by the clien… / Two trainers disagree about whether to t…|
|`pmp-p2-138`|people-2|pulldown|agile|2|Prompts: Two developers disagree about the loggin… / Team members now refer to 'the mobile ca… / One developer refuses to attend any meet…|
|`pmp-p2-139`|people-2|fillblank|hybrid|3|Two leads on a veterinary diagnostics firm's hybrid programme disagree about which laboratory information system to buy. Before…|
|`pmp-p2-140`|people-2|hotspot|agile|3|On a fintech app team, a tester asks you, as Scrum Master, to look over a message she plans to send a developer who keeps merging…|
|`pmp-p2-1`|pmp-lectures-1-11|single|agile|3|During a retrospective meeting, some team members expressed that they strongly disagree with how the product owner and scrum…|
|`pmp-p2-2`|pmp-lectures-1-11|single|agile|3|A new project manager is assigned to an ongoing agile project. The project is now starting its second 4-week iteration cycle. The…|
|`pmp-p2-3`|pmp-lectures-1-11|single|agile|3|During a software development project, the team realized that several features failed to comply with quality requirements. After…|
|`pmp-p2-4`|pmp-lectures-1-11|single|predictive|4|A client's interactions with the project team have been somewhat negative. Project team members don't want to be part of the…|
|`pmp-p2-5`|pmp-lectures-1-11|single|agile|3|A team has recently adopted agile and is following a scrum model. There is a conflict between a lead developer and a quality…|
|`pmp-p2-6`|pmp-lectures-1-11|single|predictive|4|A project team member suddenly approaches the project manager and asks to resign from the project. The team member is upset…|
|`pmp-p2-7`|pmp-lectures-1-11|single|predictive|3|A project manager is leading a complex software development project. The project team cannot agree on which process to use when…|
|`pmp-p2-8`|pmp-lectures-1-11|single|predictive|4|A key member of a project team publicly declares that another team member has been speaking negatively about them. The team…|
|`pmp-p2-9`|pmp-lectures-1-11|single|predictive|3|During a multidisciplinary project, a project manager learns that there are many conflicts about design among team members from…|
|`pmp-p2-201`|pmp-mocks-1-10|single|agile|3|During a standup, a team member falsely reports the completion of a story which is a dependency for other stories. When the team…|
|`pmp-p2-202`|pmp-mocks-1-10|single|agile|2|During a standup meeting, some of the team members strongly disagreed about how tasks were distributed. They feel tired and…|
|`pmp-p2-203`|pmp-mocks-1-10|single|predictive|2|Discussions during weekly project team meetings are frequently dominated by a senior team member who has a tendency to talk over…|
|`pmp-p2-204`|pmp-mocks-1-10|single|predictive|3|A delivery team member is complaining about the workload and the number of top priorities they are handling. Another team member…|
|`pmp-p2-205`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a hybrid project with a self-organizing team. The team is unable to agree on the scope for the next…|
|`pmp-p2-206`|pmp-mocks-1-10|single|predictive|2|A senior team member is having challenges with a new project team member. The senior team member asks the project manager for…|
|`pmp-p2-207`|pmp-mocks-1-10|single|agile|3|Company A is moving from a predictive approach to an agile one. After the second incremental release of project X, the product…|
|`pmp-p2-208`|pmp-mocks-1-10|single|hybrid|3|A project is underway with team members from two different countries and there are complaints from both sides. The team members…|
|`pmp-p2-209`|pmp-mocks-1-10|single|predictive|3|A project manager is working on a new project. Two key project resources have a disagreement regarding a requirement. The project…|
|`pmp-p2-210`|pmp-mocks-1-10|single|hybrid|2|A virtual project team is composed of members from various countries with cultural differences. The project manager is concerned…|
|`pmp-p2-211`|pmp-mocks-1-10|single|agile|3|A project leader and a product owner had a side conversation about the status of the next delivery. The product owner raised…|
|`pmp-p2-212`|pmp-mocks-1-10|single|predictive|2|A delivery milestone for a project is fast approaching, but an item that is planned to be completed is at risk. A team member…|
|`pmp-p2-213`|pmp-mocks-1-10|single|predictive|2|A project manager complains that three engineers from different cultural backgrounds leave project meetings twice a day and…|
|`pmp-p2-214`|pmp-mocks-1-10|single|hybrid|2|A project manager identifies conflicts among local and virtual team members' understanding of the deliverables.|
|`pmp-p2-215`|pmp-mocks-1-10|single|predictive|2|During a meeting with project stakeholders, two stakeholders have a disagreement regarding how a software component is being…|
|`pmp-p2-216`|pmp-mocks-1-10|single|predictive|2|A project manager is working on a complex software project. Two team members must team up and work together on a critical module…|
|`pmp-p2-217`|pmp-mocks-1-10|single|predictive|2|A team member is hosting an event and invites several colleagues through their work calendars. Another team member, who was not…|
|`pmp-p2-218`|pmp-mocks-1-10|single|predictive|1|A project manager has noticed apparent interpersonal conflict between some team members during the initiation of a project.|
|`pmp-p2-219`|pmp-mocks-1-10|single|predictive|2|A project team has an after-hours get-together. One of the team members who was recently hired and new to the team did not…|
|`pmp-p2-220`|pmp-mocks-1-10|single|predictive|2|A newly-hired project manager is running a project for the first time and is worried about rising tensions. Project team members…|
|`pmp-p2-221`|pmp-mocks-1-10|single|predictive|2|A project manager is leading a project team composed of members from different departments, including engineering, marketing, and…|
|`pmp-p2-222`|pmp-mocks-1-10|single|predictive|2|During a project team meeting, the project manager notices a conflict between three key team members who have very different…|
|`pmp-p2-223`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to a project with a lot of stakeholders and competing priorities.|
|`pmp-p2-224`|pmp-mocks-1-10|single|agile|2|A project manager notices a potential conflict between a remote developer and an on-site tester during a sprint; the developer…|
|`pmp-p2-225`|pmp-mocks-1-10|single|predictive|3|An expert resource avoids assignments that include working with a foreign contractor.|
|`pmp-p2-226`|pmp-mocks-1-10|single|agile|2|A project manager is working on an IT project. During an iteration review meeting, a couple of key stakeholders enter into a…|
|`pmp-p2-227`|pmp-mocks-1-10|single|agile|2|The project manager schedules an iteration review and learns that a few key stakeholders do not approve of how a feature has been…|
|`pmp-p2-228`|pmp-mocks-1-10|single|predictive|2|A project manager working with a culturally diverse team discovers major intra-team conflicts during project execution.|
|`pmp-p2-229`|pmp-mocks-1-10|single|predictive|2|A project manager is managing multidisciplinary teams. During the last project review meeting, the project manager realized that…|
|`pmp-p2-230`|pmp-mocks-1-10|single|agile|2|A project team member continues to change a colleague's deliverable during every sprint. The colleague has asked the project…|
|`pmp-p2-231`|pmp-mocks-1-10|single|predictive|2|A team member is unhappy with their workload and feels they are not being supported enough. The project manager wants to meet…|
|`pmp-p2-232`|pmp-mocks-1-10|single|agile|2|The conflict about how to proceed has developed between two team members prior to a release. The team needs to work together to…|
|`pmp-p2-233`|pmp-mocks-1-10|single|hybrid|3|A geographically distributed project team has development issues between the colocated and virtual teams, causing productivity to…|
|`pmp-p2-234`|pmp-mocks-1-10|single|predictive|2|A project team is small and the team members have known each other for a long time. The team's project is in the execution phase.…|
|`pmp-p2-235`|pmp-mocks-1-10|single|predictive|2|A team member with sufficient experience and knowledge informs the project manager about a conflict with another team member and…|
|`pmp-p2-236`|pmp-mocks-1-10|single|predictive|2|The execution of a construction project in a remote area requires team members to live together in corporate dormitories when…|
|`pmp-p2-237`|pmp-mocks-1-10|single|predictive|3|At the midpoint of a renewable energy project, the project manager is faced with a conflict between the design team and…|
|`pmp-p2-238`|pmp-mocks-1-10|single|predictive|3|During a project charter review session, a dispute arises between the project sponsor and the product owner, witnessed by the…|
|`pmp-p2-239`|pmp-mocks-1-10|single|predictive|3|During the project execution phase, conflicts have arisen between two key team members, negatively impacting their work output.…|
|`pmp-p2-240`|pmp-mocks-1-10|multi|predictive|3|At the outset of a new phase in a complex project, after a successful kick-off meeting at the client's site, a team member…|
|`pmp-p2-241`|pmp-mocks-1-10|single|predictive|2|A project manager is overseeing the development of a new urban park. During the project, a dispute arises between the landscape…|
|`pmp-p2-242`|pmp-mocks-1-10|multi|predictive|3|During the construction phase of a new city park, conflicts have emerged between the landscape designers and environmental…|
|`pmp-p2-243`|pmp-mocks-1-10|single|agile|2|As the project manager for a novel online education platform, you're faced with divergent views among stakeholders regarding the…|
|`pmp-p2-244`|pmp-mocks-1-10|single|predictive|2|You are working on a high profile project. Several high-ranking senior executives want to take the credit for this initiative,…|
|`pmp-p2-245`|pmp-mocks-1-10|single|predictive|2|During a key scope meeting, an argument arises. The business manager accuses the technology manager of not delivering to their…|
|`pmp-p2-246`|pmp-mocks-1-10|single|predictive|3|Your team members have raised an issue as they believe the current project scope will not reach the intended project goal of…|
|`pmp-p2-247`|pmp-mocks-1-10|single|agile|2|You are an agile project manager, and holding daily stand-ups with your team. You notice people accusing other team members of…|
|`pmp-p2-248`|pmp-mocks-1-10|single|predictive|1|You are working on a project where the functional manager is unhappy with the changes being brought about to his part of the…|
|`pmp-p2-249`|pmp-mocks-1-10|single|predictive|2|The project you are working on has recently started and the team has come together from many different parts of the organisation,…|
|`pmp-p2-250`|pmp-mocks-1-10|single|predictive|2|You are working on a project where two high-ranked executives keep derailing project meetings with their own agenda, and adding…|
