# Question ledger — people-4 · Stakeholder engagement

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

### people-4 · Stakeholder engagement — 153 items

*Identify who matters, analyse them, and tailor how you work with each.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p4-001`|core|multi|agile|3|Your sprint reviews are attended by the product owner and nobody else. Three business units will use the product at launch, and…|
|`pmp-p4-002`|core|single|predictive|2|You have identified twenty-eight stakeholders on a regulatory reporting project with a filing deadline in nine months, spread…|
|`pmp-p4-003`|core|single|hybrid|4|A senior operations manager with no formal authority over your project has begun criticising it in forums you are not invited to.…|
|`pmp-p4-004`|core|multi|predictive|3|A regulatory reporting project has a general counsel who holds an effective veto at the sign-off gate but has attended none of…|
|`pmp-p4-005`|core|single|predictive|3|A department head has attended every governance meeting for your systems replacement and has never objected to anything. Your…|
|`pmp-p4-006`|core|multi|hybrid|3|Your programme spans offices in three time zones. The team nearest you is well informed and shapes most decisions. The other two…|
|`pmp-p4-007`|core|single|agile|4|Your product owner accepts work at each review and then, two or three sprints later, asks for the same feature to be reworked…|
|`pmp-p4-008`|core|single|predictive|3|Four weeks from go-live on a payroll replacement, a union representative contacts you to say the change alters how overtime is…|
|`pmp-p4-009`|core|matching|hybrid|3|Prompts: A regulator who cannot influence day-to-… / A department head whose staff must adopt… / An IT security lead who has raised the s…|
|`pmp-p4-015`|core|single|agile|3|Your agile team is building a duty-calculation engine for a freight forwarder. Developers message the firm's three customs…|
|`pmp-p4-011`|core|hotspot|predictive|3|The diagram shows the sequence your organisation follows when working with stakeholders on a new project. A regulator who was…|
|`pmp-p4-012`|core|single|hybrid|3|On a hybrid finance transformation, a regional director with no formal role on the programme has twice persuaded the steering…|
|`pmp-p4-013`|core|single|predictive|2|Your project will widen a road through a residential area. A residents' association has formed, has begun writing to local…|
|`pmp-p4-014`|core|multi|agile|3|The operations director is your most significant stakeholder and has sent a junior analyst to every review for the past two…|
|`pmp-p4-061`|core|graphic|predictive|3|The power and interest grid for your reservoir dam safety upgrade was drawn at the start of the project. This week the regional…|
|`pmp-case3-005`|case-03|single|hybrid|3|Dr Helen Park, the chief biomedical scientist at Site C, was not part of the requirements workshops. After a demonstration she…|
|`pmp-p4-101`|people-4|single|predictive|1|You are managing a £3.2 million refurbishment of a regional museum's galleries. Your stakeholder engagement plan schedules a…|
|`pmp-p4-102`|people-4|single|predictive|3|On a rail signalling upgrade, the main installation contractor's new engagement lead asks you for a copy of your stakeholder…|
|`pmp-p4-103`|people-4|single|agile|4|Your team is building a stock-ordering app for 640 bakery franchisees. Because the whole network cannot attend sprint reviews,…|
|`pmp-p4-104`|people-4|single|hybrid|5|Your organisation is developing a telematics service for its tractors, with the hardware on a predictive plan and the software…|
|`pmp-p4-105`|people-4|single|predictive|2|You are replacing the laboratory information system at a pharmaceutical quality-control site. The lab manager is the only person…|
|`pmp-p4-106`|people-4|single|agile|3|A credit union's agile team is rebuilding its loan application journey. The compliance officer has started insisting that every…|
|`pmp-p4-107`|people-4|single|predictive|3|You are managing a £14 million new-build secondary school. Your engagement plan promised the heads of science, art and design…|
|`pmp-p4-108`|people-4|single|hybrid|4|Eight months into a council's housing repairs system replacement, your stakeholder register still records the head of tenant…|
|`pmp-p4-109`|people-4|single|agile|2|On a travel company's booking platform, the regional sales director has started attending the team's daily stand-up. Each morning…|
|`pmp-p4-110`|people-4|single|hybrid|3|You are planning consultation with academic staff for a university's new coursework submission system. Your engagement plan…|
|`pmp-p4-111`|people-4|single|agile|3|A logistics firm's agile team will demonstrate an automated shift-allocation feature at tomorrow's sprint review, which the…|
|`pmp-p4-112`|people-4|single|predictive|4|On a hospital pharmacy automation project, your business analyst has completed the stakeholder engagement assessment matrix for…|
|`pmp-p4-113`|people-4|multi|agile|3|Your company funds an open-source data-visualisation library, and you lead the agile team that maintains it. The next major…|
|`pmp-p4-114`|people-4|multi|predictive|4|You are managing the digitisation of a national archive's map collection on a predictive plan. Your engagement plan treats the…|
|`pmp-p4-115`|people-4|multi|hybrid|3|Six months into a hybrid programme moving a dental group's 40 practices to electronic patient records, the sponsor asks how you…|
|`pmp-p4-116`|people-4|matching|predictive|3|Prompts: You suspect there are affected groups in… / You need to be sure that every external… / A neighbouring council completed a simil…|
|`pmp-p4-117`|people-4|matching|agile|2|Prompts: The head of branch operations wants to t… / The marketing director wants a loyalty f… / The risk manager wants to see at any tim…|
|`pmp-p4-118`|people-4|pulldown|hybrid|3|Prompts: The airline operators' committee is asse… / The baggage handlers' shift manager is a… / The commercial director is assessed as l…|
|`pmp-p4-119`|people-4|fillblank|predictive|2|Your team prioritises stakeholders for a ferry-terminal expansion using a weighted score: power counts 60% and interest 40%, each…|
|`pmp-p4-120`|people-4|hotspot|agile|3|A charity's agile team is building a volunteer-scheduling app. The volunteer network coordinator attends every sprint review,…|
|`pmp-p4-121`|people-4|single|predictive|1|You are managing an ERP replacement for a food manufacturer. The warehouse manager asked for a bespoke pallet-labelling module,…|
|`pmp-p4-122`|people-4|single|predictive|3|You are managing a council's £6 million leisure centre rebuild. Your engagement plan includes a community open day next month at…|
|`pmp-p4-123`|people-4|single|predictive|4|You have taken over a €40 million warehouse automation project. In your first meeting with the employee forum, an advisory body…|
|`pmp-p4-124`|people-4|single|predictive|4|You are managing a £22 million water pipeline for a regional utility. The route crosses a farm whose owner is in a legal dispute…|
|`pmp-p4-125`|people-4|single|predictive|3|You are managing the relocation of a university's chemistry department to a new building. In a private conversation, a research…|
|`pmp-p4-126`|people-4|single|agile|2|Your agile team is building a journey-planning app for a rail operator. A sight-loss charity has joined as a stakeholder, and two…|
|`pmp-p4-127`|people-4|single|agile|3|An insurer's claims-portal team has become popular, and its fortnightly sprint review now draws about 45 people from across the…|
|`pmp-p4-128`|people-4|single|agile|3|Your agile team is building a reading-practice app for children aged seven to nine. Three primary schools have agreed to take…|
|`pmp-p4-129`|people-4|single|agile|5|A bank's agile data team has built a model that ranks suspected card-fraud cases for 60 investigators, replacing a first-in,…|
|`pmp-p4-130`|people-4|single|hybrid|4|On a hybrid programme replacing a port authority's vessel-booking system, the harbour master has raised objections at each of the…|
|`pmp-p4-131`|people-4|single|hybrid|3|Your hybrid programme is consolidating an insurer's three offices into one. The facilities director controls the floor space…|
|`pmp-p4-132`|people-4|single|agile|2|An online furniture retailer's agile team is building a returns-processing app. The head of customer returns is a key stakeholder…|
|`pmp-p4-133`|people-4|multi|agile|3|Your agile team is building a task-allocation app for a hospital's porters, commissioned by the hospital's estates manager. The…|
|`pmp-p4-134`|people-4|multi|predictive|4|You are managing the consolidation of a utility's four billing centres into two, a predictive project going live in nine months.…|
|`pmp-p4-135`|people-4|multi|hybrid|3|Your hybrid programme for a fashion retailer has finished building a new point-of-sale system with a core team of…|
|`pmp-p4-136`|people-4|matching|hybrid|3|Prompts: A non-executive director has questioned… / Site foremen doubt that filling in diari… / The firm's IT architect wants detail on…|
|`pmp-p4-137`|people-4|matching|predictive|2|Prompts: The fire safety officer, whose approval… / The external support desk that will take… / Residents' families, who have asked how…|
|`pmp-p4-138`|people-4|pulldown|hybrid|3|Prompts: At one site, managers agree with every p… / At another site, junior engineers will n… / Near a third site, the neighbouring comm…|
|`pmp-p4-139`|people-4|fillblank|predictive|2|You can give 20 hours a month to engagement on a water utility's smart-meter programme. The plan has monthly one-hour meetings…|
|`pmp-p4-140`|people-4|hotspot|agile|3|A bus operator's agile team is replacing paper concessionary passes with a phone app. A pensioners' forum has a formal right to…|
|`pmp-p4-1`|export-batch-1|single|predictive|4|A project manager conducted an analysis with relevant stakeholders to validate project alignment with organizational strategy and…|
|`pmp-p4-2`|export-batch-1|single|predictive|3|During the planning phase of a project, the project manager is having trouble identifying stakeholders. Without input from the…|
|`pmp-p4-3`|export-batch-1|single|predictive|3|A project manager has just finished a project charter and received approval from the project sponsor. The project manager then…|
|`pmp-p4-4`|export-batch-1|single|agile|3|A project manager receives an email shortly after the beginning of a sprint. It is from a key stakeholder who is unfamiliar with…|
|`pmp-p4-5`|export-batch-1|single|predictive|3|During the first few days of executing a six-week project, an impediment is discovered by the project team. The project manager…|
|`pmp-p4-6`|export-batch-1|single|predictive|3|A project manager has been assigned to a multimillion-dollar project already in execution. During a project status meeting, one…|
|`pmp-p4-7`|export-batch-1|single|predictive|3|A new product development project has been launched. The project sponsor has reviewed the results of the risk analysis as…|
|`pmp-p4-8`|export-batch-1|single|predictive|2|A project manager has recently been assigned to an existing multinational project and is not familiar with the key stakeholders.|
|`pmp-p4-9`|export-batch-1|single|predictive|2|A project manager is leading a project where a low-level stakeholder has just been promoted to a new position and has now become…|
|`pmp-p4-10`|export-batch-1|single|predictive|3|A company is implementing a new computerized system. During project execution, the leader of a department that is critical to…|
|`pmp-p4-11`|export-batch-1|single|agile|3|A key stakeholder is upset about the new features in the current iteration for the product. The key stakeholder was unavailable…|
|`pmp-p4-12`|export-batch-1|single|predictive|3|An external stakeholder has a significant impact on the project, as they have the ability to approve or reject changes to the…|
|`pmp-p4-13`|export-batch-1|single|predictive|3|A project manager is working on a project charter and discovers that two key stakeholders are transferring to other departments…|
|`pmp-p4-14`|export-batch-1|single|predictive|3|During project execution, a key stakeholder is replaced with a new team member who has approval authority. The new member states…|
|`pmp-p4-15`|export-batch-1|single|predictive|3|A five-year highway construction project is in its third year of execution when the project manager receives a complaint from a…|
|`pmp-p4-16`|export-batch-1|single|predictive|3|During a kick-off meeting with the project sponsor and various project members, it became clear that it was not necessary for…|
|`pmp-p4-17`|export-batch-1|single|predictive|3|During project implementation, a stakeholder is questioning the purpose of the project and wants to pause the project to assess a…|
|`pmp-p4-18`|export-batch-1|single|agile|3|A company is creating a new application for its users. The agile team completed the initiating stage and is performing iterations…|
|`pmp-p4-19`|export-batch-1|single|predictive|2|An experienced project manager is mentoring a new project manager assigned to their first project. As the new project manager…|
|`pmp-p4-201`|export-batch-2|single|predictive|3|A project manager in the execution phase of a project discovers several stakeholders identified at project initiation are no…|
|`pmp-p4-202`|export-batch-2|single|hybrid|3|An ongoing project has a high degree of change.|
|`pmp-p4-203`|export-batch-2|single|predictive|3|A project manager has been working on an internal project for the organization's project management office (PMO). The project was…|
|`pmp-p4-204`|export-batch-2|single|predictive|3|A project manager is in the execution phase of a project. A newly identified stakeholder continuously bypasses the project…|
|`pmp-p4-205`|export-batch-2|single|predictive|3|A project is in the planning phase and requires tremendous time and effort for data collection and analysis. The project sponsor…|
|`pmp-p4-206`|export-batch-2|single|agile|2|A senior stakeholder was invited to a sprint review meeting and asked a project team member to describe the final product that…|
|`pmp-p4-207`|export-batch-2|single|hybrid|3|A business environment has become unstable because a stakeholder has altered the project deliverables and outcomes in the middle…|
|`pmp-p4-208`|export-batch-2|single|predictive|2|A project stakeholder has been missing scheduled facilitation workshops and has delegated assigned tasks to another member of the…|
|`pmp-p4-209`|export-batch-2|single|predictive|2|A project manager is developing a project charter, but a key stakeholder is failing to provide information about the expected…|
|`pmp-p4-210`|export-batch-2|single|agile|3|A critical software development project has been in development for the past six sprints. A demonstration for stakeholders took…|
|`pmp-p4-211`|export-batch-2|single|predictive|2|A complex project has multiple participants. The project manager agrees with the team that they need to better understand the…|
|`pmp-p4-212`|export-batch-2|single|predictive|2|A key stakeholder refuses to sign off on a product because it does not meet the expectations outlined in the project scope.|
|`pmp-p4-213`|export-batch-2|single|predictive|3|While executing a project that is nearly behind schedule, a project manager must commission a new piece of equipment. Two local…|
|`pmp-p4-214`|export-batch-2|single|predictive|1|A project manager compiling a list of project stakeholders discovers an interested stakeholder with little to no authority.|
|`pmp-p4-215`|export-batch-2|single|predictive|2|During a project's execution, a previously unidentified stakeholder within the customer's organization requests a change that…|
|`pmp-p4-216`|export-batch-2|single|predictive|2|A new project manager joins a project during the delivery of a critical milestone. This project has multiple stakeholders, and…|
|`pmp-p4-217`|export-batch-2|single|predictive|2|A project manager must ensure stakeholder responsibilities are clear, concise, and promote successful completion while…|
|`pmp-p4-218`|export-batch-2|single|hybrid|3|A project manager is working on a hybrid project. The team performance has been suffering and multiple team members have…|
|`pmp-p4-219`|export-batch-2|single|agile|3|A key project stakeholder showed interest in the beginning of a complex agile project but has become less involved as the sprint…|
|`pmp-p4-220`|export-batch-2|single|predictive|2|A key stakeholder's input is essential to a project's success. However, the stakeholder is always late on critical project…|
|`pmp-p4-221`|export-batch-2|single|predictive|3|During the initial planning phase of a complex development project, a project team has identified several stakeholders, including…|
|`pmp-p4-222`|export-batch-2|single|hybrid|2|A project manager is managing a hybrid project. After attending an iteration review, a stakeholder tells the project manager that…|
|`pmp-p4-223`|export-batch-2|single|predictive|2|An executive stakeholder has crucial decision power on the execution and success of a project.|
|`pmp-p4-224`|export-batch-2|single|predictive|1|After stakeholder data and information collection is complete, an analysis of the collected data begins. The power/interest…|
|`pmp-p4-225`|export-batch-2|single|hybrid|2|A project manager is leading an environmental project and realizes that they missed an influential foreign stakeholder. After the…|
|`pmp-p4-226`|export-batch-2|single|predictive|1|A project manager is leading a project with seven key stakeholders.|
|`pmp-p4-227`|export-batch-2|single|hybrid|2|A project manager sends out requests for regular status meetings and daily standup meetings during the project setup phase.…|
|`pmp-p4-228`|export-batch-2|single|predictive|2|During a project steering committee meeting, the project manager reports that a formerly supportive key team member has become…|
|`pmp-p4-229`|export-batch-2|single|predictive|2|A project manager has just completed the initiation phase of a project and needs to identify and analyze the stakeholders. The…|
|`pmp-p4-230`|export-batch-2|single|agile|2|A long-term agile project has just reached its midway mark and a new project manager is appointed to take over. Additional to the…|
|`pmp-p4-231`|export-batch-2|single|hybrid|2|A project manager is leading a complex initiative with diverse stakeholders and must cultivate strong stakeholder relationships.…|
|`pmp-p4-232`|export-batch-2|single|predictive|2|A project manager has identified stakeholders for a software development project: the sales team will be the primary users of the…|
|`pmp-p4-233`|export-batch-2|single|predictive|2|A project is experiencing delays because cultural differences are causing misunderstandings among some stakeholders.|
|`pmp-p4-234`|export-batch-2|single|agile|3|A project manager for an important agile project has a key stakeholder who was actively involved at the beginning of the project.…|
|`pmp-p4-235`|export-batch-2|single|predictive|2|Midway through a project, the project manager identifies new stakeholders. Each of these new stakeholders plays a different…|
|`pmp-p4-236`|export-batch-2|single|predictive|1|A project is in development when a functional manager leaves the company and is no longer involved with the project.|
|`pmp-p4-237`|export-batch-2|single|predictive|1|A project manager in a global organization is responsible for a project involving six different companies. Midway through the…|
|`pmp-p4-238`|export-batch-2|single|predictive|2|A project manager is having difficulty managing stakeholders because the stakeholder register does not distinguish how aware of…|
|`pmp-p4-239`|export-batch-2|single|agile|2|A project manager is attempting to engage project stakeholders in a new project and has invited them to daily standup meetings.…|
|`pmp-p4-240`|export-batch-2|single|agile|2|An agile project that the current project manager has been managing for three years is replacing one of the stakeholders with one…|
|`pmp-p4-241`|export-batch-2|single|predictive|2|A project manager is leading a complex infrastructure project that is vital for the company's growth. During the project's…|
|`pmp-p4-242`|export-batch-2|single|agile|3|During the execution of a project, the finance team identified that they were not involved in building the business case for the…|
|`pmp-p4-243`|export-batch-2|single|agile|2|An agile project manager is leading a new software system development project that is in the user acceptance testing phase. The…|
|`pmp-p4-244`|export-batch-2|multi|hybrid|2|A large transformation project that requires cross-functional effort from various business teams is in progress. (Choose two)|
|`pmp-p4-245`|export-batch-2|single|agile|2|A director is acting as the product owner for a government software project. The director is repeatedly unable to engage with the…|
|`pmp-p4-246`|export-batch-2|multi|predictive|2|A project is close to completion when an unidentified and influential stakeholder appears and threatens to put the project in…|
|`pmp-p4-247`|export-batch-2|single|agile|1|At what point in an agile project should a project manager engage stakeholders?|
|`pmp-p4-248`|export-batch-2|single|predictive|2|A project manager is managing a construction project that is 40% through the execution phase. The project sponsor has asked the…|
|`pmp-p4-249`|export-batch-2|single|predictive|1|A project team is creating a stakeholder engagement assessment matrix to govern stakeholder management activities during project…|
|`pmp-p4-250`|export-batch-2|single|agile|2|A project manager is concerned about delivering the planned business value because the product owner is not engaged.|
|`pmp-p4-251`|export-batch-2|single|hybrid|3|Several items remain in the Kanban blocked column despite the team’s efforts to address them. The retrospective reveals that all…|
|`pmp-p4-252`|export-batch-2|single|predictive|2|A functional manager who is also a project stakeholder has time constraints and delegates project responsibilities to someone…|
|`pmp-p4-253`|export-batch-2|single|agile|3|An R&D executive, who is a project stakeholder, occasionally visits the agile team’s work area to give instructions about urgent…|
|`pmp-p4-254`|export-batch-2|single|predictive|2|A new project team is setting early priorities, and they plan to gain the customer's trust by delivering value as quickly as…|
|`pmp-p4-255`|export-batch-2|single|predictive|2|A project manager overseeing the launch of a new e-commerce platform discovers that the finance department has concerns about not…|
|`pmp-p4-256`|export-batch-2|single|predictive|3|A project manager is overseeing the construction of a new office building. A key stakeholder complains that they never received…|
|`pmp-p4-257`|export-batch-2|single|predictive|2|During a pivotal project phase, it was revealed that critical stakeholders did not attend a meeting focused on confirming the…|
|`pmp-p4-258`|export-batch-2|single|predictive|2|At the beginning of a new environmental conservation project, a key stakeholder expresses concerns over the project's approach,…|
|`pmp-p4-259`|export-batch-2|single|hybrid|2|In the development phase of an international public health project, a project manager faces skepticism from government partners…|
|`pmp-p4-260`|export-batch-2|single|predictive|2|As a project manager overseeing the development of a new line of eco-friendly packaging materials, you encounter pushback from…|
|`pmp-p4-261`|export-batch-2|single|predictive|3|Your stakeholders seem to be engaged, however there are frequent changes to scope and requirements, causing delays. Issues are…|
|`pmp-p4-262`|export-batch-2|single|predictive|2|A project manager is leading the development of a new public park in a densely populated urban area. Community leaders have…|
|`pmp-p4-263`|export-batch-2|single|predictive|2|A project manager is leading the development of a new public library. During the planning phase, it comes to light that the…|
|`pmp-p4-264`|export-batch-2|single|predictive|2|At the outset of an initiative to introduce smart city technologies, it's discovered that the input from a crucial environmental…|
|`pmp-p4-265`|export-batch-2|single|predictive|2|You are delivering a critical change and several key stakeholders in your organisation have added seemingly unnecessary steps for…|
|`pmp-p4-266`|export-batch-2|single|predictive|2|You are analysing your stakeholder through an impact over influence chart and have found more than 100 different groups of…|
|`pmp-p4-267`|export-batch-2|single|predictive|2|You are working on a change that includes stakeholders at all levels from front-line workers to senior executives. After…|
|`pmp-p4-268`|export-batch-2|single|predictive|2|The functional manager raises a concern that they have not been engaged correctly and the program is missing some key…|
|`pmp-p4-269`|export-batch-2|single|predictive|2|Your project has been in progress for the past six months. Someone you have not met before approaches you and says they have not…|
|`pmp-p4-270`|export-batch-2|single|predictive|2|You have been working with the stakeholders on your project for some time, and the communication has been getting more and more…|
|`pmp-p4-271`|export-batch-2|single|predictive|2|You have been working on a project in your organisation for some time. In the latest working group meeting, the project sponsor…|
|`pmp-p4-272`|export-batch-2|single|predictive|1|You are creating a high level project charter which has now been approved. The Project Sponsor expects you to get to work…|
|`pmp-p4-273`|export-batch-2|single|predictive|2|The project charter has been approved, you have identified the stakeholders you will be working with and who will be impacted. As…|
|`pmp-p4-274`|export-batch-2|single|predictive|2|Your project charter has been approved and your stakeholder analysis reveals multiple impacted highly paid executives with the…|
|`pmp-p4-275`|export-batch-2|single|agile|2|You are at the beginning of a new iteration and about to plan and decide what gets worked on in the upcoming sprint. You decide…|
|`pmp-p4-276`|export-batch-2|single|hybrid|3|During a retrospective, your team advise that a high ranking executive from another part of the business keep approaching them to…|
|`pmp-p4-277`|export-batch-2|single|predictive|1|You are working on a project to deliver a major system change to retail workers in a large department store. You have worked…|
|`pmp-p4-278`|export-batch-2|single|predictive|1|You are in the initiating stages of a project, and have been given an initial project team. Your team are gathering the project…|
