# Question ledger — business-5 · Risk

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
