# Question ledger — people-6 · Customer expectations

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

### people-6 · Customer expectations — 85 items

*Track what internal and external customers expect, and respond as it moves.*

|id|source|type|appr.|d|scenario|
|-|-|-|-|-|-|
|`pmp-p6-001`|core|single|hybrid|3|Halfway through a customer-facing implementation, the client's business has been reorganised and their new operations lead wants…|
|`pmp-p6-002`|core|single|agile|2|At a sprint review for an expenses tool, the customer representative says the new receipt-matching feature works exactly as…|
|`pmp-p6-003`|core|single|predictive|3|Halfway through a twelve-month customer portal build, your client's own market has shifted and their competitors have launched…|
|`pmp-p6-004`|core|single|agile|2|An internal operations team is the customer for a scheduling tool. For four consecutive sprints they have accepted the demo, then…|
|`pmp-p6-005`|core|single|agile|3|Six sprints into a customer portal, usage data from the beta shows that the feature the customer described as essential is barely…|
|`pmp-p6-006`|core|single|hybrid|4|Your customer's operations director escalates to your managing director, saying the delivered reporting module is unusable…|
|`pmp-p6-007`|core|multi|predictive|3|Your project's customer is an internal department that will run the system after handover. Their operations manager has been…|
|`pmp-p6-015`|core|single|predictive|2|Your firm is supplying four switchgear panels to a data-centre operator under a fixed-price contract that requires a factory…|
|`pmp-p6-016`|core|multi|agile|3|Your team develops a timetabling system for a further-education college, and the first release went live five weeks ago. The…|
|`pmp-p6-010`|core|multi|agile|3|Three sprints into an implementation, your new customer is increasingly unhappy. Reviewing the sales cycle, you find a pre-sales…|
|`pmp-p6-011`|core|single|predictive|2|Three months into a twelve-month customer portal build, your client's marketing head asks for a change to the visual design that…|
|`pmp-p6-012`|core|single|hybrid|4|A hybrid platform serves two internal customers. Claims processing needs the release in March to meet a regulatory commitment.…|
|`pmp-p6-013`|core|single|agile|2|Your team demonstrates every two weeks to a customer representative who has begun comparing each increment unfavourably with a…|
|`pmp-p6-014`|core|single|predictive|3|Two weeks before go-live on a system built for an internal department, the department head mentions in passing that she expects…|
|`pmp-case2-002`|case-02|single|agile|3|Older residents repeatedly struggle at the vehicle registration step in usability sessions. Malik wants to hold the release scope…|
|`pmp-case3-010`|case-03|single|hybrid|2|Clinicians on the wards have been told little about the cutover at Site A and expect turnaround times for test results to stay as…|
|`pmp-case5-005`|case-05|single|hybrid|3|Since the first centre moved onto the new platform, customers have been waiting longer on the phone and complaints have risen.…|
|`pmp-p6-101`|people-6|single|predictive|1|You are managing the refurbishment of a 120-room hotel under a fixed-price contract with an agreed specification. During a site…|
|`pmp-p6-102`|people-6|single|agile|2|Your team is building a stock-management app for a hospital pharmacy. The product owner comes from the hospital's IT department,…|
|`pmp-p6-103`|people-6|multi|predictive|3|You are managing a three-year bridge strengthening programme for a county council. At the annual review every milestone is on…|
|`pmp-p6-104`|people-6|single|predictive|2|Your agency is building a public information website for a government department under a contract requiring conformance with WCAG…|
|`pmp-p6-105`|people-6|single|agile|3|You lead an agile team building a claims app for an insurance broker. The broker's operations manager, who acts as product owner,…|
|`pmp-p6-106`|people-6|matching|predictive|3|Prompts: The extract rate measured at two fume cu… / The lab manager says she expected the sy… / The client asks for a third fume cupboar…|
|`pmp-p6-107`|people-6|single|predictive|3|You are delivering a bespoke packaging line to a food manufacturer for €2.4 million. Acceptance tests passed last month and the…|
|`pmp-p6-108`|people-6|single|agile|3|Your team is building an online banking feature for a building society. Its product owner has started insisting on approving a…|
|`pmp-p6-109`|people-6|single|predictive|3|You are managing the fit-out of three office floors for a law firm. The contract delegates to you authority to agree variations…|
|`pmp-p6-110`|people-6|fillblank|hybrid|3|After rolling out a new field-service system to a water utility, your team runs a six-week hypercare period. The utility expects…|
|`pmp-p6-111`|people-6|single|hybrid|4|Your hybrid team is delivering a loyalty app for a fashion retailer. The retailer's marketing lead has a national campaign…|
|`pmp-p6-112`|people-6|single|agile|5|You lead an agile team building a route-planning platform for a logistics firm. Its chief operating officer tells you she has…|
|`pmp-p6-113`|people-6|multi|agile|4|Your team is building a case-management tool for a utility's contact centre. Agents in the pilot group have started messaging…|
|`pmp-p6-114`|people-6|single|hybrid|1|Yesterday your team released an update to the online booking service of a chain of physiotherapy clinics. This morning the…|
|`pmp-p6-115`|people-6|pulldown|agile|2|Prompts: The head of libraries wants to know, spr… / Librarians say search must be 'fast', bu… / Branch staff keep asking about features…|
|`pmp-p6-116`|people-6|single|predictive|4|Your company is implementing a records system for a regional health authority. The contract, managed by the authority's contracts…|
|`pmp-p6-117`|people-6|hotspot|hybrid|3|A council is piloting self-service kiosks in its libraries. In interviews, no user mentioned printed loan receipts. When receipts…|
|`pmp-p6-118`|people-6|single|agile|4|Your team supports a university's student-records system and has moved from monthly to weekly releases, as the registry director…|
|`pmp-p6-119`|people-6|multi|predictive|3|Your consultancy is designing a flood defence wall for an environment agency under an agreed brief that covers capacity,…|
|`pmp-p6-120`|people-6|matching|hybrid|3|Prompts: Call-centre agents say the system 'feels… / Staff in 40 branches need to say which o… / The sales director and the service direc…|
|`pmp-p6-121`|people-6|single|predictive|1|Your software company is three months into implementing a patient-administration system for a hospital trust under a fixed-scope…|
|`pmp-p6-122`|people-6|single|agile|2|Your team is building a mobile inspection app for a housing association. At the last review the association's head of property…|
|`pmp-p6-123`|people-6|single|hybrid|2|Your hybrid team is replacing the online ticketing system for a science museum. To test the booking journey, the designer built a…|
|`pmp-p6-124`|people-6|single|predictive|3|Your firm has taken over a university's building-maintenance contract from a long-standing incumbent. The new contract requires…|
|`pmp-p6-125`|people-6|single|predictive|4|Two months after handing over a warehouse management system to a furniture manufacturer, its interface to a carrier's booking…|
|`pmp-p6-126`|people-6|single|agile|3|Your team builds features for an airport operator's passenger app. Stories accepted at each sprint review go into a release that…|
|`pmp-p6-127`|people-6|single|agile|3|You are the project manager for an agile team building an appointment app for a chain of veterinary practices. The product owner,…|
|`pmp-p6-128`|people-6|single|hybrid|4|Your team delivers quarterly releases of a booking system for a ferry operator. For the first two releases, as goodwill, your…|
|`pmp-p6-129`|people-6|single|agile|4|Your team builds a candidate-matching platform for a recruitment agency. At the end of each sprint the agency's head of…|
|`pmp-p6-130`|people-6|single|predictive|5|You are the internal project manager for a new vehicle-scheduling system at a regional bus company. The business case, owned by…|
|`pmp-p6-131`|people-6|single|hybrid|4|You are managing the refurbishment of a charity's head office on a £1.2 million budget. At the start, its chief executive said…|
|`pmp-p6-132`|people-6|single|predictive|2|Six weeks before migrating a parcel firm's warehouse system to a new data centre, you review the cutover plan with its site…|
|`pmp-p6-133`|people-6|multi|agile|3|Your team is building a training platform for a pharmaceutical company. Its learning and development director, the main customer…|
|`pmp-p6-134`|people-6|multi|agile|3|Your agile team works on a time-and-materials contract for a payments start-up, billed at €52,000 a month. After six months, the…|
|`pmp-p6-135`|people-6|multi|predictive|3|You are managing a £3.1 million extension to a secondary school, built while the school stays open. The contract allows noisy…|
|`pmp-p6-136`|people-6|matching|predictive|2|Prompts: The monthly payroll run for 18,000 staff… / Processing costs should fall by 30% with… / The finance director wants to hear withi…|
|`pmp-p6-137`|people-6|matching|hybrid|3|Prompts: Were tenants satisfied with how their in… / How much effort did tenants have to put… / Would tenants recommend the association'…|
|`pmp-p6-138`|people-6|pulldown|agile|3|Prompts: Midway through a sprint, the clubs' memb… / During a review, the membership lead poi… / The clubs' procurement officer asks for…|
|`pmp-p6-139`|people-6|fillblank|hybrid|3|An online grocer's operations director is about to sign a hosting contract offering 99.5% monthly availability. She says this…|
|`pmp-p6-140`|people-6|hotspot|predictive|3|You are delivering a warehouse automation project for a pharmaceutical distributor. The diagram is a timeline of the monthly…|
|`pmp-p6-1`|export-batch-1|single|agile|3|A company has hired a project manager to implement an adaptive project framework. The project has made some progress, but the…|
|`pmp-p6-2`|export-batch-1|single|agile|3|An agile team is developing a product with strict budget constraints. The customer is requesting a new independent feature be…|
|`pmp-p6-3`|export-batch-1|single|agile|3|Even though the customer is receiving sprint velocity and results reports from the project manager by email, escalations to the…|
|`pmp-p6-4`|export-batch-1|single|agile|3|A client has contracted a company to manage a large project in an agile environment with many subcontractors. The client is…|
|`pmp-p6-201`|export-batch-2|single|agile|2|A project manager is involved in a project where requirements are constantly evolving.|
|`pmp-p6-202`|export-batch-2|single|predictive|2|As part of a project deliverable, a project manager demonstrates how to use one of the products to a subset of a client's…|
|`pmp-p6-203`|export-batch-2|single|hybrid|2|In a hybrid project, the need for customer involvement throughout the project is not understood by the customer. As a…|
|`pmp-p6-204`|export-batch-2|single|hybrid|3|During one of the final incremental deliveries of a project, the end user informed the project leader that they are not satisfied…|
|`pmp-p6-205`|export-batch-2|single|predictive|2|A project has been closed and customer care needs to measure customer satisfaction with the deliverables.|
|`pmp-p6-206`|export-batch-2|single|agile|3|A customer is concerned that the current project deliverable will not fulfill stated project requirements.|
|`pmp-p6-207`|export-batch-2|single|agile|3|A complex technology project is in the execution phase. New technologies are added to the backlog based on customer requirements,…|
|`pmp-p6-208`|export-batch-2|single|agile|3|During a daily meeting, the project team raised an impediment that one of the business subject matter experts is frequently…|
|`pmp-p6-209`|export-batch-2|single|hybrid|3|The team informed the project leader that they are not sure of the stakeholder needs, despite the multiple collection…|
|`pmp-p6-210`|export-batch-2|single|predictive|2|An operational support project is running smoothly, but a customer consistently complains about a lack of visibility into the…|
|`pmp-p6-211`|export-batch-2|single|predictive|2|A customer requires knowing exactly the state of a product, the overall project, and the next steps planned for the next month…|
|`pmp-p6-212`|export-batch-2|single|predictive|2|Multiple stakeholders express dissatisfaction with a project deliverable.|
|`pmp-p6-213`|export-batch-2|single|agile|3|A project manager is assigned a project for a customer with a history of being demanding and unclear about requirements. The…|
|`pmp-p6-214`|export-batch-2|single|hybrid|2|A project manager is on a project with a very involved customer. The customer demands regular updates on the project, including…|
|`pmp-p6-215`|export-batch-2|single|predictive|3|During a meeting with stakeholders, the project manager received multiple concerns about the quality of the project’s…|
|`pmp-p6-216`|export-batch-2|single|agile|2|A project manager receives an email from a customer detailing reservations about the project deliverables.|
|`pmp-p6-217`|export-batch-2|single|agile|2|A customer is not satisfied with the delivered product, saying that it was not what they expected. The project manager is…|
|`pmp-p6-218`|export-batch-2|single|agile|3|A customer repeatedly complains, saying that a function developed by the development team does not seem to align with the…|
|`pmp-p6-219`|export-batch-2|single|hybrid|3|During a challenging shareholder meeting, the customer indicates that they will reject an iteration's deliverable.|
|`pmp-p6-220`|export-batch-2|single|predictive|2|A customer's resources have not completed the required training. The project manager has to consider the risk it poses to the…|
|`pmp-p6-221`|export-batch-2|single|predictive|2|A customer is performing a weekly visit to a project site to inspect the final stage of product development. The quality manager…|
|`pmp-p6-222`|export-batch-2|single|agile|2|During a one-year project, a team delivered everything as requested and defined by the product owner and met all of the sprint…|
|`pmp-p6-223`|export-batch-2|single|agile|2|While working on a long project with many changes, a project manager is informed by a team member that some users are…|
|`pmp-p6-224`|export-batch-2|multi|agile|3|A stakeholder on an agile project has attended the sprint demo where the team shows the minimum viable product (MVP) as defined…|
