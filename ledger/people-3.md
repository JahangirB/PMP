# Question ledger — people-3 · Leading the team

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

### people-3 · Leading the team — 224 items

*Set expectations, empower the team, and clear problems out of its path.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p3-001`|core|pulldown|agile|3|Prompts: A newly formed team is willing but has n… / A long-standing team knows the domain be…|
|`pmp-p3-002`|core|single|agile|2|During a daily standup, a developer mentions for the third day running that they are still waiting on a test environment from…|
|`pmp-p3-003`|core|single|predictive|3|You take over a project mid-execution. Within a week you notice that meeting attendance is optional in practice, decisions get…|
|`pmp-p3-016`|core|pulldown|hybrid|4|Prompts: The squad chose a single-page layout for… / The squad has planned the scanner integr… / To speed up testing, the squad has agree…|
|`pmp-p3-005`|core|single|agile|3|In daily standups you have started assigning the day's work item by item, because two sprints ago the team left an urgent defect…|
|`pmp-p3-007`|core|single|agile|3|A developer who joined your team three months ago is visibly struggling. Their work needs rework more often than anyone else's,…|
|`pmp-p3-008`|core|single|predictive|2|You inherit a well-established project team from a project manager who decided everything personally. The team is capable and…|
|`pmp-p3-017`|core|single|agile|3|Your team is building a track-inspection app for a rail operator. With six working days left in the sprint, the developers find…|
|`pmp-p3-018`|core|multi|predictive|3|On a £9 million refurbishment of a county archive building, you promoted Priya, an electrician on the project for a year, to…|
|`pmp-p3-011`|core|single|hybrid|4|A supplier team of twelve works alongside your eight employees on the same product. You have been asked to empower the combined…|
|`pmp-p3-012`|core|pulldown|agile|3|Prompts: For the tester whose work is invisible o… / For the developer who has done the same…|
|`pmp-p3-013`|core|single|agile|2|A developer tells you privately that she found a defect in her own work after it had passed review, fixed it quietly, and said…|
|`pmp-p3-014`|core|single|hybrid|3|Your strongest engineer is technically outstanding and consistently dismissive in design reviews, cutting people off and calling…|
|`pmp-p3-015`|core|pulldown|agile|3|Prompts: On the choice of testing library, which… / On the order of next quarter's themes, w… / On accepting a defect into the release,…|
|`pmp-p3-061`|core|graphic|agile|2|Three months after forming, your team has agreed its working norms, members resolve most disagreements between themselves, and…|
|`pmp-case2-004`|case-02|single|agile|4|Sunita, the sponsor, asks you to assign tasks to individual developers so she can show councillors who is doing what. The team…|
|`pmp-case4-010`|case-04|single|predictive|3|The in-house exhibition designers have worked long hours for six weeks to recover from late design approvals. One of them has…|
|`pmp-case5-009`|case-05|single|agile|2|Two advisers from the Calder centre who joined the agile team say little in retrospectives, although they raise useful points…|
|`pmp-p3-101`|people-3|single|predictive|1|You are leading the move of a building society's core systems to a new data centre. The client has moved the cutover from a…|
|`pmp-p3-102`|people-3|single|agile|3|Your team builds a payments app for a fintech start-up. Velocity has risen from 34 to 46 points over three sprints, and the…|
|`pmp-p3-103`|people-3|single|predictive|3|You lead a pharmaceutical team preparing a regulatory submission. Six weeks ago you told the team the filing deadline was 14…|
|`pmp-p3-104`|people-3|single|agile|3|On an online retailer's checkout team, the commercial director has started messaging two developers directly with small changes…|
|`pmp-p3-105`|people-3|single|predictive|2|You manage a water utility's mains-replacement programme. At the steering meeting, the sponsor thanks you personally for…|
|`pmp-p3-106`|people-3|single|agile|4|Your team builds route-planning software for a logistics firm. For two weeks the developers have debated which of two…|
|`pmp-p3-107`|people-3|single|predictive|3|On a highway-widening project, Sinead, a senior surveyor, tells you privately that her father has been diagnosed with a serious…|
|`pmp-p3-108`|people-3|single|hybrid|5|For a museum exhibition fit-out, you delegated authority to the build team to choose audio-visual suppliers up to €40,000. They…|
|`pmp-p3-109`|people-3|single|agile|2|Your game studio team has five developers in Lisbon and two in Auckland, which is thirteen hours ahead. The daily sync runs at…|
|`pmp-p3-110`|people-3|single|predictive|3|You lead a council's housing-repairs system replacement. At a closed steering meeting you learn that the project may be paused at…|
|`pmp-p3-111`|people-3|single|hybrid|4|You lead a hybrid programme replacing a university's student records system: a fixed data-migration plan alongside two agile…|
|`pmp-p3-112`|people-3|single|hybrid|3|At a medical-device maker, hardware follows a stage-gated plan while firmware is built in sprints. Joanna, a capable mid-level…|
|`pmp-p3-113`|people-3|multi|agile|3|Your team at an insurance comparison site can finish a change in two days, but every release must pass the security change board,…|
|`pmp-p3-114`|people-3|multi|predictive|4|You are managing a planned maintenance shutdown of a chemical plant, with forty fitters working in shifts. Site rules give every…|
|`pmp-p3-115`|people-3|multi|hybrid|3|Your team spent nine months building a claims self-service portal for an insurer. Two weeks before launch, the board shelved it…|
|`pmp-p3-116`|people-3|matching|agile|3|Prompts: The test suite fails intermittently beca… / The payment provider has ignored the tea… / A developer finds that the story she pic…|
|`pmp-p3-117`|people-3|matching|predictive|2|Prompts: A technician's careful isolation check b… / A site supervisor has submitted the crew… / A whole crew installed drainage to the w…|
|`pmp-p3-118`|people-3|pulldown|hybrid|4|Prompts: In estimation sessions the team waits fo… / A well-liked developer is openly sceptic… / A contractor with deep knowledge of the…|
|`pmp-p3-119`|people-3|fillblank|predictive|3|Your seven-engineer team at a broadcaster has a 10-working-day phase ahead. Two engineers each have 2 days of booked leave. Each…|
|`pmp-p3-120`|people-3|hotspot|agile|2|The diagram is your team's skills matrix, one row per skill, showing current expertise and the sprint in which the skill is next…|
|`pmp-p3-121`|people-3|single|predictive|1|You manage the construction of a secondary school extension. Site rules require hard hats and high-visibility vests beyond the…|
|`pmp-p3-122`|people-3|single|agile|3|Your team at a travel-booking platform agreed in its working agreement that pull requests are reviewed within one working day.…|
|`pmp-p3-123`|people-3|single|predictive|4|You lead the €2.4 million installation of a new production line at an industrial bakery. A key supplier has slipped its delivery…|
|`pmp-p3-124`|people-3|multi|predictive|3|Your £38 million rail signalling upgrade has eight weeks of commissioning left. The team of fourteen engineers, half of them on…|
|`pmp-p3-125`|people-3|single|hybrid|4|On a hybrid programme building an online grocer's fulfilment platform, you introduced a monthly 'save of the month' shout-out at…|
|`pmp-p3-126`|people-3|single|agile|3|Your team builds a booking app for a chain of veterinary practices. Four developers work in the office and three work remotely…|
|`pmp-p3-127`|people-3|matching|predictive|2|Prompts: Engineers received messages late in the… / Two discipline leads disagreed over a ce… / Drawings and markups were kept in email,…|
|`pmp-p3-128`|people-3|single|hybrid|2|You lead a hybrid team at an engineering consultancy designing a flood-defence scheme. Every milestone so far has been celebrated…|
|`pmp-p3-129`|people-3|single|agile|3|Two developers on your language-learning app team ask to move to a compressed week of four ten-hour days with Fridays off.…|
|`pmp-p3-130`|people-3|multi|agile|3|Your sports-streaming team of five had worked smoothly for a year. Three developers joined a month ago. Since then stand-ups have…|
|`pmp-p3-131`|people-3|single|predictive|3|You manage the design of a £60 million hospital wing for an architectural practice. Six architects each name layers, files and…|
|`pmp-p3-132`|people-3|single|agile|2|During a sprint review for a housing association's tenant portal, a senior stakeholder interrupts a junior developer's…|
|`pmp-p3-133`|people-3|single|hybrid|3|You lead a programme replacing an airline's crew-rostering system: a stage-gated vendor integration alongside two in-house scrum…|
|`pmp-p3-134`|people-3|single|predictive|5|On a design-and-build contract for a €45 million water treatment works, the design and site teams report to different directors…|
|`pmp-p3-135`|people-3|single|agile|4|Your team maintains a bank's mortgage-application service. For four sprints nobody except two senior developers has picked up…|
|`pmp-p3-136`|people-3|multi|predictive|4|You manage the design of a road bridge for a county council. A structural engineer by background, you have been quietly…|
|`pmp-p3-137`|people-3|matching|hybrid|3|Prompts: Engineers in Seoul rarely raise objectio… / A developer in Pune answers 'that may be… / A major release has been scheduled for t…|
|`pmp-p3-138`|people-3|pulldown|agile|3|Prompts: The team must decide how out-of-hours on… / Ticket sales have failed during a live o… / The team needs an indexing approach for…|
|`pmp-p3-139`|people-3|fillblank|predictive|3|Your five-engineer team on a gas network upgrade gains two new engineers next month. Each newcomer is expected to deliver 25%,…|
|`pmp-p3-140`|people-3|hotspot|hybrid|2|The diagram maps a fortnight of design decisions on your hybrid team building a ferry operator's booking system. Each box shows a…|
|`pmp-p3-1`|pmp-lectures-1-11|single|predictive|3|A project manager is building a project team. All of the project team members have worked together on previous projects. One of…|
|`pmp-p3-2`|pmp-lectures-1-11|single|agile|2|A project manager for a new project is tasked with applying agile practices to improve team performance.|
|`pmp-p3-3`|pmp-lectures-1-11|single|agile|3|A project manager has a newly formed team that includes junior members who have not worked on agile projects before. The product…|
|`pmp-p3-4`|pmp-lectures-1-11|single|agile|2|A newly appointed product owner is not sure how to prioritize product specifications and mentions this challenge to the project…|
|`pmp-p3-5`|pmp-lectures-1-11|single|predictive|3|The project scope statement is approved by the project sponsor. The team members would like to modify the work breakdown…|
|`pmp-p3-6`|pmp-lectures-1-11|single|agile|2|An agile project team member is working on a user story that is overdue. This delay is causing the team to miss sprint goals for…|
|`pmp-p3-7`|pmp-lectures-1-11|single|predictive|3|A project manager is assigned to an ongoing project with virtual team members from different countries who are working remotely…|
|`pmp-p3-8`|pmp-lectures-1-11|single|predictive|3|During a one-on-one session, a new team member shared their frustration regarding their inability to successfully complete an…|
|`pmp-p3-9`|pmp-lectures-1-11|single|agile|3|A project team adopted agile practices to meet the business need to develop an application. One of the senior team members…|
|`pmp-p3-10`|pmp-lectures-1-11|single|hybrid|4|A project manager has been assigned to a project that is currently late and over budget. The project uses a predictive approach…|
|`pmp-p3-11`|pmp-lectures-1-11|single|predictive|3|An extremely talented and results-oriented team member is suddenly struggling with a personal problem. Due to this, there is a…|
|`pmp-p3-12`|pmp-lectures-1-11|single|agile|3|A new agile team is forming and the project manager notices that the team is struggling in many areas. The team is experiencing…|
|`pmp-p3-13`|pmp-lectures-1-11|single|predictive|2|A project team has just reached a project milestone successfully. The project manager noticed that the team performed extremely…|
|`pmp-p3-14`|pmp-lectures-1-11|single|agile|3|A project manager oversees an agile project with an experienced project team. The project manager does not typically attend the…|
|`pmp-p3-201`|pmp-mocks-1-10|single|hybrid|3|A highly skilled subject matter expert (SME) is engaged in a government project where approximately 30% of the deliverables will…|
|`pmp-p3-202`|pmp-mocks-1-10|single|predictive|3|The lead sales manager and team are unable to submit a project status report on time for the new sales report integration…|
|`pmp-p3-203`|pmp-mocks-1-10|single|agile|3|A midsize project has the objective of developing an innovative product with changing requirements. Several team members are…|
|`pmp-p3-204`|pmp-mocks-1-10|single|hybrid|2|A project manager used to work on predictive projects and now has started working on a project using a hybrid approach.|
|`pmp-p3-205`|pmp-mocks-1-10|single|predictive|2|A project manager believes that the project team could benefit from development activities to improve project performance and…|
|`pmp-p3-206`|pmp-mocks-1-10|single|agile|2|Due to a lack of locally skilled resources, a company employs an internationally dispersed project team that will work virtually.…|
|`pmp-p3-207`|pmp-mocks-1-10|single|predictive|2|A project manager notices that team members are not interacting with each other and believes that the lack of interaction is…|
|`pmp-p3-208`|pmp-mocks-1-10|single|predictive|2|The project manager of a large team notices team members are not interacting together, which is impacting project performance and…|
|`pmp-p3-209`|pmp-mocks-1-10|single|predictive|3|A project is in the implementation phase. A new project manager discovers that the team members have no previous experience with…|
|`pmp-p3-210`|pmp-mocks-1-10|single|predictive|2|A project manager observes that team members are not collaborating or being proactive.|
|`pmp-p3-211`|pmp-mocks-1-10|single|predictive|2|A project manager realizes that the team needs training on a specific technology that is required in the project.|
|`pmp-p3-212`|pmp-mocks-1-10|single|agile|2|A team member who is unfamiliar with the subject matter is struggling to complete a complex deliverable.|
|`pmp-p3-213`|pmp-mocks-1-10|single|predictive|3|A project manager is assembling a team with a mix of new and experienced team members. The project manager finds out that there…|
|`pmp-p3-214`|pmp-mocks-1-10|single|agile|2|A project manager is not sure if the team has the critical skills needed to implement a key feature of an upcoming deliverable.|
|`pmp-p3-215`|pmp-mocks-1-10|single|agile|2|A project manager notices that conflicts between team members are occurring in every sprint. After an investigation, the project…|
|`pmp-p3-216`|pmp-mocks-1-10|single|agile|2|A remote project team consists of members from two countries. One of the teams is expecting to have three days off to celebrate a…|
|`pmp-p3-217`|pmp-mocks-1-10|single|agile|3|Members of an agile team are struggling. While there is trust among the team members, the majority of team decisions are made by…|
|`pmp-p3-218`|pmp-mocks-1-10|single|hybrid|2|In the early stages of team formation, a project manager is unable to focus on the main tasks because team members are constantly…|
|`pmp-p3-219`|pmp-mocks-1-10|single|predictive|2|At the first meeting about a road construction project, the project manager reminds the team of the importance of observing all…|
|`pmp-p3-220`|pmp-mocks-1-10|single|hybrid|1|A project manager is setting expectations with a team that is delivering a hybrid project.|
|`pmp-p3-221`|pmp-mocks-1-10|single|predictive|2|A senior team member was very influential and played a crucial role in past projects. In this new project, the senior member no…|
|`pmp-p3-222`|pmp-mocks-1-10|single|hybrid|3|A project manager is leading a project using a hybrid approach. The project team began the project with high morale and…|
|`pmp-p3-223`|pmp-mocks-1-10|single|agile|2|Working in a continuous delivery environment, a project leader notices that one of the key team members is experiencing low…|
|`pmp-p3-224`|pmp-mocks-1-10|single|predictive|2|During project execution, the project manager realizes that a team member is not completing tasks in accordance with the project…|
|`pmp-p3-225`|pmp-mocks-1-10|single|hybrid|1|A project team is working on a regulatory application that has a fixed duration for delivery. The team is using a hybrid approach…|
|`pmp-p3-226`|pmp-mocks-1-10|single|agile|2|A new project manager is assigned to lead an agile project. The project manager wants to use motivation to encourage the team to…|
|`pmp-p3-227`|pmp-mocks-1-10|single|agile|3|A project manager leads two highly motivated teams who typically work on 3-4 concurrent short projects with tight deadlines.…|
|`pmp-p3-228`|pmp-mocks-1-10|single|predictive|2|A new member joined a project during the execution phase. The work requires strong collaboration with other team members.|
|`pmp-p3-229`|pmp-mocks-1-10|single|predictive|2|A project manager begins to notice disengaged members arriving late, not delivering on time, not attending meetings, and…|
|`pmp-p3-230`|pmp-mocks-1-10|single|agile|3|A project manager is leading a Scrum team. During Business Acceptance Testing (BAT), the client rejects the deliverable due to…|
|`pmp-p3-231`|pmp-mocks-1-10|single|predictive|3|During the execution phase of a project, a new project manager is assigned to replace the previous project manager. As the…|
|`pmp-p3-232`|pmp-mocks-1-10|single|hybrid|3|A team is assembled for a new project that will be using a predictive approach through requirements gathering and an agile…|
|`pmp-p3-233`|pmp-mocks-1-10|single|predictive|2|A project manager has been on a complex project that began eight months ago. The project manager has a quality team that has been…|
|`pmp-p3-234`|pmp-mocks-1-10|single|agile|1|An organization has decided to implement agile and a project manager with Scrum Master has been selected to coach the project…|
|`pmp-p3-235`|pmp-mocks-1-10|single|predictive|2|During the execution phase of your project, a Resource Manager informed you that one of the experts allocated to your project had…|
|`pmp-p3-236`|pmp-mocks-1-10|single|predictive|2|During project execution, a team member made a mistake resulting in a 1-week delay to the project schedule.|
|`pmp-p3-237`|pmp-mocks-1-10|single|predictive|2|The project manager for a complex project discovers that a certain team member has missed several targets.|
|`pmp-p3-238`|pmp-mocks-1-10|single|predictive|2|A project leader noticed that a new team member is demotivated and always arrives late to work, causing a delay in the…|
|`pmp-p3-239`|pmp-mocks-1-10|single|predictive|2|A project manager is assigned to an existing project, replacing the previous project manager. Soon after joining the project, the…|
|`pmp-p3-240`|pmp-mocks-1-10|single|predictive|3|A project manager must make decisions daily, regardless of the decision's level of importance or criticality.|
|`pmp-p3-241`|pmp-mocks-1-10|multi|hybrid|3|A project manager wants to develop their own leadership capability during a long project. Select three ways to improve and…|
|`pmp-p3-242`|pmp-mocks-1-10|single|hybrid|2|A project manager is managing a hybrid project. A stakeholder has directly contacted a team member and has requested a change.…|
|`pmp-p3-243`|pmp-mocks-1-10|single|hybrid|3|A project manager is managing a project with a hybrid approach and has requested a senior team member to perform a quantitative…|
|`pmp-p3-244`|pmp-mocks-1-10|single|hybrid|2|A newly formed project team features members from several countries and continents, with significant age differences and a…|
|`pmp-p3-245`|pmp-mocks-1-10|single|hybrid|1|A project manager engaged in the delivery of a global project is in the process of recruiting a project team.|
|`pmp-p3-246`|pmp-mocks-1-10|single|predictive|2|Some team members assigned to a new technology project expressed their lack of knowledge about the technology and the challenges…|
|`pmp-p3-247`|pmp-mocks-1-10|single|predictive|2|A project manager is in a matrix organization environment. The project manager notices the team is not working well together,…|
|`pmp-p3-248`|pmp-mocks-1-10|single|hybrid|3|A team member received a request from a stakeholder to conduct a quantitative risk analysis. The team member is not certain…|
|`pmp-p3-249`|pmp-mocks-1-10|single|agile|3|In the middle of a project incremental delivery, due to a change in the technology, a team member informed the project leader…|
|`pmp-p3-250`|pmp-mocks-1-10|single|agile|1|A product owner has requested to incorporate embedded functionality for the patient portal in the next sprint. The team decided…|
|`pmp-p3-251`|pmp-mocks-1-10|single|predictive|2|A project team just completed a major milestone and is now on track to deliver the project ahead of the due date. The project…|
|`pmp-p3-252`|pmp-mocks-1-10|single|predictive|2|A project manager wants to motivate their team members and improve project performance.|
|`pmp-p3-253`|pmp-mocks-1-10|multi|predictive|3|An internal project has delivered its scope on time, within budget, and with the expected quality. The client was satisfied with…|
|`pmp-p3-254`|pmp-mocks-1-10|single|agile|2|An experienced project manager is managing a multi-million dollar agile project. The project manager communicates the ground…|
|`pmp-p3-255`|pmp-mocks-1-10|single|agile|3|Since the beginning of a project, the product owner keeps asking during ceremonies about the budget spent for each product…|
|`pmp-p3-256`|pmp-mocks-1-10|single|agile|2|A project team recently completed a long-term project using a predictive approach. The team then started a new project with the…|
|`pmp-p3-257`|pmp-mocks-1-10|single|hybrid|3|A project is being executed. The project's life cycle is defined as predictive; however, a major deliverable will be handed over…|
|`pmp-p3-258`|pmp-mocks-1-10|single|predictive|2|At which stage in the Tuckman team development model should the project manager put the most focus on increasing team motivation?|
|`pmp-p3-259`|pmp-mocks-1-10|single|agile|2|A team member approaches the project manager immediately after the end of the project meeting about his need for help to complete…|
|`pmp-p3-260`|pmp-mocks-1-10|single|hybrid|2|A project team often asks questions about the rationale for their work and how the client perceives their progress and…|
|`pmp-p3-261`|pmp-mocks-1-10|single|predictive|3|Project manager B has been assigned to take over a project from Project Manager A. Project Manager A informs Project Manager B…|
|`pmp-p3-262`|pmp-mocks-1-10|single|hybrid|3|A project manager with a directive behavioral orientation is leading a virtual team of software developers. The project manager…|
|`pmp-p3-263`|pmp-mocks-1-10|single|agile|2|A project manager is working with a Scrum team that is continually missing deadlines. The steering committee is concerned about…|
|`pmp-p3-264`|pmp-mocks-1-10|single|predictive|2|A lengthy, ongoing project exhausts a project team. Due to deadline issues, the project manager cannot extend the project…|
|`pmp-p3-265`|pmp-mocks-1-10|single|agile|3|The stakeholders and project team working on an agile project meet with the project manager at the end of an iteration. The…|
|`pmp-p3-266`|pmp-mocks-1-10|single|predictive|2|A team lead is assigned to an ongoing project. The team members have been working together for a while.|
|`pmp-p3-267`|pmp-mocks-1-10|single|predictive|2|A project manager wants to establish an incentive and rewards strategy for the project team.|
|`pmp-p3-268`|pmp-mocks-1-10|single|agile|2|A team member is working on a critical task for a project, and approaches the project manager immediately before the start of a…|
|`pmp-p3-269`|pmp-mocks-1-10|single|agile|3|During the fourth iteration of an agile project, the customer requests a technical change to the development of the project…|
|`pmp-p3-270`|pmp-mocks-1-10|single|agile|2|A new college graduate with a degree in computer science was recently hired by an organization. The new employee has been…|
|`pmp-p3-271`|pmp-mocks-1-10|single|agile|2|A project manager is leading a daily standup meeting when a team member requests that other team members take on some of their…|
|`pmp-p3-272`|pmp-mocks-1-10|multi|agile|3|(Choose 2).|
|`pmp-p3-273`|pmp-mocks-1-10|single|agile|2|A software developer who is working on a critical component has to take emergency leave from the project. Due to time…|
|`pmp-p3-274`|pmp-mocks-1-10|single|predictive|2|Team members express frustration because their functional manager does not listen to their opinions. They seem reluctant to…|
|`pmp-p3-275`|pmp-mocks-1-10|single|predictive|2|A project is in the execution phase when the project manager needs to assign new resources to the existing project team.|
|`pmp-p3-276`|pmp-mocks-1-10|single|predictive|1|In which stage of Tuckman's stages of team development do the team members begin to resolve conflicts effectively?|
|`pmp-p3-277`|pmp-mocks-1-10|single|agile|2|A project manager is managing an agile project where the team is regularly missing user story due dates.|
|`pmp-p3-278`|pmp-mocks-1-10|single|predictive|2|A project manager is committed to the success of a multinational project with tight deadlines. A new team member is continually…|
|`pmp-p3-279`|pmp-mocks-1-10|single|agile|2|A project manager leading an agile project wants to motivate the project team to improve performance.|
|`pmp-p3-280`|pmp-mocks-1-10|single|predictive|2|A project team is regularly missing milestones. Stakeholders are concerned that the project will miss the deliverable due date.…|
|`pmp-p3-281`|pmp-mocks-1-10|single|agile|3|Two agile teams are collaborating on a project. Team A's output is not complete enough for Team B to integrate into their backlog.|
|`pmp-p3-282`|pmp-mocks-1-10|single|predictive|2|A project manager notices schedule performance issues due to the expertise levels of project resources. Some experienced…|
|`pmp-p3-283`|pmp-mocks-1-10|single|hybrid|2|A hybrid project has the following characteristics: It is in the early stages. It has a brand new team. The project manager will…|
|`pmp-p3-284`|pmp-mocks-1-10|single|hybrid|3|A project is using a hybrid approach and team performance is consistently excellent during the predictive stages. During the…|
|`pmp-p3-285`|pmp-mocks-1-10|single|predictive|2|A goal has been set at four months for a project team to distribute a product. Unfortunately, it will be difficult to adhere to…|
|`pmp-p3-286`|pmp-mocks-1-10|single|agile|2|Members from an agile team have been consistently tardy for stand-up meetings and are not meeting project target dates. The…|
|`pmp-p3-287`|pmp-mocks-1-10|single|predictive|2|A project's deliverables due date is approaching, but one team member's performance is negatively impacting the schedule.|
|`pmp-p3-288`|pmp-mocks-1-10|single|predictive|2|A project manager observed that a team member is becoming increasingly disengaged from an ongoing project.|
|`pmp-p3-289`|pmp-mocks-1-10|single|predictive|2|A project manager notices that one team member seems increasingly unhappy during team meetings. The team member is not making…|
|`pmp-p3-290`|pmp-mocks-1-10|single|predictive|2|Early in a project, the project manager realizes that one of the key team members is not prepared to perform the assigned task.|
|`pmp-p3-291`|pmp-mocks-1-10|single|predictive|2|A project manager provided video-based training for new software but several team members reported feeling unprepared to use it.|
|`pmp-p3-292`|pmp-mocks-1-10|single|agile|2|A new project manager joins an existing team. The project manager is made aware that there are strong personalities on the team,…|
|`pmp-p3-293`|pmp-mocks-1-10|single|predictive|2|A project manager's team has been efficiently delivering expected results in an environment where budget and scope are fixed.…|
|`pmp-p3-294`|pmp-mocks-1-10|single|agile|2|In an agile project initiation phase, a senior team member suggests using a new documentation tool based on experience on a…|
|`pmp-p3-295`|pmp-mocks-1-10|single|agile|3|An agile project was scaled from the feasibility study phase to the implementation phase. Due to time constraints, the same team…|
|`pmp-p3-296`|pmp-mocks-1-10|single|hybrid|2|A project is in the execution phase when new team members are onboarded.|
|`pmp-p3-297`|pmp-mocks-1-10|single|predictive|2|A project manager recently took over a project from a project manager who left the company. The new project manager is aware that…|
|`pmp-p3-298`|pmp-mocks-1-10|single|agile|3|A project manager works in an organization where the culture encourages virtual teams. New challenges arise periodically in this…|
|`pmp-p3-299`|pmp-mocks-1-10|single|agile|3|A project manager observes that team members are in a low mood after demos. Feedback from stakeholders is constantly causing…|
|`pmp-p3-300`|pmp-mocks-1-10|multi|hybrid|3|A large transformation project has moved its project management office (PMO) to a virtual environment. There have been…|
|`pmp-p3-301`|pmp-mocks-1-10|single|hybrid|2|A project manager is working on a global project with team members in several different countries. To manage communications, the…|
|`pmp-p3-302`|pmp-mocks-1-10|single|predictive|2|During the execution phase of a project, the project manager is reassigned to another project. A new project manager takes over…|
|`pmp-p3-303`|pmp-mocks-1-10|single|agile|2|A project team is working on a complex technology project. A competitor introduces a product with a new feature, forcing the team…|
|`pmp-p3-304`|pmp-mocks-1-10|single|predictive|2|Regular check-ins with the delivery team reveal low morale and a lack of efficiency.|
|`pmp-p3-305`|pmp-mocks-1-10|single|predictive|2|A team member could not be reached by email or phone during the week that they were absent. The team member had not notified…|
|`pmp-p3-306`|pmp-mocks-1-10|multi|predictive|3|A project manager is preparing to start a project and notices that the project team is having difficulties communicating and…|
|`pmp-p3-307`|pmp-mocks-1-10|single|agile|2|On an agile project, a team member has been taking smaller, easier user stories from the backlog, not those with the highest…|
|`pmp-p3-308`|pmp-mocks-1-10|multi|hybrid|2|A project manager is beginning a new project and is starting to form the project team. The project manager has not worked with…|
|`pmp-p3-309`|pmp-mocks-1-10|single|agile|2|In the middle of a release, the project manager is replaced. Feedback provided by the team to the new project manager hinted at…|
|`pmp-p3-310`|pmp-mocks-1-10|single|agile|2|A software delivery team at a large bank is frustrated by the documentation requirements put in place by the governance…|
|`pmp-p3-311`|pmp-mocks-1-10|single|agile|1|A company is implementing an agile approach for a new project.|
|`pmp-p3-312`|pmp-mocks-1-10|single|predictive|2|A project manager notices that a critical team member has lost interest in the project. The team member's lack of interest has…|
|`pmp-p3-313`|pmp-mocks-1-10|single|agile|2|An agile project team has not completed the tasks within the current iteration. During a meeting, the project sponsor gives the…|
|`pmp-p3-314`|pmp-mocks-1-10|single|agile|2|A developer is expressing concerns about the time spent on daily standups, updating stories, creating tasks, and reviewing…|
|`pmp-p3-315`|pmp-mocks-1-10|single|predictive|3|During a weekly team meeting, the project manager reviewed task progress and found that a senior team member did not follow the…|
|`pmp-p3-316`|pmp-mocks-1-10|single|hybrid|3|In the middle of a software development project following a predictive lifecycle, a significant portion of the software is set…|
|`pmp-p3-317`|pmp-mocks-1-10|single|hybrid|2|During routine progress update sessions for a multinational project, a project manager notes that one of the key technical…|
|`pmp-p3-318`|pmp-mocks-1-10|single|agile|2|The goal on a project is to foster a culture of cooperation that drives the team towards exceptional performance. Following a…|
|`pmp-p3-319`|pmp-mocks-1-10|single|agile|2|In a routine progress check, an agile project manager notices that a lead analyst is habitually passing off less complex analysis…|
|`pmp-p3-320`|pmp-mocks-1-10|single|agile|2|During the second sprint of a software development project, it's observed that one team member frequently dictates scrum…|
|`pmp-p3-321`|pmp-mocks-1-10|single|predictive|2|As the deadline for a critical project approaches, the project manager notices a decrease in team enthusiasm, with several…|
|`pmp-p3-322`|pmp-mocks-1-10|single|hybrid|2|In leading a complex software development project with team members spread across diverse geographical locations, the project…|
|`pmp-p3-323`|pmp-mocks-1-10|single|predictive|2|Following a corporate restructuring, the project manager notices that the newly assembled team is having difficulty adapting to…|
|`pmp-p3-324`|pmp-mocks-1-10|single|predictive|2|After a project milestone, a team member voices concerns over the lack of detailed feedback regarding their project…|
|`pmp-p3-325`|pmp-mocks-1-10|single|agile|1|You have been working on a project where the manager leads in a dictatorship style. Over time, the team have stopped making…|
|`pmp-p3-326`|pmp-mocks-1-10|single|predictive|2|Team members lack enthusiasm for participating in essential project planning sessions, with many arriving late or not…|
|`pmp-p3-327`|pmp-mocks-1-10|single|agile|2|Discovering that one project team member feels their efforts are undervalued compared to others, leading to a decline in their…|
|`pmp-p3-328`|pmp-mocks-1-10|multi|agile|3|During a critical phase of a healthcare software implementation project, the project manager realizes that the project team is…|
|`pmp-p3-329`|pmp-mocks-1-10|single|predictive|1|Following a critical review session of a healthcare software project, a project manager notes that the output from one of the…|
|`pmp-p3-330`|pmp-mocks-1-10|single|predictive|2|In the late stages of launching a marketing campaign project, it's identified that a critical piece of content is not ready…|
|`pmp-p3-331`|pmp-mocks-1-10|single|predictive|1|Near the completion of an extensive software development project, it's noted that one developer's innovative solutions have…|
|`pmp-p3-332`|pmp-mocks-1-10|single|predictive|3|In the midst of executing a high-stakes software development project, several unexpected software bugs and team miscommunications…|
|`pmp-p3-333`|pmp-mocks-1-10|single|predictive|1|You are managing a project team mostly made up of men. Recently Jane was hired for her high expertise in the brands that make up…|
|`pmp-p3-334`|pmp-mocks-1-10|single|agile|3|Your agile project team has only recently formed. The product is still unclear, and there have been disagreements and minor…|
|`pmp-p3-335`|pmp-mocks-1-10|single|predictive|2|Your project team has started to find their rhythm, yet you notice that they seem disengaged in their work and are dragging their…|
|`pmp-p3-336`|pmp-mocks-1-10|single|predictive|1|Your project team came together from different parts of the organisation, and were introduced formally to each other. Now you…|
|`pmp-p3-337`|pmp-mocks-1-10|single|agile|2|You are working as an Agile project manager in a new project team. Many project team members are despondent, and do not show up…|
|`pmp-p3-338`|pmp-mocks-1-10|single|agile|2|Your Agile team leader works through servant leadership to the team, and has built a safe environment for disagreement.|
|`pmp-p3-339`|pmp-mocks-1-10|single|agile|2|New projects go through the Tuckman Model of forming, storming, norming, and performing. You have just taken the lead of a new…|
|`pmp-p3-340`|pmp-mocks-1-10|single|predictive|1|You have been working on a large waterfall project for some time, and one of the business analysts working with you has shown…|
|`pmp-p3-341`|pmp-mocks-1-10|single|predictive|1|You are working in an organisation where a large change is happening to multiple departments. You have been asked to manage this…|
|`pmp-p3-342`|pmp-mocks-1-10|single|predictive|2|You have started as a junior project manager on an internal process improvement project within your existing team. Your program…|
|`pmp-p3-343`|pmp-mocks-1-10|single|predictive|1|In the last few months, your project team have had a large amount of conflict, finding their role and place within the team. They…|
|`pmp-p3-344`|pmp-mocks-1-10|single|predictive|3|The project management office is looking to you for help in hiring and coaching new project managers, and they ask you to…|
|`pmp-p3-345`|pmp-mocks-1-10|single|agile|2|You are leading an Agile team that has members in various locations around the country. You have arranged daily stand-ups via…|
|`pmp-p3-346`|pmp-mocks-1-10|single|agile|1|The Agile manifesto has twelve clarifying principles added to it to help projects and teams understand the Agile values.|
|`pmp-p3-347`|pmp-mocks-1-10|single|agile|2|You are working on the requirements for the next iteration and a decision needs to be made by the team. The product owner…|
|`pmp-p3-348`|pmp-mocks-1-10|single|agile|2|You have just taken over a project that is behind schedule. The previous Project Manager was brought on during a company crisis,…|
|`pmp-p3-349`|pmp-mocks-1-10|single|hybrid|2|The project you have taken over recently was behind schedule. It has seen some improvement with some minor releases, but the team…|
|`pmp-p3-350`|pmp-mocks-1-10|single|hybrid|2|You are working as a Project Manager on a project where the Project Sponsor makes all decisions on project requirements and…|
|`pmp-p3-351`|pmp-mocks-1-10|single|predictive|1|You have just been allocated to a project where the team has been working together for the past six months. The Project…|
|`pmp-p3-352`|pmp-mocks-1-10|single|agile|2|The project you are working on is on schedule, but has been marred with conflict and politics for its duration so far. You ask…|
