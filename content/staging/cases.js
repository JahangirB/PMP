/* 05.3  Case studies. Five cases, 50 items. Replaces the single line
   `PMP.CASES      = [];` in §05.3 (keep the comment above it, or replace it
   with this one).

   Shape (checked by content/staging/validate.mjs; see README.md):
     id         "case-01" …, unique, never reused
     title      a short name, shown as "Case: <title>"
     approach   predictive | agile | hybrid, the case's own delivery approach
     setting    one line: organisation and situation, for lists and headers
     mix        { people, process, business } item counts, re-derived by the
                validator from the questions so it cannot drift
     passage[]  the background, one string per paragraph, 300–600 words in all
     exhibits[] { id, label, title, cols[], rows[][], note? } small tables the
                passage and items refer to as "Exhibit n"
     questions[] 8–12 items in the ordinary §05.2 schema (schema v2), ids
                "pmp-caseN-NNN". Each item reads on its own with the passage
                shown above it; an item's approach may differ from the case's
                where it concerns one part of a hybrid programme.

   Mix across all five: People 17 / Process 20 / Business 13 (34/40/26 against
   33/41/26); predictive 20 / adaptive 14 / hybrid 16; 38 multiple choice,
   7 multiple response, 2 matching, 1 drop-down, 2 fill-in-the-blank.
   All scenarios, names and organisations are invented. */
