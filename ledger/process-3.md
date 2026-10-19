# Question ledger — process-3 · Value delivery

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

### process-3 · Value delivery — 149 items

*Prioritise by value and keep checking the work is still worth doing.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-pr3-001`|core|fillblank|agile|3|A Kanban team currently has 24 work items in progress and finishes an average of 6 items per week. Work down the average cycle…|
|`pmp-pr3-002`|core|single|agile|3|Your product backlog has 240 items. The product owner prioritises by asking each stakeholder to rank their own requests, then…|
|`pmp-pr3-003`|core|single|agile|3|Your backlog is ordered by a scoring model the product owner built eighteen months ago. Two features near the top serve a…|
|`pmp-pr3-004`|core|single|hybrid|3|Your programme was funded to save two million a year by automating claims handling. Nine months in, a competitor's acquisition…|
|`pmp-pr3-005`|core|fillblank|agile|3|Your team can start only one of two features next. Finance estimates that delaying the express checkout feature costs the…|
|`pmp-pr3-006`|core|single|agile|3|Your team could release a working version of the booking feature now, covering the two journeys that account for most bookings,…|
|`pmp-pr3-007`|core|fillblank|predictive|3|A warehouse automation project will cost £480,000 to deliver and is forecast to reduce operating costs by £160,000 in each full…|
|`pmp-pr3-008`|core|single|agile|3|Usage data six months after launch shows that four of the eleven features your team built are used by fewer than two per cent of…|
|`pmp-pr3-014`|core|multi|hybrid|4|A national museum's hybrid programme is refurbishing six galleries to a fixed plan while an agile team builds the visitor app.…|
|`pmp-pr3-010`|core|fillblank|predictive|3|Two proposals compete for the same funding. Yours requires an investment of £1,800,000 and is forecast to return net benefits of…|
|`pmp-pr3-011`|core|multi|agile|3|Your team completes and demonstrates working increments every two weeks and has done so for eight months. Nothing reaches…|
|`pmp-pr3-012`|core|single|hybrid|3|Your product owner prioritises the backlog strictly by customer value and has deferred a mandatory accessibility conformance…|
|`pmp-pr3-013`|core|fillblank|agile|3|Your product owner has five remaining backlog items and has estimated the annual benefit of each: £540,000, £360,000, £180,000,…|
|`pmp-pr3-061`|core|graphic|agile|3|Your team's average lead time has doubled over the past month. The board shows the items in each column against its…|
|`pmp-case2-005`|case-02|multi|agile|4|The old permit database contract ends in five months and cannot be extended. Malik asks which of the remaining items should come…|
|`pmp-case5-010`|case-05|single|agile|4|The agile team's next monthly release could include either the shared call-routing features that let either centre answer any…|
|`pmp-pr3-101`|process-3|single|predictive|1|Your project is rolling out electronic prescribing across a hospital trust's four sites and closes next month. The business case…|
|`pmp-pr3-102`|process-3|single|agile|3|You lead an agile team building a scheduling app for home-care agencies. The sales director has asked the product owner to move…|
|`pmp-pr3-103`|process-3|single|predictive|3|Your project replaces a water company's billing platform, with go-live in three weeks. One benefit in the business case is a 25…|
|`pmp-pr3-104`|process-3|single|agile|3|Your team at a retail bank completes features every two weeks, but the bank's release board approves production releases once a…|
|`pmp-pr3-105`|process-3|multi|predictive|3|A third-party logistics firm goes live with a new warehouse management system in six weeks. The forecast benefit is an 18 per…|
|`pmp-pr3-106`|process-3|single|hybrid|4|In a hybrid programme for a hotel group, check-in kiosks are being installed in 40 hotels to a fixed plan, and an agile team…|
|`pmp-pr3-107`|process-3|matching|agile|3|Prompts: Answers from a rider survey need sorting… / Six councils that fund the service each… / A release tied to a fixed timetable-chan…|
|`pmp-pr3-108`|process-3|single|predictive|3|Your project is implementing a repairs-scheduling system for a city council's housing department. The benefit owner named in the…|
|`pmp-pr3-109`|process-3|single|predictive|5|Your project is moving a university's written examinations onto a digital platform. The business case counts two benefits:…|
|`pmp-pr3-110`|process-3|fillblank|predictive|3|A lift-maintenance company rolled out a mobile job app to its field engineers three months ago. The business case assumed 600…|
|`pmp-pr3-111`|process-3|single|agile|1|Your team is building a donation app for a national charity. At the sprint review, pilot donors say the monthly-giving screen…|
|`pmp-pr3-112`|process-3|single|predictive|2|You are installing solar panels across 30 schools in a multi-academy trust, planned in five waves of six schools in alphabetical…|
|`pmp-pr3-113`|process-3|multi|agile|3|An agile team at a motor insurer has been building a self-service claims portal for nine months. The sponsor asks for a dashboard…|
|`pmp-pr3-114`|process-3|single|hybrid|5|A parcel courier's programme is building a bespoke route-planning system in sprints, with depot rollouts scheduled in fixed…|
|`pmp-pr3-115`|process-3|pulldown|hybrid|2|Prompts: The gate installation at the two largest… / A rival operator's free rebooking offer…|
|`pmp-pr3-116`|process-3|hotspot|agile|2|An agile team at an online pharmacy has mapped how a change travels from an agreed idea to customers. Average lead time is 41…|
|`pmp-pr3-117`|process-3|matching|hybrid|3|Prompts: The tenant portal went live in all fourt… / Sixty-two per cent of repair requests ar… / Contact-centre running costs have fallen…|
|`pmp-pr3-118`|process-3|single|predictive|4|Your project is delivering a case-management system for a county council's children's services. The business case rests on each…|
|`pmp-pr3-119`|process-3|single|agile|4|You are the agile lead for a fintech team in the third day of a two-week sprint. The sprint goal is to complete an open-banking…|
|`pmp-pr3-120`|process-3|multi|hybrid|3|A hospital is building a new outpatient wing to a fixed design through a contractor, while an internal agile team builds the…|
|`pmp-pr3-121`|process-3|single|predictive|1|You are managing delivery of a berth-booking system for a port authority against an approved scope baseline. At a progress check…|
|`pmp-pr3-122`|process-3|single|agile|3|A gym chain's agile team plans to build a class-recommendation engine that suggests sessions to members, on the hypothesis that…|
|`pmp-pr3-123`|process-3|single|predictive|3|Your project is building a six-gate extension to a regional airport terminal, due for completion in October. The first three…|
|`pmp-pr3-124`|process-3|single|hybrid|4|A pensions administrator's hybrid programme is moving members from a legacy platform to a new one. The business case rests on…|
|`pmp-pr3-125`|process-3|single|agile|3|An online grocer's team spent three sprints redesigning checkout. The new design was released behind a feature toggle to half of…|
|`pmp-pr3-126`|process-3|single|agile|3|A physiotherapy clinic chain's agile team is building a booking app whose business case depends on submitting claims directly to…|
|`pmp-pr3-127`|process-3|single|predictive|4|You manage a predictive project replacing a rail operator's procurement system. Its business case claims £900,000 a year from…|
|`pmp-pr3-128`|process-3|single|predictive|2|A fire and rescue service's project is replacing paper rostering with a self-service system. Two benefits are listed: lower…|
|`pmp-pr3-129`|process-3|single|predictive|3|A mining company has contracted a supplier to replace its maintenance management system. The approved requirement, set by the…|
|`pmp-pr3-130`|process-3|single|agile|3|A veterinary practice group wants to cut missed appointments, which cost it about £18,000 a month. The product owner has asked…|
|`pmp-pr3-131`|process-3|single|hybrid|5|An insurer's hybrid programme is replacing its claims platform. The agile team's next increment auto-approves low-value home…|
|`pmp-pr3-132`|process-3|single|predictive|4|Your project has piloted a new appointment-booking process at one of a dental group's 28 practices for twelve weeks. Missed…|
|`pmp-pr3-133`|process-3|multi|agile|2|An agile team at a pharmacy chain has spent five months building a stock-ordering tool for branch pharmacists. Every sprint…|
|`pmp-pr3-134`|process-3|multi|hybrid|3|A building society's hybrid programme is replacing its branch teller system. The agile team releases new teller features to all…|
|`pmp-pr3-135`|process-3|multi|predictive|4|Your project retrofitted insulation and heat pumps in 120 council-owned buildings, with a business case promising a 25 per cent…|
|`pmp-pr3-136`|process-3|matching|predictive|2|Prompts: Keeping the benefits register current du… / Changing depot working practices so that… / Deciding at a stage gate whether the for…|
|`pmp-pr3-137`|process-3|matching|agile|3|Prompts: A new hotel-ranking algorithm could lowe… / A rebuilt search service must cope with… / Two wordings for the booking button are…|
|`pmp-pr3-138`|process-3|pulldown|hybrid|2|Prompts: Route-planning software lets the existin… / An online repairs service lets the counc… / Finance officers each save four hours a…|
|`pmp-pr3-139`|process-3|fillblank|agile|3|A meal-kit company's product owner is weighing a family-plan feature that costs £120,000 to build. If families take it up, it…|
|`pmp-pr3-140`|process-3|hotspot|hybrid|3|A hybrid programme is building a new central library to a fixed opening date. The agile team building its online room-booking…|
|`pmp-pr3-1`|pmp-lectures-1-11|single|predictive|2|A project is nearing completion and a stakeholder wants to know how the project outcomes will impact the organization.|
|`pmp-pr3-2`|pmp-lectures-1-11|single|predictive|3|At the beginning of a project, a stakeholder is concerned that the initiative may not deliver value to the organization.|
|`pmp-pr3-3`|pmp-lectures-1-11|single|predictive|2|A lengthy upgrade project is being delivered in a series of phased outputs. The end users will face some level of disruption…|
|`pmp-pr3-4`|pmp-lectures-1-11|single|predictive|3|During the final stage of a project, one of the key stakeholders asks the project manager to brief the executive board about the…|
|`pmp-pr3-5`|pmp-lectures-1-11|single|predictive|2|A project manager has completed the project deliverable and has passed the acceptance criteria.|
|`pmp-pr3-6`|pmp-lectures-1-11|single|hybrid|4|As a project is nearing completion, a struggle between the product owner and the project sponsor regarding who owns the benefits…|
|`pmp-pr3-7`|pmp-lectures-1-11|single|hybrid|3|A company is transitioning from using a predictive approach to a hybrid predictive-agile project implementation. The leadership…|
|`pmp-pr3-8`|pmp-lectures-1-11|single|predictive|3|A newly hired project manager is assigned to a school construction project. The school serves students from surrounding…|
|`pmp-pr3-9`|pmp-lectures-1-11|single|agile|3|An agile project is in its first of ten iterations. After the iteration review, the team receives stakeholder feedback that the…|
|`pmp-pr3-10`|pmp-lectures-1-11|single|hybrid|3|During the final sprint review, the product owner stated that the product does not deliver any value. The project manager…|
|`pmp-pr3-11`|pmp-lectures-1-11|single|agile|4|During sprint planning, an agile team and product owner identified that the team should be able to finish five user stories.…|
|`pmp-pr3-12`|pmp-lectures-1-11|single|agile|3|An organization makes an acquisition of a similar product halfway through the development cycle of a potential product. This…|
|`pmp-pr3-201`|pmp-mocks-1-10|single|hybrid|3|A scrum team is using a hybrid approach with 2-week sprints. The minimum viable product (MVP) release is at the end of the…|
|`pmp-pr3-202`|pmp-mocks-1-10|single|agile|3|A business manager and product owner are requesting a more flexible way to incorporate changes into the product backlog. They…|
|`pmp-pr3-203`|pmp-mocks-1-10|single|agile|2|An agile team is receiving multiple unplanned requests for product enhancements in an iteration. This impediment is causing the…|
|`pmp-pr3-204`|pmp-mocks-1-10|single|predictive|3|At the end of a large project, the stakeholders realize that the achieved benefits are not the same as the expected benefits.|
|`pmp-pr3-205`|pmp-mocks-1-10|single|agile|3|An agile project team has been successfully releasing updated deliverables each sprint that consistently provide an increased…|
|`pmp-pr3-206`|pmp-mocks-1-10|single|agile|2|During a project planning session, the project manager is trying to define a release plan to achieve consistent value delivery…|
|`pmp-pr3-207`|pmp-mocks-1-10|single|agile|3|A project manager is working on launching a minimum viable product (MVP) for a new-to-market product. The team has successfully…|
|`pmp-pr3-208`|pmp-mocks-1-10|single|hybrid|3|During product development, two functional departments indicate that they are short on resources and cannot build and deliver all…|
|`pmp-pr3-209`|pmp-mocks-1-10|single|agile|3|A project manager has developed a project management plan based on agile methodologies in an effort to deliver the product…|
|`pmp-pr3-210`|pmp-mocks-1-10|multi|predictive|4|A project manager is leading a project to implement a process improvement software system that was developed in-house. However,…|
|`pmp-pr3-211`|pmp-mocks-1-10|single|agile|3|In an agile project, the project status indicates that the project is 95% completed. During an iteration review, the stakeholders…|
|`pmp-pr3-212`|pmp-mocks-1-10|single|predictive|3|A project is close to completion, and the customer wants to celebrate the success by sharing a business case about the benefits…|
|`pmp-pr3-213`|pmp-mocks-1-10|single|agile|3|A project manager is overseeing a project aimed at providing customers with real-time shipment tracking capabilities. The project…|
|`pmp-pr3-214`|pmp-mocks-1-10|single|predictive|3|During sign-off for a strategic project that resulted in additional costs, a project manager was unable to justify the additional…|
|`pmp-pr3-215`|pmp-mocks-1-10|single|hybrid|2|A marketing team is developing ideas in order to revamp an existing product. The marketing executive reaches out to the project…|
|`pmp-pr3-216`|pmp-mocks-1-10|single|agile|2|An agile project is in its last iteration. During the daily standup, the team realizes that they will not be able to complete all…|
|`pmp-pr3-217`|pmp-mocks-1-10|single|predictive|3|A project manager has been assigned to a project to implement an asset health management system. The executive business sponsor…|
|`pmp-pr3-218`|pmp-mocks-1-10|single|predictive|3|During a new technology project, confusion arises due to the lack of a clearly defined and communicated future state of the…|
|`pmp-pr3-219`|pmp-mocks-1-10|single|agile|2|A team member suggests using a new product to speed up feature development, but this will increase the project budget. The…|
|`pmp-pr3-220`|pmp-mocks-1-10|single|agile|3|A new system is being developed to replace an existing manual process. During a sprint demo, business stakeholders expressed the…|
|`pmp-pr3-221`|pmp-mocks-1-10|single|agile|3|Due to a downturn in the business environment, a customer is only able to make a project's payment in part, rather than the fully…|
|`pmp-pr3-222`|pmp-mocks-1-10|single|agile|3|A startup in its early stages has been acquired by a major company. In light of this new scenario, the project manager has been…|
|`pmp-pr3-223`|pmp-mocks-1-10|single|predictive|3|A project manager works in a matrix organization and manages an internal software development project to improve the company's…|
|`pmp-pr3-224`|pmp-mocks-1-10|single|predictive|3|A large-scale, multiyear transformation project was completed as per the defined requirements and within the approved budget and…|
|`pmp-pr3-225`|pmp-mocks-1-10|single|agile|2|A project manager is working on a software development project. Several team members have become overly focused on delivering…|
|`pmp-pr3-226`|pmp-mocks-1-10|single|predictive|3|A project manager is leading a business transformation project at a company facing significant financial challenges due to a…|
|`pmp-pr3-227`|pmp-mocks-1-10|single|predictive|3|A project manager is assigned to a complex IT project that is expected to deliver significant benefits to the organization. The…|
|`pmp-pr3-228`|pmp-mocks-1-10|single|agile|3|A product owner has been attending daily standups but seems focused only on the budget and costs of each iteration. When…|
|`pmp-pr3-229`|pmp-mocks-1-10|single|hybrid|3|In the middle of a three-year-long project, the company committee overseeing the project becomes uncertain about whether the…|
|`pmp-pr3-230`|pmp-mocks-1-10|single|predictive|2|A project manager is overseeing the development and launch of a new product. In order to comply with the Project Management…|
|`pmp-pr3-231`|pmp-mocks-1-10|single|agile|3|A project manager has started a new project with a 24 month timeline. Soon after the kickoff, the sponsor informs the project…|
|`pmp-pr3-232`|pmp-mocks-1-10|single|agile|2|During a complex project, a stakeholder informs the project manager about a requirement and proposes to call a meeting to inform…|
|`pmp-pr3-233`|pmp-mocks-1-10|single|hybrid|3|A project manager is overseeing a project with an extremely tight schedule. During a meeting, the project stakeholders express…|
|`pmp-pr3-234`|pmp-mocks-1-10|single|predictive|2|An organization is redesigning and relaunching a key product. The stakeholders are all company executives and want to ensure that…|
|`pmp-pr3-235`|pmp-mocks-1-10|single|predictive|1|A project manager is part of a project management strategy focus group that is brainstorming the ways projects will be used to…|
|`pmp-pr3-236`|pmp-mocks-1-10|single|agile|1|An agile team is determining which activities to complete first during an upcoming project iteration for a new product…|
|`pmp-pr3-237`|pmp-mocks-1-10|single|predictive|2|A project manager is midway through project delivery. The project manager decides to hold a stakeholder engagement meeting with…|
|`pmp-pr3-238`|pmp-mocks-1-10|single|predictive|3|Due to regulatory changes, a government agency is now a key stakeholder in a complex project. An agency representative has asked…|
|`pmp-pr3-239`|pmp-mocks-1-10|single|agile|2|During a standup, a project team member suggests modifying some features to benefit from a new opportunity. Another team member…|
|`pmp-pr3-240`|pmp-mocks-1-10|single|agile|3|A project manager is leading a development project for a high-profile client with strict deadlines. During a sprint, the project…|
|`pmp-pr3-241`|pmp-mocks-1-10|single|agile|3|A product company is transforming the way it develops and releases products in the market. Executives believe that this is a…|
|`pmp-pr3-242`|pmp-mocks-1-10|single|hybrid|3|A customer indicates that they would like to add functionality. Although this request is made during the execution phase of the…|
|`pmp-pr3-243`|pmp-mocks-1-10|single|predictive|2|Before a project evaluation meeting, a key stakeholder complains that the project does not deliver the expected business…|
|`pmp-pr3-244`|pmp-mocks-1-10|single|agile|3|During an agile iteration, Task 1 cannot be completed on time due to unexpected challenges. Another team within the project is…|
|`pmp-pr3-245`|pmp-mocks-1-10|single|agile|2|During a daily standup, some team members realized they were not all prioritizing the requirements in the same way. This could…|
|`pmp-pr3-246`|pmp-mocks-1-10|single|agile|2|In the most recent sprint review meeting, the client announced that they need to implement two of the five modules committed for…|
|`pmp-pr3-247`|pmp-mocks-1-10|single|agile|2|A project manager is assigned to a new project. The requirements are still evolving, but there is a need to deliver business…|
|`pmp-pr3-248`|pmp-mocks-1-10|single|agile|2|An agile development team changes a design element in a project. Testing results improve dramatically over the next two…|
|`pmp-pr3-249`|pmp-mocks-1-10|single|agile|2|During a critical phase of a software development project, a key developer notifies the project manager that they will need to…|
|`pmp-pr3-250`|pmp-mocks-1-10|single|hybrid|2|A project manager is overseeing the development of an enterprise resource planning (ERP) system and has decided that a…|
|`pmp-pr3-251`|pmp-mocks-1-10|single|predictive|2|Your project is nearing completion. You have monitored and controlled quality through testing, and risks with mitigations…|
|`pmp-pr3-252`|pmp-mocks-1-10|single|agile|2|In the development stage of an innovative mobile application, the agile project team discovers the need for an advanced software…|
|`pmp-pr3-253`|pmp-mocks-1-10|single|agile|2|During a mobile application development project, the project manager faces significant uncertainty regarding user preferences and…|
|`pmp-pr3-254`|pmp-mocks-1-10|single|hybrid|2|Amid the rollout of a new software platform designed to enhance customer engagement for a retail chain, the lead role transitions…|
|`pmp-pr3-255`|pmp-mocks-1-10|single|agile|2|A technology firm is transitioning its software development practices to agile methodologies to improve its product development…|
|`pmp-pr3-256`|pmp-mocks-1-10|single|agile|2|As a project manager overseeing a pivotal update to a client's software system, you've noticed a significant lack of engagement…|
|`pmp-pr3-257`|pmp-mocks-1-10|single|hybrid|3|In a project designed to deploy new technology across several departments, a project manager notices a shift in stakeholder…|
|`pmp-pr3-258`|pmp-mocks-1-10|single|agile|2|Facing the challenge of rolling out a critical disaster response application, a project manager leads a multidisciplinary team…|
|`pmp-pr3-259`|pmp-mocks-1-10|single|agile|2|A project manager is overseeing a project designed to deliver iterative enhancements to an existing service and has an…|
|`pmp-pr3-260`|pmp-mocks-1-10|single|predictive|2|As a project manager overseeing the rollout of a new online banking platform, ensuring that customers can seamlessly transition…|
|`pmp-pr3-261`|pmp-mocks-1-10|single|agile|3|You have recently completed a project for a new product and have been asked to manage it after its release to keep it relevant in…|
|`pmp-pr3-262`|pmp-mocks-1-10|single|predictive|3|You are initiating a project to a sales team where the process will change significantly, even though the outcome of a completed…|
|`pmp-pr3-263`|pmp-mocks-1-10|single|agile|2|The product owner has worked with a team of senior users to gather feature ideas. They now have a list of more than fifty ideas…|
|`pmp-pr3-264`|pmp-mocks-1-10|single|predictive|2|You have been given authority to manage the upcoming features your team will release. You do not have a high enough level of…|
|`pmp-pr3-265`|pmp-mocks-1-10|single|agile|1|Your agile project has recently gained some new stakeholders after a department restructure. They complain to you that they are…|
|`pmp-pr3-266`|pmp-mocks-1-10|single|agile|1|Your agile project has delivered three features so far and is halfway through delivering the latest improvement. During a…|
|`pmp-pr3-267`|pmp-mocks-1-10|single|agile|1|You are a project manager working on an Agile project, and want your team to maximise the value they bring to the customer.|
|`pmp-pr3-268`|pmp-mocks-1-10|single|agile|1|You are working on an Agile project and the project team have just finished an iteration. All tasks for the iteration have been…|
|`pmp-pr3-269`|pmp-mocks-1-10|single|agile|1|Feedback in many different forms is one of the most important parts of a successful Agile product delivery.|
|`pmp-pr3-270`|pmp-mocks-1-10|single|predictive|2|You are in the process of creating a project charter for your part of the organisation, to try and gain resources for an…|
|`pmp-pr3-271`|pmp-mocks-1-10|single|predictive|3|You have been planning a project with the manager of a functional area who needs to increase their profit by 57% in the next…|
|`pmp-pr3-272`|pmp-mocks-1-10|single|agile|2|You are the Scrum Master and about to begin a new iteration in your Agile project. New features have been added to the backlog,…|
|`pmp-pr3-273`|pmp-mocks-1-10|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values.|
|`pmp-pr3-274`|pmp-mocks-1-10|single|agile|2|You are the Agile lead or Scrum Master for your Agile team and planning for the next iteration is about to begin.|
|`pmp-pr3-275`|pmp-mocks-1-10|single|agile|1|The Agile team for which you are the project manager has determined several key features to deliver to the customer.|
|`pmp-pr3-276`|pmp-mocks-1-10|single|agile|1|You are working on a project as an Agile project manager. You look at the product backlog and ensure several features, or minimum…|
|`pmp-pr3-277`|pmp-mocks-1-10|single|agile|2|A team new to Agile has worked closely with the product owner and has identified the highest value features of the project.|
|`pmp-pr3-278`|pmp-mocks-1-10|single|agile|2|You have begun planning a large project with the input from your Project Sponsor and the help of your project team. The project…|
|`pmp-pr3-279`|pmp-mocks-1-10|single|predictive|2|Your project is around half-way complete and has had a great deal of issues around resources and funding. Other executives from…|
|`pmp-pr3-280`|pmp-mocks-1-10|single|agile|2|You are an agile project manager and your team have delivered three features to improve a customer app over the last three…|
|`pmp-pr3-281`|pmp-mocks-1-10|single|agile|3|You are working on an agile project and are planning your product backlog for the next three to six months. You have some high…|