PMP.CASES = [

  /* ── CASE 1 ── predictive · water utility ─────────────────────────────── */
  {
    id: "case-01",
    title: "Filtration upgrade at a live treatment works",
    approach: "predictive",
    setting: "A regional water utility replacing four filter banks while the works stays in service.",
    mix: { people: 3, process: 5, business: 2 },
    passage: [
      "You are the project manager for a water utility's upgrade of the filtration stage at Millbrook treatment works, which supplies drinking water to around 180,000 people. The works must stay in service throughout. The approved plan replaces four filter banks one at a time over twelve months, with a budget at completion of 4.8 million. You took the project over at the start of month 5, when your predecessor moved to another programme. Her handover notes were brief: a schedule, a risk register last updated in month 2, and the contracts.",
      "The civil works are let to a contractor on a fixed-price contract with a schedule of rates for variations. Mechanical installation is on a time-and-materials contract with an installer the utility has used before. Design support comes from an engineering consultancy whose lead hydraulic engineer, Dr Ines Moreau, produced the original filter-bank calculations. The consultancy has told you she will move to another client in four weeks.",
      "Each filter bank can only be connected during a planned shutdown. Operations allows two 36-hour shutdowns per quarter, and the works' discharge permit requires the environmental regulator to be given 30 days' written notice of any shutdown that could affect the quality of water leaving the site. Both shutdowns so far have overrun, by nine and fourteen hours, and the operations manager, Grace Adeyemi, has had to call in her night-shift team at short notice each time. She has told you she will not release the next window until she sees a plan she believes.",
      "The sponsor, the utility's director of assets, Martin Hale, wants the third and fourth banks in service before the winter demand peak, which begins in month 11. He has asked you to keep the board report positive while the project recovers.",
      "At the end of month 6 the civil contractor submitted a variation claim of 180,000 for unforeseen ground conditions under filter bank 3, and your site supervisor has already told them to start the additional piling so that the next shutdown is not missed. The new filter controls also differ markedly from the old panels, and none of the operators has yet been trained on them.",
      "Exhibit 1 shows the earned value position at the end of month 6."
    ],
    exhibits: [
      { id: "ex1", label: "Exhibit 1", title: "Earned value at the end of month 6, in thousands",
        cols: ["Measure", "Value"],
        rows: [
          ["Budget at completion (BAC)", "4,800"],
          ["Planned value (PV)", "2,400"],
          ["Earned value (EV)", "2,160"],
          ["Actual cost (AC)", "2,700"],
          ["Contingency reserve remaining", "150"]
        ],
        note: "All figures are cumulative from the start of the project." }
    ],
    questions: [
      {
        id: "pmp-case1-001", type: "fillblank", domain: "process", ecoTask: "process-6",
        approach: "predictive", difficulty: 3, tags: ["earned-value", "forecast", "cpi", "archetype-apply-the-rule"],
        stem: "Using Exhibit 1, work out the cost performance index at the end of month 6, then the estimate at completion in thousands, assuming the cost performance to date continues for the rest of the project.",
        blanks: [
          { id: "cpi", label: "Cost performance index, to two decimal places", numeric: true, expect: 0.8, tol: 0.005, unit: "", rationale: "The cost performance index is earned value divided by actual cost: 2,160 ÷ 2,700 = 0.80. Each unit spent has delivered eighty per cent of a unit of planned work." },
          { id: "eac", label: "Estimate at completion, in thousands", numeric: true, expect: 6000, tol: 5, unit: "thousand", rationale: "With performance expected to continue, the estimate at completion is the budget divided by the index: 4,800 ÷ 0.80 = 6,000. That is 1,200 above the approved budget, far more than the 150 of contingency left." }
        ],
        explanation: "The cost performance index measures how much value each unit of spend is producing, and dividing the budget at completion by it gives the forecast if nothing about performance changes. That assumption should be stated whenever the figure is reported: a forecast of this kind projects the trend rather than setting a target, and its main use is to show early whether reserves can absorb the gap. Here they cannot, by a wide margin, which turns a cost variance into a decision for the sponsor.",
        mindset: "A forecast is only as useful as the assumption behind it, so say which assumption the figure carries."
      },
      {
        id: "pmp-case1-002", type: "single", domain: "process", ecoTask: "process-9",
        approach: "predictive", difficulty: 4, tags: ["status", "reporting", "sponsor", "archetype-make-visible"],
        stem: "Martin has asked you to keep the board report positive while the project recovers. The earned value figures in Exhibit 1 point to a significant overrun at completion. What should the project manager do?",
        options: [
          { id: "a", text: "Report the current forecast and the drivers behind it, with the recovery options and the decisions needed from the board, and walk Martin through it before it is issued.", correct: true,
            rationale: "Honest reporting and support for the sponsor are compatible. Showing Martin the content first respects his position, and presenting options turns bad news into a set of decisions the board can actually make." },
          { id: "b", text: "Report the cost variance to date but leave out the estimate at completion until the recovery actions have had a month to take effect.", correct: false,
            rationale: "This is the selective-disclosure error. The forecast is the figure the board most needs, and withholding it for a month removes the time in which corrective decisions are cheapest." },
          { id: "c", text: "Issue the report as Martin asks, describing the position as recovering, and record your own concerns about the forecast in the project file.", correct: false,
            rationale: "This is the private-record error. A file note protects the project manager and does nothing for the decision-makers, and a report that misstates the forecast breaches the duty to report truthfully." },
          { id: "d", text: "Escalate Martin's request to the board chair as a governance concern before the next report is due.", correct: false,
            rationale: "This is the premature-escalation error. Martin has not yet seen the analysis or been asked to support an accurate report, and going round him damages the relationship the recovery depends on." }
        ],
        explanation: "Status reporting exists so that the people funding a project can decide with accurate information, and that duty does not bend to a sponsor's preference for a positive tone. The professional route is to give the sponsor the analysis first, with options attached, so the conversation is about what to do rather than what to say. Escalation comes after the sponsor has been given the chance to act on the facts and has declined.",
        mindset: "Report the forecast truthfully and bring the sponsor in early, so honesty and support arrive together."
      },
      {
        id: "pmp-case1-003", type: "single", domain: "business", ecoTask: "business-3",
        approach: "predictive", difficulty: 4, tags: ["change-control", "variation", "contract", "archetype-route-to-owner"],
        stem: "The civil contractor has started additional piling under filter bank 3 on your site supervisor's instruction, and has submitted a variation claim of 180,000. No change request has been raised. What should the project manager do first?",
        options: [
          { id: "a", text: "Raise the claim through the change control process, record the supervisor's instruction and its date, and have the impact on cost, schedule and the shutdown plan assessed before the work goes further.", correct: true,
            rationale: "The work has started, so the priority is to bring it under control: a recorded decision path, the contractual facts captured while they are fresh, and an impact assessment that lets the right authority decide whether to continue." },
          { id: "b", text: "Approve the variation, since stopping the piling now would miss the next shutdown and cost more than the claim.", correct: false,
            rationale: "This is the sunk-momentum error. It may well be the right outcome, and the project manager approving a 180,000 variation outside the process takes a decision that belongs to the change authority, on an untested claim." },
          { id: "c", text: "Instruct the contractor to stop work until the claim has been independently verified, so the utility is not committed to costs it has not approved.", correct: false,
            rationale: "This is the over-correction error. Stopping without assessment risks the shutdown and may itself create a claim for disruption, and the instruction already given may bind the utility whatever happens next." },
          { id: "d", text: "Tell the site supervisor that only the project manager can instruct the contractor, and ask the contractor to resubmit the claim through the correct channel.", correct: false,
            rationale: "This is the procedural-reprimand error. Clarifying authority matters, and on its own it neither assesses the claim nor decides whether the work in progress should continue." }
        ],
        explanation: "When a change has been instructed outside the process, the response is to bring it into the process quickly rather than to ratify it or reverse it on instinct. Recording who instructed what and when protects the contractual position, and an impact assessment gives the change authority the basis for a decision. Approving or stopping without that assessment both substitute the project manager's judgement for the governance the contract and the organisation have set up.",
        mindset: "Bring an unauthorised change under control first, then let the right authority decide on evidence."
      },
      {
        id: "pmp-case1-004", type: "single", domain: "people", ecoTask: "people-5",
        approach: "predictive", difficulty: 3, tags: ["expectations", "operations", "shutdown", "archetype-diagnose-first"],
        stem: "Grace Adeyemi has said she will not release the next shutdown window until she sees a plan she believes. Both earlier shutdowns overran and her night-shift team was called in at short notice. What should the project manager do?",
        options: [
          { id: "a", text: "Meet Grace to understand what the overruns cost her team, then build the next shutdown plan with her, including the go and no-go points and how an overrun would be handled.", correct: true,
            rationale: "Her condition is reasonable and specific. Planning the shutdown with her rather than for her addresses the cause of her distrust and gives operations a say in the controls that protect them." },
          { id: "b", text: "Send Grace the revised shutdown schedule with additional contingency hours added, and ask her to confirm the window by the end of the week.", correct: false,
            rationale: "This is the plan-for-not-with error. A longer schedule produced without her answers none of her questions about how the work will be run, and a deadline to confirm adds pressure without adding confidence." },
          { id: "c", text: "Ask Martin to remind Grace that the upgrade is a board priority and that operations is expected to make windows available.", correct: false,
            rationale: "This is the borrowed-authority error. It may secure the window, and it will cost the cooperation of the person whose team runs the works during every shutdown that follows." },
          { id: "d", text: "Offer to fund overtime for the night-shift team for any future overrun, so the impact on operations is covered.", correct: false,
            rationale: "This is the price-the-symptom error. Paying for the overrun treats Grace's concern as a cost problem, when what she has asked for is a plan that does not overrun." }
        ],
        explanation: "A stakeholder who withholds cooperation after two failures is usually signalling that her expectations were never built into the plan. Aligning expectations means agreeing, with the people who carry the consequences, what the plan is, how it will be controlled and what happens if it slips. Authority or money may unlock one window; joint planning is what makes the remaining ones reliable.",
        mindset: "When trust has been lost through overruns, rebuild it by planning with the people who bore the cost."
      },
      {
        id: "pmp-case1-005", type: "multi", domain: "business", ecoTask: "business-2",
        approach: "predictive", difficulty: 3, tags: ["compliance", "regulator", "permit", "archetype-apply-the-rule"],
        stem: "To recover time, your planner proposes bringing the next shutdown forward so that it begins in three weeks. Connecting filter bank 3 could affect the quality of water leaving the works. Which two actions should the project manager take? Select two.",
        options: [
          { id: "a", text: "Hold the shutdown to a date at least 30 days after written notice can be given to the regulator, and replan the recovery around that constraint.", correct: true,
            rationale: "The permit's notice period is a legal condition of operating the works. Recovery has to be planned inside it, not by treating it as negotiable." },
          { id: "b", text: "Give the regulator written notice now for the earliest compliant date, after confirming that date with operations.", correct: true,
            rationale: "Notifying at once secures the earliest date the permit allows, and agreeing it with operations first keeps the notice accurate." },
          { id: "c", text: "Proceed with the earlier date and notify the regulator by telephone, since the shutdown is routine work on an existing asset.", correct: false,
            rationale: "This is the reclassify-to-comply error. Calling the work routine does not change what the permit requires, and a telephone call is not written notice." },
          { id: "d", text: "Ask the regulator to waive the notice period in view of the winter demand peak.", correct: false,
            rationale: "This is the ask-for-an-exception error. It invites the regulator to set aside a condition designed to protect water quality, to solve a schedule problem the project created." },
          { id: "e", text: "Bring the shutdown forward but shorten it to 24 hours, so that any effect on water quality is too brief to need notice.", correct: false,
            rationale: "This is the engineer-around-the-rule error. The permit ties notice to the possible effect on quality, not to duration, and a shorter window raises the overrun risk Grace is already worried about." }
        ],
        explanation: "Compliance requirements are constraints on the plan, not risks to be traded against the schedule. The right response to a recovery idea that breaches one is to keep the requirement intact and recover what time is still available inside it, which here means giving notice at once and agreeing the date with operations. Waivers, reclassification and redesigning the work to avoid a trigger all start from the assumption that the requirement is the problem.",
        mindset: "Plan recovery inside legal and regulatory limits, never by testing where they give."
      },
      {
        id: "pmp-case1-006", type: "single", domain: "people", ecoTask: "people-7",
        approach: "predictive", difficulty: 2, tags: ["knowledge-transfer", "handover", "consultancy", "archetype-act-now"],
        stem: "Dr Ines Moreau, whose hydraulic calculations underpin the filter-bank design, leaves for another client in four weeks. Much of her reasoning about the design margins is not written down. What should the project manager do?",
        options: [
          { id: "a", text: "Agree with the consultancy a handover in which her successor works alongside her on the remaining design questions, with the key design decisions and their reasoning recorded.", correct: true,
            rationale: "Tacit reasoning transfers best by working together on live problems, and recording decisions with their rationale leaves something the project can return to when a margin is questioned." },
          { id: "b", text: "Ask Dr Moreau to write a full design report before she leaves, covering every calculation she has made.", correct: false,
            rationale: "This is the document-everything error. A comprehensive report in four weeks is unlikely to be finished, and it will capture calculations, which exist already, rather than the judgement behind the margins." },
          { id: "c", text: "Ask the consultancy to keep Dr Moreau on the project until all four banks are commissioned.", correct: false,
            rationale: "This is the hold-the-person error. The consultancy has made a resourcing decision, and asking it to reverse that is a request rather than a plan for the knowledge." },
          { id: "d", text: "Rely on the consultancy's internal handover, since the contract makes the consultancy responsible for continuity of its staff.", correct: false,
            rationale: "This is the contract-will-cover-it error. Contractual responsibility does not protect the project from losing understanding it depends on, and the project manager does not know what the internal handover will cover." }
        ],
        explanation: "The knowledge at risk is judgement rather than data, and judgement transfers through shared work and recorded reasoning far more than through documents written in a hurry. The project manager's role is to make the handover happen on purpose, with the consultancy, while the expert is still available, rather than to rely on a clause or ask for the departure to be cancelled.",
        mindset: "Transfer judgement while its owner is still in the room, by working alongside them and recording why."
      },
      {
        id: "pmp-case1-007", type: "single", domain: "process", ecoTask: "process-5",
        approach: "predictive", difficulty: 4, tags: ["procurement", "time-and-materials", "contract", "archetype-diagnose-first"],
        stem: "Hours billed by the mechanical installer under its time-and-materials contract have risen by 40 per cent over two months, while the installation milestones completed have not moved. What should the project manager do?",
        options: [
          { id: "a", text: "Review the timesheets and progress records with the installer against the contract terms, establish why hours are not turning into progress, and agree reporting that links hours to completed work.", correct: true,
            rationale: "Under time and materials the buyer carries the cost risk, so oversight is the buyer's main control. Diagnosing the gap with the installer, and tying future reporting to progress, uses that control without prejudging the cause." },
          { id: "b", text: "Withhold payment of the latest invoices until the installer can show progress in line with the hours billed.", correct: false,
            rationale: "This is the withhold-first error. Unless the contract allows it, withholding payment for properly billed hours is a breach by the buyer, and it damages the relationship before anyone knows why the hours rose." },
          { id: "c", text: "Ask procurement to convert the contract to a fixed price for the remaining installation work.", correct: false,
            rationale: "This is the switch-contract-type error. A fixed price may suit the remaining work later, and negotiating it before understanding the overrun means pricing a scope nobody yet understands." },
          { id: "d", text: "Accept the hours as the normal effect of working around a live plant, and increase the cost forecast to match.", correct: false,
            rationale: "This is the absorb-without-asking error. The live plant may explain some of the increase, and accepting it without review gives up the oversight a time-and-materials contract depends on." }
        ],
        explanation: "Contract types put cost risk in different places, and under time and materials it sits with the buyer, which makes monitoring how hours turn into progress the central control. The first move is to find out why that conversion has broken down, using the contract's terms and the records, and to strengthen reporting so the gap cannot widen unseen again. Penalties, renegotiation and quiet acceptance all act before the cause is known.",
        mindset: "Where the contract leaves cost risk with you, watch what each hour buys and ask early when it buys less."
      },
      {
        id: "pmp-case1-008", type: "single", domain: "process", ecoTask: "process-10",
        approach: "predictive", difficulty: 3, tags: ["closure", "transition", "training", "archetype-apply-the-rule"],
        stem: "Filter bank 3 is due to be handed over to operations after its commissioning tests next month. None of the operators has yet been trained on the new filter controls, which differ markedly from the old panels. What should the project manager do?",
        options: [
          { id: "a", text: "Make operator training and sign-off part of the handover criteria for each bank, and plan the training with Grace so it is complete before bank 3 is accepted.", correct: true,
            rationale: "Transition is complete only when the receiving organisation can run what it has been given. Writing training into the acceptance criteria makes readiness a condition of handover rather than a hope." },
          { id: "b", text: "Hand bank 3 over after commissioning as planned, and arrange training once all four banks are installed so the operators learn the full system in one course.", correct: false,
            rationale: "This is the batch-the-training error. It leaves operators running a live treatment asset on controls they have not been taught for months, which is an operational and public health risk." },
          { id: "c", text: "Ask the installer to keep a technician on site for the first month after handover to operate the new controls.", correct: false,
            rationale: "This is the borrowed-operator error. It covers the gap with someone outside operations and postpones the point at which the utility can run its own asset." },
          { id: "d", text: "Give the operators the manufacturer's manuals for the new controls ahead of handover.", correct: false,
            rationale: "This is the manual-equals-training error. Documentation supports training and does not replace it, particularly for controls that differ markedly from what operators know." }
        ],
        explanation: "A deliverable is not finished when it passes its technical tests but when the people who will run it are ready to. Building operational readiness into the handover criteria, and planning it with the receiving team, makes readiness a checked condition rather than an assumption. Deferring training, borrowing operators or handing over manuals all transfer the asset before the capability to run it.",
        mindset: "Hand over only what the receiving team can run, and make that readiness part of what acceptance checks."
      },
      {
        id: "pmp-case1-009", type: "matching", domain: "people", ecoTask: "people-8",
        approach: "predictive", difficulty: 2, tags: ["communication", "stakeholders", "channels", "archetype-apply-the-rule"],
        stem: "Match each stakeholder group in the Millbrook case to the communication approach that fits it best. One approach is not used.",
        prompts: [
          { id: "s1", text: "The environmental regulator, ahead of a shutdown that may affect water quality" },
          { id: "s2", text: "The operators on each shift during a shutdown" },
          { id: "s3", text: "Martin, as sponsor, between board meetings" }
        ],
        options: [
          { id: "reg", text: "Formal written notice in the form the permit requires", correct: true, match: "s1",
            rationale: "The permit specifies written notice, and a regulatory record needs to be precise, dated and traceable. A conversation can supplement it but cannot replace it." },
          { id: "brief", text: "A face-to-face briefing at each shift handover", correct: true, match: "s2",
            rationale: "Operators need current, practical information at the point of work and a chance to ask questions. A briefing at handover reaches each shift as it takes responsibility." },
          { id: "exc", text: "A short report by exception, with the forecast and any decision he needs to take", correct: true, match: "s3",
            rationale: "A sponsor needs to know what has changed and what he must decide. Reporting by exception keeps his attention on the few things that need it." },
          { id: "news", text: "A monthly newsletter describing progress on all four filter banks", correct: false, match: null,
            rationale: "Not used. A newsletter suits a wide audience with a general interest, and none of the three groups needs that: each needs something specific, timely or formal." }
        ],
        explanation: "Communication methods are chosen for what each audience needs to do with the information. A regulator needs a formal record, people at the point of work need timely and interactive briefings, and a sponsor needs the exceptions and the decisions. A broadcast format fits none of these, which is why a single channel for every stakeholder is rarely enough.",
        mindset: "Choose the channel by what the audience must do with the message, not by what is easiest to send."
      },
      {
        id: "pmp-case1-010", type: "single", domain: "process", ecoTask: "process-8",
        approach: "predictive", difficulty: 4, tags: ["schedule", "compression", "sponsor", "archetype-trade-off"],
        stem: "Martin asks how the fourth filter bank could still be in service before the winter peak. Its remaining activities are civil works, mechanical installation and commissioning, currently planned in sequence. What should the project manager do?",
        options: [
          { id: "a", text: "Analyse the options for compressing the remaining path, such as overlapping installation with the end of the civil works or adding resources, and present each with its cost and risk for Martin to decide.", correct: true,
            rationale: "Both compression techniques have a price: overlapping adds rework risk and adding resources adds cost. Laying out each with its consequences gives the sponsor a real choice rather than a promise." },
          { id: "b", text: "Overlap commissioning with mechanical installation, since commissioning is the last activity and has the most room to move.", correct: false,
            rationale: "This is the overlap-the-wrong-pair error. Commissioning tests the installed equipment, so starting it before installation is complete is not fast-tracking but testing something that does not exist yet." },
          { id: "c", text: "Tell Martin the date cannot be met, since the plan has already lost time in both shutdowns.", correct: false,
            rationale: "This is the declare-defeat error. The schedule has compression options that have not been analysed, and the sponsor is entitled to see them before the date is given up." },
          { id: "d", text: "Ask both contractors to work extended hours on the remaining activities, and report the new date to the board.", correct: false,
            rationale: "This is the commit-before-costing error. Extended hours may be one of the options, and committing to them and announcing a date before cost, fatigue and shutdown constraints are assessed creates a promise the analysis may not support." }
        ],
        explanation: "Compressing a schedule is a trade: fast-tracking buys time with rework risk, crashing buys it with money, and either is only worth doing on the critical path. The project manager's job is to analyse the options and present them with their consequences, so the person who owns the date also owns the trade. Committing to one technique first, or declaring the date lost without analysis, both skip that decision.",
        mindset: "Every way of compressing a schedule has a price, so put the price in front of the person who owns the date."
      }
    ]
  },

  /* ── CASE 2 ── adaptive · city council digital service ───────────────── */
  {
    id: "case-02",
    title: "Online parking permits for a city council",
    approach: "agile",
    setting: "A council's Scrum team replacing a paper permit process before an old contract ends.",
    mix: { people: 4, process: 4, business: 2 },
    passage: [
      "Harbridge City Council is replacing its paper and telephone process for residents' parking permits with an online service. The council's contract for the old permit database ends in five months and will not be renewed, so a first release must be live by then. You are the project manager working with a Scrum team of six developers and testers, a product owner from parking services, Malik Osei, and a user researcher shared with another service. Sprints are two weeks long.",
      "Six sprints in, the team has delivered account creation, address checking and payment, and its velocity has settled (Exhibit 2). The backlog items Malik has marked as essential for the first release total 160 points. They include vehicle registration, proof of residence upload, visitor permits and an interface to the enforcement officers' handheld devices.",
      "Usability sessions with residents have gone well for most tasks, but older residents repeatedly struggle at the vehicle registration step, and two abandoned the session. Malik is keen to hold the release scope and has suggested a help page. A resident who uses a screen reader has also written to complain that the payment page cannot be completed without a mouse. The team's definition of done includes code review and automated tests but nothing about accessibility.",
      "The head of digital services, Sunita Rao, sponsors the work. She is under pressure from councillors and has asked you to assign tasks to individual developers so she can see who is doing what. Two senior developers, Ewan and Lior, have fallen out over code review standards, and the daily scrum has become tense. Attendance at sprint reviews by parking services managers has dropped, and they now hear about progress second-hand.",
      "The service will store residents' names, addresses and vehicle registrations. The council's information governance officer has not yet been involved. Last week the national government announced a common data standard for parking permits that every council must adopt within eighteen months."
    ],
    exhibits: [
      { id: "ex2", label: "Exhibit 2", title: "Velocity by sprint, in story points completed",
        cols: ["Sprint", "1", "2", "3", "4", "5", "6"],
        rows: [["Points completed", "18", "22", "25", "21", "24", "22"]],
        note: "Remaining work marked essential for the first release: 160 points. About ten sprints remain before the old contract ends." }
    ],
    questions: [
      {
        id: "pmp-case2-001", type: "fillblank", domain: "process", ecoTask: "process-8",
        approach: "agile", difficulty: 3, tags: ["velocity", "forecast", "release", "archetype-apply-the-rule"],
        stem: "Using Exhibit 2, work out the average velocity of the last three sprints, then the number of whole sprints needed at that pace to complete the 160 points marked essential for the first release.",
        blanks: [
          { id: "vel", label: "Average velocity of sprints 4 to 6, in points", numeric: true, expect: 22.33, tol: 0.1, unit: "points", rationale: "(21 + 24 + 22) ÷ 3 = 67 ÷ 3, about 22.3 points a sprint. The recent sprints are used because they reflect the team as it now works, after the early ramp-up." },
          { id: "sprints", label: "Whole sprints needed for 160 points", numeric: true, expect: 8, tol: 0, unit: "sprints", rationale: "160 ÷ 22.3 is about 7.2, which rounds up to 8 because a part-sprint still has to be run. That leaves about two sprints in hand before the old contract ends." }
        ],
        explanation: "A velocity-based forecast divides the remaining work by the pace the team has recently shown, and rounds up to whole sprints. Using recent sprints rather than the whole history gives a figure that reflects the team as it now works. The forecast fits inside the time available with about two sprints to spare, which is margin rather than slack: it is the room in which known changes, such as accessibility fixes and the new data standard, will have to be absorbed.",
        mindset: "Forecast from the pace the team has shown, and treat whatever margin remains as room for the change you know is coming."
      },
      {
        id: "pmp-case2-002", type: "single", domain: "people", ecoTask: "people-6",
        approach: "agile", difficulty: 3, tags: ["customers", "usability", "product-owner", "archetype-make-visible"],
        stem: "Older residents repeatedly struggle at the vehicle registration step in usability sessions. Malik wants to hold the release scope and add a help page. What should the project manager do?",
        options: [
          { id: "a", text: "Bring the research findings to Malik with the user researcher, and help him weigh a simpler registration step against other release items in the backlog.", correct: true,
            rationale: "Malik owns the backlog, so the decision stays with him. Putting the evidence in front of him, with the researcher who gathered it, makes sure the trade is made knowingly and with the users' experience in view." },
          { id: "b", text: "Ask the team to redesign the registration step in the next sprint, since the research shows the current design does not work.", correct: false,
            rationale: "This is the bypass-the-owner error. The evidence is strong, and changing what the team builds without the product owner's decision undermines the role that sets priorities." },
          { id: "c", text: "Accept Malik's help page, since he is accountable for the product and has decided.", correct: false,
            rationale: "This is the defer-without-evidence error. Malik's authority is real, and supporting the role includes making sure he has seen what users are doing before the decision is treated as made." },
          { id: "d", text: "Raise the issue with Sunita, since excluding older residents could reflect badly on the council.", correct: false,
            rationale: "This is the escalate-around-the-owner error. Going to the sponsor before working it through with the product owner turns a backlog decision into a political one." }
        ],
        explanation: "In an agile team the product owner decides priorities, and the project manager's contribution is to make sure those decisions rest on evidence about real users. Bringing research into backlog refinement, with the person who gathered it, lets the product owner weigh a usability fix against other value rather than decide in the abstract. Overriding him, deferring to him without the evidence, or escalating past him all weaken the role that agile delivery relies on.",
        mindset: "Put the users' evidence in front of the person who decides, and let the decision follow from it."
      },
      {
        id: "pmp-case2-003", type: "single", domain: "people", ecoTask: "people-2",
        approach: "agile", difficulty: 3, tags: ["conflict", "working-agreement", "team", "archetype-diagnose-first"],
        stem: "Ewan and Lior, the two senior developers, disagree sharply about code review standards, and the tension has started to show in the daily scrum. Other team members have gone quiet. What should the project manager do first?",
        options: [
          { id: "a", text: "Talk with Ewan and Lior together to understand both positions, then help the team agree a code review standard that it records in its working agreement.", correct: true,
            rationale: "The disagreement is about a shared practice, so the durable resolution is a team agreement rather than a ruling. Hearing both first means the agreement deals with the real concerns." },
          { id: "b", text: "Decide the code review standard yourself, based on industry practice, so the team can move on.", correct: false,
            rationale: "This is the impose-a-ruling error. It ends the argument and leaves one developer overruled, and the team learns that its disagreements are settled from outside." },
          { id: "c", text: "Ask each of them to keep the disagreement out of the daily scrum and resolve it privately.", correct: false,
            rationale: "This is the move-it-offstage error. It protects the meeting and leaves both the conflict and the inconsistent reviews in place." },
          { id: "d", text: "Let the conflict run its course, since a self-organising team should resolve disagreements without intervention.", correct: false,
            rationale: "This is the misread-self-organisation error. Self-organisation does not mean ignoring a conflict that is already silencing other team members." }
        ],
        explanation: "Conflict over how the work is done is healthy until it becomes personal or starts to silence others, and both signs are present. The useful intervention brings the people involved together, separates the positions from the personalities, and turns the outcome into a team agreement everyone can hold each other to. Ruling, sidelining and waiting all leave the underlying disagreement unresolved.",
        mindset: "Resolve a disagreement about shared practice by helping the team agree the practice, not by choosing a winner."
      },
      {
        id: "pmp-case2-004", type: "single", domain: "people", ecoTask: "people-3",
        approach: "agile", difficulty: 4, tags: ["servant-leadership", "self-organisation", "sponsor", "archetype-challenge-the-premise"],
        stem: "Sunita, the sponsor, asks you to assign tasks to individual developers so she can show councillors who is doing what. The team currently pulls work from the sprint backlog itself. What should the project manager do?",
        options: [
          { id: "a", text: "Explain to Sunita why the team chooses its own tasks, and offer her views that answer the councillors' real question, such as the sprint goal, the board and the release forecast.", correct: true,
            rationale: "Her underlying need is visibility, not assignment. Meeting that need with the artifacts the team already uses protects self-organisation and gives her something better to show." },
          { id: "b", text: "Assign tasks as Sunita asks for the next few sprints, then return to self-selection once the pressure eases.", correct: false,
            rationale: "This is the temporary-command error. Teams take time to recover ownership once it has been removed, and the pressure that prompted the change is unlikely to ease before release." },
          { id: "c", text: "Decline the request on the grounds that task assignment is incompatible with Scrum.", correct: false,
            rationale: "This is the framework-as-shield error. It is accurate and unhelpful, and it leaves the sponsor with her problem and a reason to think the team is unaccountable." },
          { id: "d", text: "Ask the team to record in the tracking tool who is working on each task, and give Sunita access.", correct: false,
            rationale: "This is the half-measure error. It may satisfy the request, and it puts individual activity rather than progress towards the goal in front of councillors, which invites the scrutiny that pushes teams to look busy." }
        ],
        explanation: "A sponsor asking for individual assignments is usually asking for confidence that the work is under control. A servant leader protects the team's way of working by meeting that need on its own terms: explaining why self-organisation matters and offering transparent artifacts that show progress against goals. Complying, refusing flatly or exposing individual activity all leave the real concern unaddressed.",
        mindset: "Protect how the team works by answering the need behind a request, not by obeying or refusing its wording."
      },
      {
        id: "pmp-case2-005", type: "multi", domain: "process", ecoTask: "process-3",
        approach: "agile", difficulty: 4, tags: ["value", "backlog", "release", "archetype-trade-off"],
        stem: "The old permit database contract ends in five months and cannot be extended. Malik asks which of the remaining items should come first in the order of release. Which two should the project manager advise placing earliest? Select two.",
        options: [
          { id: "a", text: "Vehicle registration and proof of residence upload, since residents cannot obtain a permit without them.", correct: true,
            rationale: "These complete the core journey. Without them nothing the team has built can replace the old system, so they carry the release's value." },
          { id: "b", text: "The interface to the enforcement officers' handheld devices, since permits issued online must be checkable on the street.", correct: true,
            rationale: "A permit that cannot be enforced does not replace the old process. This item is less visible to residents and is essential to retiring the old database." },
          { id: "c", text: "Visitor permits, since residents mentioned them most often in usability sessions.", correct: false,
            rationale: "This is the loudest-request error. Visitor permits matter to residents, and the old system can be retired once permits can be issued and enforced, so they can follow." },
          { id: "d", text: "A redesign of the account pages, since the team now knows more about residents than when they were built.", correct: false,
            rationale: "This is the polish-first error. Improving delivered features is legitimate, and doing it ahead of the items that let the old system be switched off spends the margin the deadline needs." },
          { id: "e", text: "Dashboards for parking services managers, to rebuild their interest in sprint reviews.", correct: false,
            rationale: "This is the solve-the-wrong-problem error. Falling review attendance is an engagement problem, and building reports to fix it uses delivery capacity on something no deadline depends on." }
        ],
        explanation: "Ordering a backlog for value means asking what the release must make possible, which here is retiring the old system by a fixed date. The items that complete the issuing journey and make permits enforceable are what allow that, so they come first; desirable items the retirement does not depend on can follow. Popularity, polish and stakeholder interest are all real considerations, and none outranks the outcome the release exists to deliver.",
        mindset: "Order the work by the outcome the release must achieve, then fit everything else around it."
      },
      {
        id: "pmp-case2-006", type: "single", domain: "business", ecoTask: "business-2",
        approach: "agile", difficulty: 3, tags: ["compliance", "data-protection", "definition-of-done", "archetype-route-to-owner"],
        stem: "The permit service will store residents' names, addresses and vehicle registrations. The council's information governance officer has not been involved, and the team is three sprints from releasing the first features to the public. What should the project manager do?",
        options: [
          { id: "a", text: "Involve the information governance officer now, so the data protection requirements can be assessed and any resulting work added to the backlog and the definition of done.", correct: true,
            rationale: "Data protection obligations apply whatever the delivery method. Bringing the specialist in now, while the design can still change, lets compliance work be planned like any other requirement." },
          { id: "b", text: "Ask the team to follow the council's general data protection guidance, and involve the officer after release once real data is flowing.", correct: false,
            rationale: "This is the comply-later error. Assessing personal data handling after release means the public may be exposed to risks that should have been designed out." },
          { id: "c", text: "Add a backlog item for a data protection review and let Malik prioritise it against the other release items.", correct: false,
            rationale: "This is the compliance-as-option error. Whether to comply is not a prioritisation choice, although the work compliance creates can be ordered once its scope is known." },
          { id: "d", text: "Delay the release until the officer has reviewed the whole service in detail.", correct: false,
            rationale: "This is the stop-everything error. A review is needed, and freezing delivery before anyone has looked assumes the worst and spends the margin the deadline depends on." }
        ],
        explanation: "Compliance requirements are not optional backlog items, but they sit comfortably with incremental delivery when the relevant specialists are involved early. The aim is to identify the obligations, turn them into concrete work and acceptance conditions, and build them in from the next sprint. Leaving it until after release, making it negotiable, or stopping work all get the timing wrong.",
        mindset: "Bring compliance specialists in while the design can still change, and make their requirements part of done."
      },
      {
        id: "pmp-case2-007", type: "single", domain: "people", ecoTask: "people-8",
        approach: "agile", difficulty: 2, tags: ["communication", "sprint-review", "stakeholders", "archetype-diagnose-first"],
        stem: "Parking services managers have stopped attending sprint reviews and now hear about progress second-hand, sometimes inaccurately. Their teams will run the new service. What should the project manager do?",
        options: [
          { id: "a", text: "Ask the managers what would make the review worth their time, and adapt its timing and content so they see and try the increments that affect their teams.", correct: true,
            rationale: "Attendance usually falls when the event stops serving the attendee. Asking and adapting restores the feedback the team needs from the people who will operate the service." },
          { id: "b", text: "Send the managers a written summary after each sprint review instead.", correct: false,
            rationale: "This is the replace-dialogue-with-report error. A summary informs them, and it removes the inspection and feedback that are the review's purpose." },
          { id: "c", text: "Ask Sunita to require the managers' attendance at every sprint review.", correct: false,
            rationale: "This is the mandate-attendance error. Required attendance fills the room without making the event useful, and it spends the sponsor's authority on a symptom." },
          { id: "d", text: "Record the reviews so the managers can watch them when convenient.", correct: false,
            rationale: "This is the one-way-channel error. A recording cannot be questioned or tried, and the managers' feedback is the part the team is missing." }
        ],
        explanation: "The sprint review is a working session in which stakeholders inspect the increment and shape what comes next, and its value depends on the right people taking part. When key stakeholders drift away, the first step is to find out why and adapt the event to their needs. Replacing it with reports, recordings or mandated attendance keeps information flowing one way and loses the feedback.",
        mindset: "When stakeholders stop coming, change the event until it is worth their time."
      },
      {
        id: "pmp-case2-008", type: "single", domain: "process", ecoTask: "process-7",
        approach: "agile", difficulty: 3, tags: ["quality", "accessibility", "definition-of-done", "archetype-apply-the-rule"],
        stem: "A resident who uses a screen reader reports that the payment page cannot be completed without a mouse. The team's definition of done covers code review and automated tests but nothing about accessibility. What should the project manager do?",
        options: [
          { id: "a", text: "Work with the team and Malik to fix the payment page, and add accessibility checks to the definition of done so later increments meet the standard as they are built.", correct: true,
            rationale: "The defect needs fixing and the gap that let it through needs closing. Updating the definition of done makes accessibility part of every increment rather than a later clean-up." },
          { id: "b", text: "Schedule an accessibility audit of the whole service before the first release.", correct: false,
            rationale: "This is the inspect-at-the-end error. An audit will find defects, and finding them late costs more than building to the standard from now on." },
          { id: "c", text: "Fix the payment page as a priority defect and reply to the resident.", correct: false,
            rationale: "This is the fix-the-instance error. It deals with the one page reported and leaves the process that produced it unchanged, so the next page will have the same problem." },
          { id: "d", text: "Add accessibility to the backlog as a separate feature for Malik to prioritise.", correct: false,
            rationale: "This is the quality-as-feature error. Accessibility is a standard every feature must meet, not an item that can be ordered behind others." }
        ],
        explanation: "In agile delivery, quality is built in through the definition of done, which sets the standard every increment must meet before it counts as complete. A defect that reveals a gap in that standard calls for both a fix and a change to the definition, so the problem does not recur. Audits at the end, isolated fixes and treating quality as a feature all leave the gap open.",
        mindset: "When a defect exposes a gap in done, fix the defect and close the gap."
      },
      {
        id: "pmp-case2-009", type: "multi", domain: "business", ecoTask: "business-8",
        approach: "agile", difficulty: 3, tags: ["external", "regulation", "standards", "archetype-make-visible"],
        stem: "The national government has announced a common data standard for parking permits that every council must adopt within eighteen months. The first release is due in five months. Which two actions should the project manager take? Select two.",
        options: [
          { id: "a", text: "Assess with Malik and the team how far the standard differs from the current data design, and what that means for the backlog.", correct: true,
            rationale: "The right response depends on the gap. Assessing it early, with the people who own the product and the design, turns an announcement into specific backlog items." },
          { id: "b", text: "Brief Sunita on the standard, its deadline and the emerging impact, so the council's plans and funding can allow for it.", correct: true,
            rationale: "An external change with a statutory deadline affects the council beyond this release. The sponsor needs to know early so that decisions outside the team's control can be taken in time." },
          { id: "c", text: "Pause development until the full standard is published, to avoid building to a data design that will change.", correct: false,
            rationale: "This is the freeze-for-certainty error. The old contract still ends in five months, and stopping work for an eighteen-month change puts the nearer deadline at risk." },
          { id: "d", text: "Note the standard for a future project, since the first release is due well before the adoption deadline.", correct: false,
            rationale: "This is the not-our-problem-yet error. Data designs are expensive to change once populated, and ignoring the standard now may build rework into the release." },
          { id: "e", text: "Ask the team to redesign the data model to the announced standard in the next sprint.", correct: false,
            rationale: "This is the react-before-assessing error. The impact has not been assessed, and diverting a sprint before Malik has weighed it bypasses the backlog's owner." }
        ],
        explanation: "Changes in the external environment, especially regulatory ones with deadlines, should be assessed promptly for their effect on the product and made visible to the people who govern the work. Assessment turns the change into backlog items the product owner can prioritise, and briefing the sponsor lets wider plans adjust. Freezing, ignoring or reacting without analysis all mishandle a change whose impact is not yet known.",
        mindset: "Assess an external change quickly, put its impact in the backlog, and tell the people who fund the work."
      },
      {
        id: "pmp-case2-010", type: "matching", domain: "process", ecoTask: "process-9",
        approach: "agile", difficulty: 2, tags: ["artifacts", "information-radiators", "reporting", "archetype-apply-the-rule"],
        stem: "Match each question a stakeholder in the Harbridge case might ask to the artifact that answers it best. One artifact is not used.",
        prompts: [
          { id: "q1", text: "Is the team on course to deliver the essential items before the old contract ends?" },
          { id: "q2", text: "What is being worked on right now, and what is blocked?" },
          { id: "q3", text: "How much has the team completed in each recent sprint?" }
        ],
        options: [
          { id: "burnup", text: "A release burnup chart", correct: true, match: "q1",
            rationale: "A burnup shows completed work against total release scope over time, so it answers whether the release is on course and shows scope changes as they happen." },
          { id: "board", text: "The team's task board", correct: true, match: "q2",
            rationale: "The board shows each item's current state and any blockers, which is exactly what someone asking about today's work needs." },
          { id: "vel", text: "A velocity chart", correct: true, match: "q3",
            rationale: "A velocity chart shows completed points per sprint. It answers questions about recent throughput and supports forecasting, though not progress towards a particular scope." },
          { id: "gantt", text: "A detailed bar chart of every task across the five months", correct: false, match: null,
            rationale: "Not used. A task-level schedule suits work planned in detail up front, and this team plans sprint by sprint, so it would need constant redrawing and still answer none of these questions better." }
        ],
        explanation: "Information radiators work when each one answers a specific question for a specific audience. Release progress is a burnup question, current work and blockers is a board question, and throughput is a velocity question. Mismatched artifacts, such as a detailed up-front schedule for an iterative team, cost effort to maintain and mislead the people reading them.",
        mindset: "Pick the artifact that answers the question being asked, and keep it visible where the asker looks."
      }
    ]
  },

  /* ── CASE 3 ── hybrid · hospital pathology ───────────────────────────── */
  {
    id: "case-03",
    title: "Replacing a pathology laboratory system",
    approach: "hybrid",
    setting: "A hospital group's three laboratories moving to a configured product, with an agile integration team.",
    mix: { people: 3, process: 4, business: 3 },
    passage: [
      "Westvale Health runs pathology laboratories at three hospitals, which together process around 9,000 samples a day. Their laboratory information system is twenty years old and its supplier will end support next year. You are leading the replacement programme.",
      "The new system is a configurable product. Under a fixed-price contract, the vendor configures it to the laboratories' agreed specification and is paid at milestones, ending with go-live at each site in turn: Site A, then Site B, then Site C. In parallel, an internal integration team works in two-week sprints to build and adapt the interfaces between the new system and the hospitals' other systems, such as the patient record and the requesting systems used on the wards. The programme board meets monthly and holds the authority for changes to scope, cost and the milestone plan.",
      "The integration team relies on a single interface specialist, Kofi Mensah, who is also allocated half-time to an electronic prescribing project. Relations between the vendor's configuration team and the integration team have become strained, and each now blames the other for interface defects found in testing.",
      "The laboratories are accredited, and their accreditation requires documented validation of any new system before patient results are released through it. The chief biomedical scientist at Site C, Dr Helen Park, was not part of the requirements workshops. She has seen a demonstration and says the result-flagging rules are less safe than the ones her laboratory uses now. At Site B, staff have started keeping a spreadsheet of results alongside the test system just in case, and several have told their manager they do not see why the change is needed.",
      "The laboratory manager at Site A has asked the vendor for an additional turnaround-time report. The vendor says it is outside the contracted specification. Clinicians on the wards have been told little about the cutover and expect turnaround times to stay as they are.",
      "Exhibit 3 shows readiness four weeks before Site A goes live and ten weeks before Site B."
    ],
    exhibits: [
      { id: "ex3", label: "Exhibit 3", title: "Go-live readiness by site",
        cols: ["Measure", "Site A", "Site B"],
        rows: [
          ["Validation tests passed", "92%", "40%"],
          ["Staff trained", "85%", "35%"],
          ["Interfaces ready", "100%", "60%"],
          ["Weeks to go-live", "4", "10"]
        ],
        note: "Site C is not yet in readiness tracking." }
    ],
    questions: [
      {
        id: "pmp-case3-001", type: "single", domain: "business", ecoTask: "business-1",
        approach: "hybrid", difficulty: 4, tags: ["governance", "hybrid", "milestones", "archetype-route-to-owner"],
        stem: "The integration team's product owner wants to reorder the interface backlog so that ward requesting is delivered before the patient record interface. The vendor's Site A milestone depends on the patient record interface being ready. What should the project manager do?",
        options: [
          { id: "a", text: "Have the impact on the Site A milestone assessed, and take the proposed change to the programme board, which holds authority over the milestone plan.", correct: true,
            rationale: "The product owner can order the backlog within the constraints the programme has set, and a change that moves a contracted milestone crosses into the board's authority." },
          { id: "b", text: "Let the product owner reorder the backlog, since backlog order is the product owner's decision in an agile team.", correct: false,
            rationale: "This is the role-in-isolation error. The product owner's authority is real and bounded by the programme's governance, and this reorder affects a contracted milestone." },
          { id: "c", text: "Refuse the reorder, since the vendor's milestones are fixed by contract.", correct: false,
            rationale: "This is the contract-as-veto error. Milestones can change through the proper route, and refusing without assessment may forgo a better plan." },
          { id: "d", text: "Ask the vendor whether its milestone can move, and agree a new date directly if it can.", correct: false,
            rationale: "This is the side-deal error. Agreeing a milestone change with the vendor outside the board bypasses the body that holds that authority and may have contractual consequences." }
        ],
        explanation: "In a hybrid programme each part keeps its own decision rights, and governance defines where they meet. Backlog ordering belongs to the product owner until it changes something another authority controls, at which point the decision moves to that authority with an impact assessment attached. Deciding locally, refusing on contractual grounds or negotiating side agreements all blur the lines the governance model exists to hold.",
        mindset: "Let each part of a hybrid programme decide within its own bounds, and route decisions that cross them to the owner of the boundary."
      },
      {
        id: "pmp-case3-002", type: "single", domain: "process", ecoTask: "process-1",
        approach: "hybrid", difficulty: 3, tags: ["planning", "hybrid", "dependencies", "archetype-make-visible"],
        stem: "The vendor plans by milestones and the integration team plans sprint by sprint. Twice in the last month an interface needed for a milestone was not in the sprint before it. What should the project manager do?",
        options: [
          { id: "a", text: "Build an integrated plan that shows each vendor milestone with the interfaces it depends on, and use it in sprint planning so the sprint goals line up with the milestones.", correct: true,
            rationale: "The failure is at the boundary between two planning approaches. Making the dependencies explicit in one view gives both sides what they need without forcing one to plan like the other." },
          { id: "b", text: "Ask the integration team to plan all remaining interface work up front so that it can be scheduled against the milestones.", correct: false,
            rationale: "This is the make-everyone-predictive error. Detailed up-front planning removes the adaptability the team needs for interface work whose details emerge in testing." },
          { id: "c", text: "Ask the vendor to move each milestone two weeks later to give the integration team more room.", correct: false,
            rationale: "This is the buy-slack error. Later milestones hide the coordination problem, and the same miss will recur at the new dates." },
          { id: "d", text: "Hold a weekly meeting between the vendor and integration leads to discuss upcoming needs.", correct: false,
            rationale: "This is the meeting-instead-of-plan error. A conversation helps, and without a shared view of the dependencies it relies on memory and good will." }
        ],
        explanation: "Hybrid programmes work when the predictive and adaptive parts are each planned in their own way and integrated at the points where they depend on each other. That integration happens through a shared view of dependencies that feeds both the milestone plan and sprint planning. Forcing one approach on everyone, adding slack, or relying on meetings alone all leave the dependency itself unmanaged.",
        mindset: "Integrate different planning approaches at their dependencies, not by making them plan the same way."
      },
      {
        id: "pmp-case3-003", type: "multi", domain: "process", ecoTask: "process-7",
        approach: "hybrid", difficulty: 3, tags: ["quality", "validation", "go-live", "archetype-apply-the-rule"],
        stem: "Site A is four weeks from go-live. Exhibit 3 shows 92 per cent of validation tests passed and all interfaces ready, and accreditation requires documented validation before results are released through a new system. Which two actions should the project manager take before go-live? Select two.",
        options: [
          { id: "a", text: "Make sure the remaining validation tests are completed, or any exception is formally assessed and accepted by the laboratory's quality lead, with the evidence documented.", correct: true,
            rationale: "Accreditation requires documented validation, so the go-live condition is evidence, not a percentage. An exception may be legitimate, and it must be decided by the person accountable for quality." },
          { id: "b", text: "Agree go and no-go criteria for Site A with the laboratory and the board, including validation status, and check them at a readiness review before cutover.", correct: true,
            rationale: "Criteria agreed in advance turn go-live into a checked decision rather than a date, and they protect the people making it from pressure on the day." },
          { id: "c", text: "Go live as planned, since 92 per cent is well above the readiness levels at the other sites.", correct: false,
            rationale: "This is the relative-readiness error. Comparing sites says nothing about whether this site meets the accreditation requirement." },
          { id: "d", text: "Ask the vendor to certify that the system is fit for clinical use, in place of the remaining tests.", correct: false,
            rationale: "This is the outsource-the-assurance error. The laboratory's accreditation requires its own validation, and a supplier statement does not satisfy it." },
          { id: "e", text: "Move the remaining validation tests to the first month after go-live, running them on live samples.", correct: false,
            rationale: "This is the validate-in-production error. Releasing patient results through an unvalidated system is precisely what the requirement exists to prevent." }
        ],
        explanation: "Quality requirements that come from accreditation or regulation define the conditions for release, and they are met with evidence rather than with a sense that readiness is high enough. Completing or formally dispositioning the outstanding tests, and checking agreed criteria at a readiness review, make go-live a decision taken on that evidence. Comparisons, supplier assurances and testing after release all substitute something weaker for what the requirement asks.",
        mindset: "Release only on the evidence the quality standard requires, decided against criteria agreed beforehand."
      },
      {
        id: "pmp-case3-004", type: "single", domain: "business", ecoTask: "business-5",
        approach: "hybrid", difficulty: 4, tags: ["risk", "readiness", "register", "archetype-diagnose-first"],
        stem: "Exhibit 3 shows Site B at 40 per cent of validation tests passed, 35 per cent of staff trained and 60 per cent of interfaces ready, with go-live in ten weeks. The risk register still rates Site B's go-live as a low risk. What should the project manager do?",
        options: [
          { id: "a", text: "Reassess the Site B go-live risk with its owners using the readiness data, agree responses with triggers for each measure, and report the revised rating to the board.", correct: true,
            rationale: "The register no longer reflects the evidence. Reassessing with the owners, and setting triggers on measurable readiness, turns a stale rating into a managed risk the board can see." },
          { id: "b", text: "Postpone the Site B go-live by a month now, since readiness is clearly too low.", correct: false,
            rationale: "This is the respond-before-assessing error. Ten weeks may be enough with the right responses, and moving the date unilaterally takes a decision that belongs to the board." },
          { id: "c", text: "Leave the rating until the next monthly review, since Site A is the current priority.", correct: false,
            rationale: "This is the wait-for-the-cycle error. Risk reviews follow the evidence, not the calendar, and a month's delay shortens the time in which responses can work." },
          { id: "d", text: "Add trainers and testers to Site B immediately to close the gap.", correct: false,
            rationale: "This is the solution-first error. More people may be part of the answer, and committing resources before the risk is analysed may address the wrong constraint, such as interfaces." }
        ],
        explanation: "A risk register is only useful if its ratings follow the evidence, and readiness data this far behind plan is evidence that the rating is wrong. The disciplined response is to reassess with the owners, choose responses tied to triggers, and make the change visible to the body that governs the date. Postponing, waiting or adding resources first all skip the analysis that tells you which response fits.",
        mindset: "Rate risks on current evidence, and attach responses to triggers people can see coming."
      },
      {
        id: "pmp-case3-005", type: "single", domain: "people", ecoTask: "people-4",
        approach: "hybrid", difficulty: 3, tags: ["stakeholders", "engagement", "safety", "archetype-act-now"],
        stem: "Dr Helen Park, the chief biomedical scientist at Site C, was not part of the requirements workshops. After a demonstration she says the new result-flagging rules are less safe than the ones her laboratory uses now. What should the project manager do?",
        options: [
          { id: "a", text: "Meet Dr Park to understand her concerns in detail, and involve her in reviewing the flagging rules with the configuration team before Site C's configuration is finalised.", correct: true,
            rationale: "She has expertise and a patient safety concern, and she was missed. Engaging her now uses her knowledge while it can still shape the configuration." },
          { id: "b", text: "Send Dr Park the signed-off requirements and explain that the flagging rules were agreed by the other two laboratories.", correct: false,
            rationale: "This is the consensus-as-closure error. An agreement she was not part of does not answer a safety concern, and it tells a key stakeholder her view arrives too late to count." },
          { id: "c", text: "Raise a change request to adopt Site C's current flagging rules across all three sites.", correct: false,
            rationale: "This is the adopt-the-complaint error. Her rules may be better, and changing the specification for every site before the concern is understood is premature." },
          { id: "d", text: "Ask the Site C laboratory manager to reassure Dr Park that the system has been tested.", correct: false,
            rationale: "This is the delegated-reassurance error. Testing against the specification does not address a concern that the specification itself is less safe." }
        ],
        explanation: "A stakeholder missed during requirements can still hold knowledge the project needs, and a safety concern from a senior specialist is a reason to engage rather than to defend the baseline. Bringing her into the review gives her concern a proper hearing and gives the programme her expertise while it can still change the outcome. Pointing to prior agreement, changing everything at once or sending reassurance all fail to engage her.",
        mindset: "Engage a missed stakeholder's expertise as soon as you find the gap, especially when safety is at stake."
      },
      {
        id: "pmp-case3-006", type: "single", domain: "business", ecoTask: "business-7",
        approach: "hybrid", difficulty: 3, tags: ["organisational-change", "resistance", "adoption", "archetype-diagnose-first"],
        stem: "At Site B, staff have started keeping a spreadsheet of results alongside the test system just in case, and several have told their manager they do not see why the change is needed. What should the project manager do?",
        options: [
          { id: "a", text: "Work with the Site B manager and respected staff to explain why the system is being replaced, listen to their concerns, and use local champions to support people through the change.", correct: true,
            rationale: "The workaround shows low confidence and the comments show the case for change has not landed. Explaining the reason and addressing concerns through trusted local people deals with both." },
          { id: "b", text: "Instruct staff to stop using the spreadsheet, since it duplicates patient results outside a controlled system.", correct: false,
            rationale: "This is the ban-the-symptom error. The control concern is valid, and a ban without addressing why staff feel they need the spreadsheet will drive the workaround out of sight." },
          { id: "c", text: "Bring forward the Site B training so staff become familiar with the new system sooner.", correct: false,
            rationale: "This is the training-cures-all error. Training builds ability, and these staff have not yet accepted why the change is needed, which training alone will not provide." },
          { id: "d", text: "Report the resistance to the programme board as a risk to the Site B go-live.", correct: false,
            rationale: "This is the report-instead-of-act error. Making the risk visible is useful, and it does nothing on its own to change how staff at Site B feel about the system." }
        ],
        explanation: "Organisational change succeeds when people understand why it is happening and feel supported through it. Workarounds and comments like these show that understanding and willingness are missing, which calls for explanation, listening and local sponsorship before training or rules can take hold. Banning, training or reporting alone address later stages of the change before the earlier ones are in place.",
        mindset: "Address why people resist before teaching them how to change."
      },
      {
        id: "pmp-case3-007", type: "single", domain: "process", ecoTask: "process-4",
        approach: "agile", difficulty: 3, tags: ["resources", "allocation", "single-point", "archetype-route-to-owner"],
        stem: "Kofi Mensah, the only interface specialist, is allocated half-time to an electronic prescribing project. The integration team's sprints keep stalling while it waits for his input. What should the project manager do?",
        options: [
          { id: "a", text: "Raise the shared allocation with Kofi's line manager and the other project's manager, using the integration dependencies and dates, and plan for another team member to build interface skills alongside him.", correct: true,
            rationale: "The constraint is a contested resource, which is settled by the people who allocate it, with evidence. Building a second person's skills reduces the dependency for the future." },
          { id: "b", text: "Ask Kofi to prioritise the integration work, since the laboratory system has the harder deadline.", correct: false,
            rationale: "This is the put-it-on-the-person error. Kofi does not own his allocation, and asking him to choose between projects passes a management decision to an individual." },
          { id: "c", text: "Ask the integration team to plan its sprints around Kofi's availability and accept the slower pace.", correct: false,
            rationale: "This is the absorb-the-constraint error. Adapting to a constraint is sometimes right, and doing so before trying to resolve it accepts a delay to milestones the board has not agreed." },
          { id: "d", text: "Hire a contract interface specialist to replace Kofi on the programme.", correct: false,
            rationale: "This is the buy-around-it error. A contractor may help later, and onboarding takes time, needs funding approval, and does not capture the knowledge Kofi already holds." }
        ],
        explanation: "When a shared resource becomes a constraint, the allocation decision belongs to whoever manages the resource, and the project manager's role is to bring evidence of impact to that conversation. Reducing dependence on a single person is the longer-term response. Putting the choice on the individual, quietly absorbing the delay or buying a replacement first all bypass the negotiation that should come first.",
        mindset: "Settle contested resources with the people who allocate them, and reduce single-person dependencies while you do."
      },
      {
        id: "pmp-case3-008", type: "single", domain: "people", ecoTask: "people-1",
        approach: "hybrid", difficulty: 3, tags: ["shared-purpose", "vendor", "collaboration", "archetype-challenge-the-premise"],
        stem: "The vendor's configuration team and the internal integration team now blame each other for interface defects found in testing. Defects are bouncing between the teams and taking longer to close. What should the project manager do?",
        options: [
          { id: "a", text: "Bring both teams together to agree a shared goal for interface quality, joint triage of defects, and a single definition of when an interface is working.", correct: true,
            rationale: "Blame thrives where teams have separate goals and separate measures. A shared goal with joint triage gives both teams the same outcome to work towards." },
          { id: "b", text: "Review the defect history and establish which team is causing most of the defects.", correct: false,
            rationale: "This is the find-the-culprit error. Knowing where defects originate can help later, and leading with it confirms that the teams are on opposite sides." },
          { id: "c", text: "Remind the vendor of its contractual obligations for defect resolution.", correct: false,
            rationale: "This is the contract-first error. The contract may be relevant, and invoking it here pushes the vendor team further into defending its position." },
          { id: "d", text: "Assign each defect to one team on arrival, based on which side of the interface it appears.", correct: false,
            rationale: "This is the draw-the-line error. A clearer allocation rule reduces bouncing, and interface defects often sit across the boundary, so the rule keeps the teams working apart." }
        ],
        explanation: "When teams from different organisations work on the same outcome, a shared purpose and shared measures matter more than the contractual boundary between them. Joint triage and a single definition of a working interface give both teams a reason to solve problems together. Investigating blame, pointing to contracts or drawing tighter lines all reinforce the divide.",
        mindset: "Give teams that share an outcome one goal and one measure, so problems become theirs together."
      },
      {
        id: "pmp-case3-009", type: "single", domain: "process", ecoTask: "process-2",
        approach: "hybrid", difficulty: 3, tags: ["scope", "change-request", "vendor", "archetype-route-to-owner"],
        stem: "The Site A laboratory manager has asked the vendor for an additional turnaround-time report. The vendor says it is outside the contracted specification and will need a variation. What should the project manager do?",
        options: [
          { id: "a", text: "Clarify the business need with the laboratory manager, then take the request through change control so its value, cost and effect on the milestone can be assessed.", correct: true,
            rationale: "A request outside the agreed specification is a scope change, and change control lets its value be weighed against its cost before anyone commits." },
          { id: "b", text: "Ask the vendor to add the report without charge, as a goodwill gesture given the size of the programme.", correct: false,
            rationale: "This is the free-scope error. Even if the vendor agrees, unrecorded additions blur the specification and can return later as claims or delays." },
          { id: "c", text: "Decline the request, since the specification was signed off and the contract is fixed-price.", correct: false,
            rationale: "This is the frozen-scope error. A signed specification does not make every change wrong, and the report may be worth its cost." },
          { id: "d", text: "Ask the integration team to build the report in its next sprint instead.", correct: false,
            rationale: "This is the route-around-the-contract error. Moving the work to the agile team avoids the variation, and it diverts capacity from interface work without anyone deciding the trade." }
        ],
        explanation: "Requests that fall outside an agreed scope are neither automatically accepted nor automatically refused. Understanding the need and assessing it through change control lets the right people decide whether its value justifies the cost and the schedule effect. Free additions, flat refusals and quietly moving the work elsewhere all bypass that decision.",
        mindset: "Treat every request outside the agreed scope as a decision to be made on its value, through the route that owns it."
      },
      {
        id: "pmp-case3-010", type: "single", domain: "people", ecoTask: "people-6",
        approach: "hybrid", difficulty: 2, tags: ["customers", "cutover", "expectations", "archetype-make-visible"],
        stem: "Clinicians on the wards have been told little about the cutover at Site A and expect turnaround times for test results to stay as they are. The cutover plan includes eight hours of reduced service. What should the project manager do?",
        options: [
          { id: "a", text: "Agree with the clinical leads what service they can expect during the cutover, including the downtime procedure for urgent tests, and make sure it reaches the wards before the date.", correct: true,
            rationale: "Clinicians are the laboratory's customers, and their expectation is now inaccurate. Agreeing the cutover service with their leads and communicating it in advance sets an expectation the programme can actually meet." },
          { id: "b", text: "Schedule the cutover overnight so that clinicians are less likely to notice the reduced service.", correct: false,
            rationale: "This is the hope-they-do-not-notice error. Choosing a quieter period is sensible, and relying on it in place of telling clinicians leaves urgent requests overnight exposed." },
          { id: "c", text: "Send an email to all ward staff the day before cutover describing the reduced service.", correct: false,
            rationale: "This is the late-notice error. One email the day before leaves no time for wards to plan or raise concerns, and urgent pathways need agreement, not notice." },
          { id: "d", text: "Ask the laboratories to keep the old system running in parallel during cutover so turnaround times are unchanged.", correct: false,
            rationale: "This is the avoid-the-trade-off error. Running both systems in parallel is a major design decision with its own risks, and it is no substitute for setting expectations honestly." }
        ],
        explanation: "Customer expectations are managed by agreeing in advance what will be delivered, particularly when service will temporarily get worse. For a cutover that affects clinical care, that means agreeing the temporary service level and the procedure for urgent cases with the people who represent the customers, and communicating it early. Timing tricks, late notice or expensive avoidance all leave expectations misaligned.",
        mindset: "When service will dip, agree the temporary level with your customers before the dip, not during it."
      }
    ]
  },

  /* ── CASE 4 ── predictive · museum gallery ───────────────────────────── */
  {
    id: "case-04",
    title: "Redeveloping a museum gallery",
    approach: "predictive",
    setting: "A museum refitting a listed gallery for a touring exhibition with a fixed opening date.",
    mix: { people: 3, process: 4, business: 3 },
    passage: [
      "The Fenwick Museum is redeveloping its east gallery, a listed nineteenth-century space, to host a touring exhibition of early scientific instruments. The loan agreement with the lending institution fixes the opening for 14 May and requires the gallery to have held stable temperature and humidity for eight weeks before the objects arrive. You are the project manager. The museum's director, Clare Whitfield, is the sponsor, and the work is funded from the museum's capital budget and a major gift from a private donor, Anthony Burke.",
      "A main contractor is carrying out the building works under a fixed-price contract. Twelve bespoke display cases are being made by a specialist fabricator under a separate fixed-price contract, with payment on delivery. The fabricator has recently lost two large orders, and a trade contact has told you it is struggling to pay its suppliers.",
      "Because the building is listed, any fixing into the original walls needs the consent of the local authority's conservation officer. The contractor has proposed a new method for fixing the case supports that differs from the approved method statement.",
      "The museum's curators, who will care for the objects, say the contractor's staff ignore the dust and vibration rules on site. The contractor's site manager says the curators keep changing their requirements. The in-house exhibition designers have worked long hours for six weeks to recover from late design approvals, and one has told you she is close to burning out.",
      "Anthony Burke has asked to approve the gallery layout and for the gallery to carry his family's name. The government has also announced changes to the national indemnity scheme that covers loaned objects, which could alter the conditions the lender sets.",
      "An earlier gallery project at the museum overran by three months for reasons its closure report attributed to late case delivery and environmental controls. Few of its lessons were acted on.",
      "Exhibit 4 shows the remaining schedule. The contingency reserve stands at 90,000 of an original 300,000."
    ],
    exhibits: [
      { id: "ex4", label: "Exhibit 4", title: "Remaining schedule, in working days",
        cols: ["Activity", "Duration", "Depends on", "Total float"],
        rows: [
          ["Building works complete", "20", "None", "0"],
          ["Environmental controls commissioned", "15", "Building works", "0"],
          ["Display cases delivered", "59", "None", "6"],
          ["Cases installed", "10", "Cases delivered; environmental controls", "6"],
          ["Eight-week stable period", "40", "Environmental controls", "0"],
          ["Objects installed", "10", "Stable period; cases installed", "0"]
        ],
        note: "Objects installed is the last activity before opening." }
    ],
    questions: [
      {
        id: "pmp-case4-001", type: "single", domain: "process", ecoTask: "process-8",
        approach: "predictive", difficulty: 3, tags: ["schedule", "float", "critical-path", "archetype-apply-the-rule"],
        stem: "The fabricator tells you the display cases will be delivered ten working days late. Using Exhibit 4, how will this affect the date the objects can be installed, if nothing else changes?",
        options: [
          { id: "a", text: "It moves four working days later, because the first six days of the delay are absorbed by the float on the case path.", correct: true,
            rationale: "Total float on case delivery is six days. The first six days of a ten-day slip are absorbed, and the remaining four push case installation past the end of the stable period and delay object installation." },
          { id: "b", text: "It does not move, because display case delivery is not on the critical path.", correct: false,
            rationale: "This is the off-the-path-means-safe error. An activity with float becomes critical once a delay exceeds its float, which a ten-day slip against six days does." },
          { id: "c", text: "It moves ten working days later, because object installation depends on the cases being installed.", correct: false,
            rationale: "This is the ignore-the-float error. The dependency is real, and the six days of float absorb part of the slip before it reaches the finish." },
          { id: "d", text: "It moves six working days later, because that is the float on the case path.", correct: false,
            rationale: "This is the float-as-delay error. Float is how much delay can be absorbed, not how much is passed on." }
        ],
        explanation: "Float measures how far an activity can slip before it delays the finish, so a slip larger than the float delays the finish by the difference and puts the activity on the critical path. Reading the effect correctly matters because it sets the size of the recovery needed: here four days, to be found by compressing case or object installation, not ten.",
        mindset: "Subtract the float before you announce a delay, then treat the slipped path as critical."
      },
      {
        id: "pmp-case4-002", type: "single", domain: "process", ecoTask: "process-5",
        approach: "predictive", difficulty: 4, tags: ["procurement", "supplier", "fixed-price", "archetype-diagnose-first"],
        stem: "A trade contact tells you the display case fabricator is struggling to pay its own suppliers. The cases are under a fixed-price contract with payment on delivery, and they are on the path to opening. What should the project manager do?",
        options: [
          { id: "a", text: "Work with procurement to verify the fabricator's position and progress, review the contract's terms on title, inspection and termination, and prepare a contingency plan in case the fabricator fails.", correct: true,
            rationale: "A credible warning about a critical supplier calls for verification and preparation. Knowing the contractual position and having a fallback ready protects the opening without prejudging the fabricator." },
          { id: "b", text: "Offer the fabricator an advance payment to help with its cash flow, so the cases are not delayed.", correct: false,
            rationale: "This is the fund-the-risk error. Paying ahead of delivery increases the museum's exposure if the fabricator fails, and it departs from the contract without the authority to do so." },
          { id: "c", text: "Terminate the contract and place the order with another fabricator before the situation worsens.", correct: false,
            rationale: "This is the act-on-rumour error. Termination on an unverified report risks a breach claim, and a new fabricator would almost certainly deliver later." },
          { id: "d", text: "Take no action, since the fixed-price contract and payment on delivery already protect the museum.", correct: false,
            rationale: "This is the contract-as-protection error. The contract protects the museum's money, not its opening date, and a failed supplier delivers nothing on time at any price." }
        ],
        explanation: "Fixed-price contracts move cost risk to the seller and leave schedule risk with the buyer if the seller cannot perform. When there is a credible warning about a critical supplier, the buyer verifies, understands its contractual rights and prepares a contingency, working through procurement. Advancing money, terminating early or relying on the contract all treat a risk to delivery as if it were a risk to price.",
        mindset: "A good contract protects the price; protect the date yourself by verifying early and preparing a fallback."
      },
      {
        id: "pmp-case4-003", type: "single", domain: "business", ecoTask: "business-2",
        approach: "predictive", difficulty: 3, tags: ["compliance", "heritage", "consent", "archetype-route-to-owner"],
        stem: "The contractor proposes a new method for fixing the display case supports into the original walls, different from the approved method statement. It would save a week. The building is listed. What should the project manager do?",
        options: [
          { id: "a", text: "Ask the contractor to submit a revised method statement, and seek the conservation officer's consent before any fixing to the original walls proceeds under the new method.", correct: true,
            rationale: "Work affecting the listed fabric needs consent, and the approved method is what the consent covered. Seeking consent for the change keeps the saving available without risking the building or the museum's legal position." },
          { id: "b", text: "Approve the new method, since it saves a week and the contractor is responsible for the quality of the works.", correct: false,
            rationale: "This is the schedule-over-consent error. The contractor's responsibility for quality does not extend to the museum's legal obligations for a listed building." },
          { id: "c", text: "Reject the new method, since the approved method statement cannot be changed.", correct: false,
            rationale: "This is the frozen-method error. Method statements can change with the right consent, and rejecting outright gives up a week the schedule may need." },
          { id: "d", text: "Let the contractor trial the new method on one support and seek consent if it works.", correct: false,
            rationale: "This is the test-on-the-heritage error. A trial fixing into the original wall is itself work that needs consent, and any damage cannot be undone." }
        ],
        explanation: "Compliance obligations attach to the work as it is actually done, so a change to an approved method on a protected asset needs the relevant consent before it is used. The project can still pursue the time saving through the approval route. Approving, rejecting or trialling without consent each treat a legal requirement as something the project can settle on its own.",
        mindset: "When a change touches a legal consent, get the consent before the change touches the asset."
      },
      {
        id: "pmp-case4-004", type: "single", domain: "process", ecoTask: "process-6",
        approach: "predictive", difficulty: 4, tags: ["finance", "contingency", "reserves", "archetype-make-visible"],
        stem: "Contingency reserve stands at 90,000 of an original 300,000, with three months of building works left. The risks still open on the register have a combined expected value of about 140,000. What should the project manager do?",
        options: [
          { id: "a", text: "Report the shortfall to Clare with the risk analysis behind it, and set out the options, including responses that reduce the exposure and a request for management reserve if the risks occur.", correct: true,
            rationale: "The remaining reserve no longer covers the expected exposure. Making that visible to the sponsor with options gives her time to decide while the choices are still open." },
          { id: "b", text: "Draw on the museum's management reserve now to bring contingency back to its original level.", correct: false,
            rationale: "This is the self-authorised-reserve error. Management reserve is released by the sponsor or governance body, not drawn by the project manager, and only against need." },
          { id: "c", text: "Reduce the specification of the remaining works to free up budget for contingency.", correct: false,
            rationale: "This is the quiet-descope error. Scope reductions to fund contingency change what the museum receives and need the sponsor's approval." },
          { id: "d", text: "Monitor the contingency and report only if it is exhausted.", correct: false,
            rationale: "This is the report-at-zero error. Waiting until the reserve is gone removes the time in which responses can be taken and reserves requested." }
        ],
        explanation: "Contingency reserve is sized to cover identified risks, so when what is left falls below the expected value of the risks still open, the gap is a forecast problem the sponsor needs to see. The project manager's job is to show the analysis and the options, including ways to reduce the exposure, while management reserve stays under the sponsor's control. Drawing reserves, cutting scope quietly or waiting all take decisions away from the people who own them.",
        mindset: "Compare what is left in reserve with what is still at risk, and raise the gap while the sponsor can still act."
      },
      {
        id: "pmp-case4-005", type: "single", domain: "people", ecoTask: "people-5",
        approach: "predictive", difficulty: 4, tags: ["expectations", "donor", "sponsor", "archetype-route-to-owner"],
        stem: "Anthony Burke, the major donor, has asked to approve the gallery layout and for the gallery to carry his family's name. Neither is mentioned in the gift agreement. What should the project manager do?",
        options: [
          { id: "a", text: "Raise both requests with Clare, who holds the relationship and the authority, and support her in agreeing with Mr Burke what role he will have, so his expectations are clear and recorded.", correct: true,
            rationale: "Naming and approval rights are matters for the museum's leadership and its gift policies. The project manager's role is to see that the expectation is resolved by the right person and written down, not to settle it alone." },
          { id: "b", text: "Agree to show Mr Burke the layout for approval, since his gift is a significant part of the funding.", correct: false,
            rationale: "This is the pay-to-decide error. Granting approval rights outside the gift agreement creates an authority nobody has sanctioned and may conflict with the curators' obligations to the lender." },
          { id: "c", text: "Tell Mr Burke politely that layout and naming are outside the scope of the project.", correct: false,
            rationale: "This is the scope-as-shield error. The project manager does not hold the donor relationship, and a refusal from the wrong person can damage it." },
          { id: "d", text: "Reflect Mr Burke's comments in the layout design without formally agreeing his role.", correct: false,
            rationale: "This is the informal-concession error. It gives him influence without clarity, and his expectations are likely to grow while nobody has agreed what they are." }
        ],
        explanation: "Aligning expectations with an influential stakeholder means making clear what role they have, through the person who holds the relationship and the authority. Requests outside an agreement are neither granted nor refused by the project manager on the spot; they are taken to the sponsor and resolved explicitly. Granting, refusing or quietly accommodating all leave the stakeholder's expectations unmanaged.",
        mindset: "When an influential stakeholder asks for more, make the role explicit through the person who holds the authority."
      },
      {
        id: "pmp-case4-006", type: "single", domain: "people", ecoTask: "people-2",
        approach: "predictive", difficulty: 3, tags: ["conflict", "site-rules", "collaboration", "archetype-diagnose-first"],
        stem: "The curators say the contractor's staff ignore the dust and vibration rules on site. The contractor's site manager says the curators keep changing their requirements. The disagreement is slowing work in the gallery. What should the project manager do?",
        options: [
          { id: "a", text: "Bring the lead curator and the site manager together to agree one set of site rules, how changes to them will be requested, and how compliance will be checked.", correct: true,
            rationale: "Both complaints point to the same gap: rules that are neither stable nor shared. Agreeing them jointly, with a route for changes, addresses both sides and gives each something to rely on." },
          { id: "b", text: "Instruct the contractor to follow the curators' rules, since the safety of the objects comes first.", correct: false,
            rationale: "This is the pick-a-side error. Object safety does matter, and imposing whatever the curators currently say leaves the contractor's legitimate complaint about changing requirements unaddressed." },
          { id: "c", text: "Ask the curators to stop issuing new requirements until the building works are complete.", correct: false,
            rationale: "This is the freeze-one-side error. Some requirements may need to change for the objects' safety, and silencing the curators shifts the risk onto the collection." },
          { id: "d", text: "Report the conflict to Clare and ask her to settle it.", correct: false,
            rationale: "This is the escalate-too-early error. The project manager can resolve a disagreement about site rules, and sending it to the director spends her attention on something the project should handle." }
        ],
        explanation: "Conflicts between groups often rest on a shared gap that each sees from its own side. Bringing them together to agree the rules, how changes are made and how compliance is checked addresses the cause rather than choosing a winner. Siding with one party, silencing the other or escalating a dispute the project can solve all leave the gap in place.",
        mindset: "When both sides of a conflict describe the same gap, close the gap together rather than choosing a side."
      },
      {
        id: "pmp-case4-007", type: "multi", domain: "business", ecoTask: "business-8",
        approach: "predictive", difficulty: 4, tags: ["external", "indemnity", "risk", "archetype-make-visible"],
        stem: "The government has announced changes to the national indemnity scheme for loaned objects, which could alter the conditions the lender sets for the exhibition. The details will be published next month. Which two actions should the project manager take? Select two.",
        options: [
          { id: "a", text: "Ask the museum's registrar to assess with the lender how the changes might affect the loan conditions, and record the possible effects as a risk with an owner.", correct: true,
            rationale: "The impact depends on what the lender will require, which the registrar can explore. Recording it as a risk makes the uncertainty visible and owned while details are pending." },
          { id: "b", text: "Brief Clare on the possible effects on the loan and the opening date, so she can take the decisions that sit with the museum's leadership.", correct: true,
            rationale: "An external change that could affect a loan agreement reaches beyond the project. The sponsor needs early sight to engage the lender or insurers at the right level." },
          { id: "c", text: "Wait for the details to be published before taking any action, since the effect cannot yet be known.", correct: false,
            rationale: "This is the wait-for-certainty error. Early assessment can identify likely effects and lead times, which waiting gives up." },
          { id: "d", text: "Arrange commercial insurance for the objects now, in case indemnity no longer applies.", correct: false,
            rationale: "This is the pre-emptive-spend error. Buying cover before the effect is known commits money and may not meet the lender's eventual requirements." },
          { id: "e", text: "Ask the lender to confirm that the existing loan conditions will not change.", correct: false,
            rationale: "This is the demand-a-guarantee error. The lender cannot confirm conditions that depend on rules not yet published, and asking puts strain on the relationship." }
        ],
        explanation: "External changes that may affect a project's commitments should be assessed early, recorded as risks with owners, and brought to the attention of those who govern the commitments. That lets responses be prepared while the details are uncertain. Waiting, spending pre-emptively or seeking guarantees that cannot be given all mismanage the uncertainty.",
        mindset: "Treat an announced external change as a risk to assess now, not a fact to wait for."
      },
      {
        id: "pmp-case4-008", type: "multi", domain: "process", ecoTask: "process-10",
        approach: "predictive", difficulty: 3, tags: ["closure", "handover", "acceptance", "archetype-apply-the-rule"],
        stem: "The gallery is nearing completion. Which two conditions should be met before the project formally hands the gallery over to the museum's visitor services team? Select two.",
        options: [
          { id: "a", text: "The deliverables have been accepted against their acceptance criteria, with any outstanding defects recorded and agreed with the receiving team.", correct: true,
            rationale: "Formal acceptance against agreed criteria is what makes handover a controlled transfer. An agreed defects list lets minor items be finished after handover without ambiguity." },
          { id: "b", text: "Visitor services staff have been trained on the gallery's operation, including the environmental controls they will monitor and the procedures for the loaned objects.", correct: true,
            rationale: "The receiving team must be able to run what it receives, and the loan's environmental conditions make operational readiness a contractual matter, not a courtesy." },
          { id: "c", text: "The full lessons learned report for the project has been approved by Clare.", correct: false,
            rationale: "This is the closure-before-handover error. Lessons learned are part of closure and can be completed after handover; making them a condition delays the transfer for no operational benefit." },
          { id: "d", text: "Every snagging item, however minor, has been completed by the contractor.", correct: false,
            rationale: "This is the perfect-before-handover error. Minor snags can be agreed and finished afterwards, and insisting on none can delay handover past the date the stable period depends on." },
          { id: "e", text: "The final account with the main contractor has been agreed and settled.", correct: false,
            rationale: "This is the commercial-gate error. Settling the final account often takes months and is a separate closure activity, not a condition for operations to take over." }
        ],
        explanation: "Handover transfers responsibility for a deliverable to the people who will operate it, so it depends on two things: formal acceptance against agreed criteria, and the receiving team's ability to run what it receives. Closure activities such as lessons learned and the final account follow on their own timelines, and minor defects can be managed through an agreed list.",
        mindset: "Hand over when the deliverable is accepted and the receivers can run it, and finish closure on its own timeline."
      },
      {
        id: "pmp-case4-009", type: "single", domain: "business", ecoTask: "business-6",
        approach: "predictive", difficulty: 3, tags: ["continuous-improvement", "lessons-learned", "risk", "archetype-act-now"],
        stem: "The closure report from the museum's earlier gallery project attributed a three-month overrun to late case delivery and environmental controls. Few of its lessons were acted on, and your project now faces risks in both areas. What should the project manager do?",
        options: [
          { id: "a", text: "Review the earlier lessons with the team, turn the relevant ones into specific actions in the current plan and risk responses, and propose to Clare a way for future projects to act on lessons.", correct: true,
            rationale: "Lessons improve outcomes only when they change what is done. Applying them now helps this project, and fixing the gap in how lessons are used helps the projects that follow." },
          { id: "b", text: "Attach the earlier closure report to the current risk register for reference.", correct: false,
            rationale: "This is the file-it-again error. Attaching the report repeats what happened last time: the lessons are recorded and not used." },
          { id: "c", text: "Add a section to this project's closure report noting that the earlier lessons were not acted on.", correct: false,
            rationale: "This is the record-the-failure error. Noting the gap at the end does nothing for this project and is likely to be filed in the same way." },
          { id: "d", text: "Ask the earlier project's manager to join the current project as an adviser.", correct: false,
            rationale: "This is the person-as-process error. Their experience may help, and it relies on one individual rather than building the lessons into the plan and the organisation's practice." }
        ],
        explanation: "Continuous improvement depends on lessons changing behaviour, which means turning them into actions in the current plan and improving the organisation's way of using them. Recording or re-recording lessons, or relying on individuals, leaves the learning where it was.",
        mindset: "A lesson counts only when it changes the plan, so make it change this one and the process for the next."
      },
      {
        id: "pmp-case4-010", type: "single", domain: "people", ecoTask: "people-3",
        approach: "predictive", difficulty: 3, tags: ["leadership", "wellbeing", "workload", "archetype-act-now"],
        stem: "The in-house exhibition designers have worked long hours for six weeks to recover from late design approvals. One of them has told you privately that she is close to burning out. What should the project manager do?",
        options: [
          { id: "a", text: "Talk with her about what support she needs, review the designers' workload with the team, and agree a sustainable plan, raising any effect on the schedule with Clare.", correct: true,
            rationale: "Her wellbeing comes first, and six weeks of overtime is a team-level problem. Replanning the work sustainably, and being honest with the sponsor about the trade, addresses both." },
          { id: "b", text: "Thank her for raising it and encourage her to take some leave once the gallery opens.", correct: false,
            rationale: "This is the defer-the-person error. The opening is months away, and deferring support leaves her at risk now while the cause carries on." },
          { id: "c", text: "Move her to lighter duties and redistribute her work among the other designers.", correct: false,
            rationale: "This is the shift-the-load error. It helps her in the short term and increases the strain on colleagues who have been working the same hours." },
          { id: "d", text: "Ask the team to keep going until the design catches up, then return to normal hours.", correct: false,
            rationale: "This is the one-more-push error. Sustained overtime has already pushed one person to the edge, and asking for more increases errors and the risk of losing people." }
        ],
        explanation: "Sustained overtime is a sign that the plan does not fit the team's capacity, and a team member near burnout is the point at which the plan, not the person, needs to change. Leading the team means supporting the individual, replanning the workload with the team and being honest with the sponsor about what that does to the schedule. Deferring help, redistributing strain or asking for one more push leave the underlying problem in place.",
        mindset: "When the team is running on overtime, change the plan before the plan changes the team."
      }
    ]
  },

  /* ── CASE 5 ── hybrid · insurer merger ───────────────────────────────── */
  {
    id: "case-05",
    title: "Bringing two customer service centres together",
    approach: "hybrid",
    setting: "A merged insurer moving two contact centres onto one platform and one way of working.",
    mix: { people: 4, process: 3, business: 3 },
    passage: [
      "Two regional insurers, Northway and Calder Mutual, merged last year. The combined company is bringing its two customer service centres onto one customer records and telephony platform and one operating model, so that customers get the same service whichever centre answers. You are the project manager. The sponsor is the chief operating officer, Daniel Price.",
      "The platform configuration is being done by an internal agile team in two-week sprints, with releases roughly monthly. The rest of the work follows a fixed plan: moving staff onto the new platform centre by centre, training around 400 advisers, and changes to some roles. Roles cannot be changed until a formal staff consultation, required by employment law, has run its full 45 days. The consultation starts next week.",
      "Northway's centre measures success by average call handling time; Calder's by the proportion of queries resolved on first contact. The two centre managers, Ruth Ellis and Sam Okafor, each argue that their measure should be the one used.",
      "A staff survey last month asked advisers in both centres about the change (Exhibit 5). Adviser turnover at the Calder centre has risen. The human resources director wants to tell Calder advisers about their new roles at next week's team meetings to stop the rumours.",
      "Training rooms and trainers are limited, and the busiest period for claims, the winter storm season, begins in eight weeks. Since the first centre moved, customers have been waiting longer on the phone and complaints have risen. The agile team has been unable to test telephony features for two sprints because the telephony supplier has not given it access to its test environment.",
      "The steering committee wants a single red, amber or green status for the project. Two advisers from the Calder centre who joined the agile team say little in retrospectives, although they are among the most experienced people on it."
    ],
    exhibits: [
      { id: "ex5", label: "Exhibit 5", title: "Staff survey results, percentage agreeing",
        cols: ["Statement", "Northway centre", "Calder centre"],
        rows: [
          ["I understand why the change is happening", "78", "41"],
          ["I know what my role will be after the change", "30", "22"],
          ["I trust the information I receive about the change", "64", "35"]
        ],
        note: "Response rates: Northway 71 per cent, Calder 58 per cent." }
    ],
    questions: [
      {
        id: "pmp-case5-001", type: "single", domain: "business", ecoTask: "business-7",
        approach: "hybrid", difficulty: 3, tags: ["organisational-change", "survey", "trust", "archetype-diagnose-first"],
        stem: "Exhibit 5 shows that only 41 per cent of Calder advisers understand why the change is happening, against 78 per cent at Northway, and trust in the information they receive is also lower at Calder. What should the project manager do?",
        options: [
          { id: "a", text: "Work with Sam Okafor and Daniel to explain the reasons for the change directly to Calder advisers, in sessions where they can ask questions, and use those questions to shape what is communicated next.", correct: true,
            rationale: "The gap is understanding and trust, both of which grow through direct, two-way contact with credible leaders. Listening also shows what is driving the low trust." },
          { id: "b", text: "Send Calder advisers the same briefing pack that Northway advisers received, since it worked well there.", correct: false,
            rationale: "This is the same-message error. The pack may be fine, and trust in the information is exactly what is low at Calder, so more information through the same channel is unlikely to land." },
          { id: "c", text: "Focus on role clarity, since both centres score lowest on knowing what their role will be.", correct: false,
            rationale: "This is the wrong-gap error. Role clarity is low everywhere and is constrained by the consultation; the gap that sets Calder apart is understanding and trust." },
          { id: "d", text: "Repeat the survey in a month to see whether the scores improve.", correct: false,
            rationale: "This is the measure-instead-of-act error. Surveying again shows whether things have changed without doing anything to change them." }
        ],
        explanation: "Readiness for change depends on people understanding why it is happening and trusting what they are told. Where those scores are low, the response is direct, two-way engagement with credible leaders, shaped by what people actually ask. Re-sending materials, working on a different gap or measuring again without acting all miss the evidence in front of you.",
        mindset: "Read change-readiness data for the gap that sets a group apart, and close it through leaders that group trusts."
      },
      {
        id: "pmp-case5-002", type: "single", domain: "people", ecoTask: "people-1",
        approach: "hybrid", difficulty: 3, tags: ["shared-purpose", "measures", "alignment", "archetype-challenge-the-premise"],
        stem: "Ruth Ellis, who runs the Northway centre, wants success measured by average call handling time. Sam Okafor, who runs the Calder centre, wants it measured by first-contact resolution. Each is coaching advisers to their own measure. What should the project manager do?",
        options: [
          { id: "a", text: "Bring Ruth, Sam and Daniel together to agree what good service means for the combined company, and the small set of measures that reflect it, before advisers are trained to a single model.", correct: true,
            rationale: "The disagreement is about purpose, not arithmetic. Agreeing the outcome first, with the sponsor present, gives both managers a shared target from which the measures follow." },
          { id: "b", text: "Adopt first-contact resolution, since it reflects the customer's experience more closely.", correct: false,
            rationale: "This is the pick-the-better-measure error. The choice may even be right, and imposing it leaves one centre manager overruled and the shared purpose still missing." },
          { id: "c", text: "Use both measures, with each centre keeping its own until the platform is fully live.", correct: false,
            rationale: "This is the postpone-alignment error. Two centres coached to different targets on one platform will drift further apart, and the operating model is meant to be single." },
          { id: "d", text: "Ask the human resources director to decide, since the measures will appear in advisers' objectives.", correct: false,
            rationale: "This is the wrong-owner error. Measures of service are an operational decision for the sponsor; human resources applies them to objectives once they are agreed." }
        ],
        explanation: "A team that does not share a definition of success cannot be led towards one. When leaders pursue different measures, the useful step is to agree the outcome with the sponsor and derive a balanced set of measures from it. Imposing one measure, keeping both or handing the decision to a function that does not own it leaves the purpose unresolved.",
        mindset: "Agree what success means before arguing about how to measure it."
      },
      {
        id: "pmp-case5-003", type: "pulldown", domain: "people", ecoTask: "people-8",
        approach: "hybrid", difficulty: 2, tags: ["communication", "channels", "stakeholders", "archetype-apply-the-rule"],
        stem: "Complete each sentence with the communication approach that fits the Northway and Calder case. Two choices are not used.",
        prompts: [
          { id: "c1", text: "To tell Calder advisers how the consultation will work and when they will hear about roles, the project should use" },
          { id: "c2", text: "To keep the steering committee informed between its meetings, the project should use" },
          { id: "c3", text: "To agree the cutover details for the second centre with Ruth and Sam, the project should use" }
        ],
        options: [
          { id: "brief", text: "a briefing led by their own manager, with time for questions", correct: true, match: "c1",
            rationale: "Advisers anxious about their roles need a credible messenger and a chance to ask. A manager-led briefing gives both, and the content stays within what the consultation allows." },
          { id: "report", text: "a short written report with the status, the forecast and any decision needed", correct: true, match: "c2",
            rationale: "A steering committee needs a concise, consistent record it can act on between meetings. A written report gives that without calling a meeting." },
          { id: "workshop", text: "a working session to plan it together", correct: true, match: "c3",
            rationale: "Cutover details need decisions from both managers and depend on each other's constraints. A working session settles them together rather than passing them back and forth." },
          { id: "email", text: "an all-staff email from the project team", correct: false, match: null,
            rationale: "Not used. A mass email suits simple announcements, and none of these needs an announcement: each needs dialogue, a decision record or joint planning." },
          { id: "poster", text: "posters in each centre summarising the project timeline", correct: false, match: null,
            rationale: "Not used. Posters reinforce messages already given, and they cannot answer questions, record decisions or settle details." }
        ],
        explanation: "Communication methods should be chosen for what each audience needs from the exchange: reassurance and questions for anxious staff, a decision-ready record for governance, and joint working for people who must agree details. Broadcast channels such as mass emails and posters have their uses, and they cannot do any of those three jobs well.",
        mindset: "Pick the channel for the job the message must do: answer, record or decide."
      },
      {
        id: "pmp-case5-004", type: "single", domain: "business", ecoTask: "business-2",
        approach: "hybrid", difficulty: 4, tags: ["compliance", "consultation", "employment-law", "archetype-challenge-the-premise"],
        stem: "The human resources director wants to tell Calder advisers about their new roles at next week's team meetings to stop the rumours. The 45-day staff consultation on the role changes, required by employment law, starts next week. What should the project manager do?",
        options: [
          { id: "a", text: "Explain to the human resources director that roles cannot be confirmed before the consultation has run its course, and agree with her a message for next week that sets out the consultation process and timetable honestly.", correct: true,
            rationale: "Announcing outcomes before consultation would breach the legal requirement and undermine the consultation itself. A truthful message about the process addresses the rumours without pre-empting the result." },
          { id: "b", text: "Support the announcement, since clarity about roles would address the low scores in Exhibit 5.", correct: false,
            rationale: "This is the ends-justify-the-means error. Role clarity is needed, and providing it by announcing outcomes before a legally required consultation exposes the company and damages trust further." },
          { id: "c", text: "Say nothing to advisers until the consultation is complete, so nothing can be misinterpreted.", correct: false,
            rationale: "This is the silence error. Saying nothing through 45 days of rumour will deepen the mistrust the survey already shows." },
          { id: "d", text: "Leave the decision to the human resources director, since consultation is a human resources matter.", correct: false,
            rationale: "This is the not-my-domain error. The consultation is on the project's critical path, and the project manager shares responsibility for compliant delivery, so raising the concern is part of the role." }
        ],
        explanation: "Legal requirements such as a statutory consultation constrain both what can be done and when it can be said. The project manager's role is to keep the project compliant while still meeting the legitimate need behind a request, here by communicating openly about the process rather than its outcome. Announcing early, staying silent or stepping aside each fail one side or the other.",
        mindset: "Meet the need behind a request in a way the law allows, and say plainly what cannot yet be said."
      },
      {
        id: "pmp-case5-005", type: "single", domain: "people", ecoTask: "people-6",
        approach: "hybrid", difficulty: 3, tags: ["customers", "service-levels", "transition", "archetype-diagnose-first"],
        stem: "Since the first centre moved onto the new platform, customers have been waiting longer on the phone and complaints have risen. The second centre is due to move in four weeks. What should the project manager do?",
        options: [
          { id: "a", text: "Analyse with the operations team why waiting times have risen, agree with Daniel the service level customers should receive during the transition, and plan the next move with measures to protect it.", correct: true,
            rationale: "Customers' expectations are being missed now. Understanding the cause, agreeing an acceptable temporary level with the sponsor, and building protection into the next move address both the current dip and the next one." },
          { id: "b", text: "Proceed with the second move as planned, since some dip in service is normal during a transition.", correct: false,
            rationale: "This is the normalise-the-dip error. A dip may be expected, and proceeding without knowing its cause risks doubling it when the second centre moves." },
          { id: "c", text: "Pause the second move indefinitely until waiting times return to their previous level.", correct: false,
            rationale: "This is the stop-everything error. A pause may be part of the answer, and an indefinite one without analysis can prolong the transition and its costs." },
          { id: "d", text: "Ask the communications team to tell customers that waiting times will improve once the change is complete.", correct: false,
            rationale: "This is the message-instead-of-fix error. Promising customers improvements that have not been planned sets an expectation the project may not meet." }
        ],
        explanation: "During a transition, the effect on customers must be managed as deliberately as any other deliverable. That means diagnosing the cause of a service dip, agreeing with the sponsor what level of service is acceptable while the change completes, and planning the next step to protect it. Pressing on, stopping without analysis or reassuring customers without a plan all leave expectations unmanaged.",
        mindset: "Protect the customer through a transition by agreeing the service they can expect and planning to meet it."
      },
      {
        id: "pmp-case5-006", type: "single", domain: "process", ecoTask: "process-4",
        approach: "hybrid", difficulty: 3, tags: ["resources", "training", "levelling", "archetype-trade-off"],
        stem: "About 400 advisers need training before the second centre moves. Training rooms and trainers are limited, and the winter storm season, when claims are at their busiest, begins in eight weeks. What should the project manager do?",
        options: [
          { id: "a", text: "Build a training schedule with both centre managers that fits the room and trainer capacity, protects staffing through the storm season, and shows what that means for the move dates.", correct: true,
            rationale: "Resource limits and operational peaks are both constraints. Planning them together, with the people who manage staffing, produces a schedule that can be delivered and makes the effect on dates visible." },
          { id: "b", text: "Train every adviser in the next eight weeks, before the storm season begins.", correct: false,
            rationale: "This is the cram-it-in error. With limited rooms and trainers, compressing all training into eight weeks may exceed capacity and leave the centres short-staffed at the worst moment." },
          { id: "c", text: "Postpone all training until after the storm season, and move the second centre then.", correct: false,
            rationale: "This is the all-or-nothing error. It may be part of the answer, and deciding it without exploring what capacity allows gives up options and delays the benefits." },
          { id: "d", text: "Replace classroom training with self-study materials so that every adviser can be trained at once.", correct: false,
            rationale: "This is the change-the-method-to-fit error. Self-study may supplement training, and swapping it in wholesale to fit a constraint risks advisers serving customers on a platform they have not practised on." }
        ],
        explanation: "Resource planning balances demand against the capacity available and the operational constraints around it. Here, limited rooms and trainers and the need to keep advisers on the phones through the storm season mean the training plan, the staffing plan and the move dates must be planned together. Cramming, postponing everything or changing the method to fit all avoid that balancing.",
        mindset: "Plan scarce resources against the real constraints, and show the dates that result rather than the dates that were hoped for."
      },
      {
        id: "pmp-case5-007", type: "multi", domain: "process", ecoTask: "process-9",
        approach: "hybrid", difficulty: 3, tags: ["status", "reporting", "governance", "archetype-make-visible"],
        stem: "The steering committee wants a single red, amber or green status for the project. Configuration is on track, the first centre's move is complete but customer waiting times have worsened, and adviser turnover at Calder is rising. Which two actions should the project manager take? Select two.",
        options: [
          { id: "a", text: "Agree with the committee what each colour means for this project, including measures of customer service and staff readiness as well as delivery progress.", correct: true,
            rationale: "A status is only as useful as the definition behind it. Agreed definitions that include outcomes stop green delivery from hiding amber service or red adoption." },
          { id: "b", text: "Show the overall status alongside a short breakdown of delivery, service and people measures, with the reasons for any amber or red.", correct: true,
            rationale: "A single colour meets the committee's need for a headline, and the breakdown keeps the different dimensions visible so the committee can act on the right one." },
          { id: "c", text: "Report green, since configuration is on track and the first move is complete.", correct: false,
            rationale: "This is the delivery-only error. It reports what has been built and leaves out what has happened to customers and staff, which are the outcomes the change exists for." },
          { id: "d", text: "Report red, so the committee gives the project its full attention.", correct: false,
            rationale: "This is the alarm-for-attention error. Overstating the position to win attention erodes the meaning of the status and trust in future reports." },
          { id: "e", text: "Decline to give a single status, since the project's position cannot be summarised in one colour.", correct: false,
            rationale: "This is the refuse-the-format error. The committee's need for a headline is legitimate; the answer is to make the headline meaningful, not to withhold it." }
        ],
        explanation: "Status reporting must be truthful, consistent and useful to the people who govern the project. A single colour can serve that purpose if its meaning is agreed and it is supported by the measures that matter, including outcomes for customers and staff. Reporting only delivery, exaggerating or refusing the format all reduce the committee's ability to govern.",
        mindset: "Make a headline status mean something by agreeing what it measures and showing what sits beneath it."
      },
      {
        id: "pmp-case5-008", type: "single", domain: "business", ecoTask: "business-4",
        approach: "agile", difficulty: 3, tags: ["impediments", "supplier", "escalation", "archetype-route-to-owner"],
        stem: "For two sprints the agile team has been unable to test telephony features because the telephony supplier has not given it access to its test environment. The team has raised the problem at every daily scrum. What should the project manager do?",
        options: [
          { id: "a", text: "Escalate the access problem to whoever manages the telephony supplier relationship, with its effect on the release plan, and agree a date by which access will be provided.", correct: true,
            rationale: "The impediment is outside the team's control and has persisted for two sprints. Escalating with evidence of impact to the person who can act on the supplier is the step the team cannot take for itself." },
          { id: "b", text: "Ask the team to keep raising the problem at the daily scrum until the supplier responds.", correct: false,
            rationale: "This is the repeat-the-signal error. The daily scrum surfaces impediments; it does not remove the ones that sit outside the team." },
          { id: "c", text: "Ask the team to mark the telephony features as done without testing, so the release plan can proceed.", correct: false,
            rationale: "This is the lower-the-bar error. Untested features do not meet the definition of done and move the risk into live service." },
          { id: "d", text: "Ask the team to build a simulated telephony environment of its own, to remove the dependency.", correct: false,
            rationale: "This is the build-around-it error. A simulation may help later, and building one diverts capacity and may not reproduce the supplier's real behaviour." }
        ],
        explanation: "Removing impediments that the team cannot remove itself is a central part of the project manager's role in agile delivery, and an impediment that has lasted two sprints has outlasted the team's own channels. Escalating it to the person who can act, with its impact on the plan, is the step that moves it. Repeating it, lowering quality or building workarounds all leave the cause in place.",
        mindset: "When the team has raised an impediment it cannot clear, take it to the person who can."
      },
      {
        id: "pmp-case5-009", type: "single", domain: "people", ecoTask: "people-3",
        approach: "agile", difficulty: 2, tags: ["leadership", "psychological-safety", "retrospectives", "archetype-diagnose-first"],
        stem: "Two advisers from the Calder centre who joined the agile team say little in retrospectives, although they raise useful points with you informally afterwards. The rest of the team are Northway staff or developers. What should the project manager do?",
        options: [
          { id: "a", text: "Encourage them to bring their points to the team, and change how retrospectives are run, for example with silent writing or small-group discussion, so every voice is heard.", correct: true,
            rationale: "Their points have value, and the format is not drawing them out. Changing how input is gathered builds the safety for them to contribute without singling them out." },
          { id: "b", text: "Raise their points in the retrospective yourself, without saying where they came from.", correct: false,
            rationale: "This is the speak-for-them error. It gets the points heard once and keeps the advisers silent, while the team never learns that they have something to say." },
          { id: "c", text: "Ask them directly in the retrospective what they think, so they are given space to speak.", correct: false,
            rationale: "This is the spotlight error. Putting quiet people on the spot in front of a group they do not yet trust can make them less likely to speak." },
          { id: "d", text: "Accept their quietness, since some people prefer to contribute informally.", correct: false,
            rationale: "This is the accept-the-silence error. Preference may be part of it, and in a team drawn from two former companies their silence may also reflect a lack of safety worth addressing." }
        ],
        explanation: "Teams perform better when every member can contribute openly, and in a team drawn from two merged organisations psychological safety cannot be assumed. Adjusting how input is gathered, and encouraging people to bring their ideas to the group, builds that safety. Speaking for people, putting them on the spot or accepting silence each leave the barrier in place.",
        mindset: "When people speak only in private, change the room until they can speak in it."
      },
      {
        id: "pmp-case5-010", type: "single", domain: "process", ecoTask: "process-3",
        approach: "agile", difficulty: 4, tags: ["value", "release", "business-case", "archetype-trade-off"],
        stem: "The agile team's next monthly release could include either the shared call-routing features that let either centre answer any customer, or a set of improvements to the advisers' screens that advisers have asked for. Daniel's business case rests on customers getting the same service whichever centre answers. What should the project manager advise?",
        options: [
          { id: "a", text: "Prioritise the call-routing features, since they deliver the outcome the business case depends on, and plan the screen improvements for the following release with a clear explanation to advisers.", correct: true,
            rationale: "Value is judged against the outcome the investment exists to achieve. Routing is what makes the single service possible, and explaining the sequence respects the advisers' requests." },
          { id: "b", text: "Prioritise the screen improvements, since adviser support is critical given the survey results.", correct: false,
            rationale: "This is the morale-over-outcome error. Advisers' needs matter, and the screen changes do not deliver the outcome the business case is built on." },
          { id: "c", text: "Split the release so that half of each set is delivered, to show progress on both.", correct: false,
            rationale: "This is the half-of-everything error. Half of the routing features may deliver none of their value, and splitting delays both outcomes." },
          { id: "d", text: "Ask the team which set it would prefer to build, since it is a self-organising team.", correct: false,
            rationale: "This is the misplaced-ownership error. The team decides how to build; what to deliver first is a value decision for the product owner and the sponsor." }
        ],
        explanation: "Delivering value means sequencing work by its contribution to the outcome the investment exists to achieve, and being open about what is deferred. Routing enables the single service the business case depends on; the screen improvements matter and can follow. Choosing by morale, splitting everything or handing a value decision to the team all lose sight of the outcome.",
        mindset: "Sequence releases by the outcome the investment is for, and tell the people who must wait why."
      }
    ]
  }

];
