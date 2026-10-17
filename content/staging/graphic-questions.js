/* Staged for §05.2 PMP.QUESTIONS — twelve "graphic" items.
   Splice: copy the twelve objects between the outer [ and ] below and paste
   them at the end of PMP.QUESTIONS, after the last item and before the
   closing "];", with a comma after the item that was last. Nothing else in
   this file goes into index.html.

   A graphic item is a hotspot over a diagram (§10.04 ALIAS graphic -> hotspot).
   The renderer draws each region as a labelled box at its x, y, w, h inside
   graphic.viewBox, numbers it with n, and joins boxes left to right with flow
   arrows unless graphic.flow is false. It draws nothing else, so every fact a
   candidate needs is in the stem or in a region label. Labels wrap at 14
   characters to at most three lines; a no-break space (\u00a0) keeps a phrase
   such as "PV\u00a0500" on one line. `multi: true` marks the two items with
   more than one correct region (the web renderer reads q.multi; the phones
   also infer it from the correct count).

   Mix: People 4 / Process 5 / Business 3; predictive 5 / adaptive 3 / hybrid 4.
   Ids use 061 in each task, clear of the core (001–026), the lecture pack
   (1–2x), the extended packs (101+) and the mock pack (201+). */
PMP.STAGED_GRAPHIC_QUESTIONS = [

  {
    id: "pmp-pr8-061", type: "graphic", domain: "process", ecoTask: "process-8",
    approach: "predictive", difficulty: 3, multi: true, tags: ["schedule", "float", "network", "archetype-apply-the-rule"],
    stem: "The network diagram shows the five activities left on your plant installation, with their durations and predecessors. The finish date is set by the longest path. The sponsor asks which activities could each, on its own, start two days late without moving the finish date. Select the two.",
    graphic: { viewBox: "0 0 660 190", flow: false, title: "Network diagram, five remaining activities with durations and predecessors", caption: "Select two activities. Use Tab or the arrow keys to move between activities, then Enter or Space." },
    regions: [
      { id: "a", n: 1, label: "A, 3 days, from start", x: 20, y: 20, w: 150, h: 62, correct: false, rationale: "A sits on the longest path, A then C then E, at 12 days. It has no float, so any late start moves the finish date day for day." },
      { id: "b", n: 2, label: "B, 4 days, from start", x: 20, y: 108, w: 150, h: 62, correct: true, rationale: "B is on the shorter path, B then D then E, at 10 days. The two-day difference from the critical path is its total float, so a two-day late start is absorbed." },
      { id: "c", n: 3, label: "C, 5 days, after A", x: 255, y: 20, w: 150, h: 62, correct: false, rationale: "C is the longest activity on the critical path. A two-day slip here moves the finish by two days." },
      { id: "d", n: 4, label: "D, 2 days, after B", x: 255, y: 108, w: 150, h: 62, correct: true, rationale: "D shares the two days of float on the B path. Taken on its own, a two-day late start uses all of it and leaves the finish unchanged." },
      { id: "e", n: 5, label: "E, 4 days, after C and D", x: 490, y: 64, w: 150, h: 62, correct: false, rationale: "E is where both paths meet and it is on the critical path. Nothing downstream of the last activity can absorb a delay to it." }
    ],
    explanation: "Total float on a path is the difference between its length and the length of the critical path. Here A, C and E take 12 days and B, D and E take 10, so B and D share two days of float and the other three have none. Float belongs to the path rather than to each activity, which is why the question asks about each on its own: once B has started two days late, D has nothing left.",
    mindset: "Find the longest path first, because float only means something relative to it."
  },

  {
    id: "pmp-pr9-061", type: "graphic", domain: "process", ecoTask: "process-9",
    approach: "agile", difficulty: 2, tags: ["burndown", "scope", "sprint", "archetype-diagnose-first"],
    stem: "Your team committed to 80 points for a ten-day sprint. The burndown shows the points remaining at each two-day checkpoint. At the sprint review the product owner says the team simply worked too slowly. Select the checkpoint that shows the sprint's scope changed after the sprint began.",
    graphic: { viewBox: "0 0 660 130", title: "Sprint burndown, points remaining at six checkpoints", caption: "Select a checkpoint. Use Tab or the arrow keys to move between checkpoints, then Enter or Space." },
    regions: [
      { id: "d0", n: 1, label: "Day\u00a00 80\u00a0points", x: 6, y: 34, w: 100, h: 62, correct: false, rationale: "This is the commitment at the start of the sprint. It is the baseline the rest of the chart is read against, not evidence that anything changed." },
      { id: "d2", n: 2, label: "Day\u00a02 64\u00a0points", x: 114, y: 34, w: 100, h: 62, correct: false, rationale: "Sixteen points in two days is exactly the pace the commitment needs. Nothing here suggests slow work or added scope." },
      { id: "d4", n: 3, label: "Day\u00a04 48\u00a0points", x: 222, y: 34, w: 100, h: 62, correct: false, rationale: "Another sixteen points done, still on pace. The team was not working slowly in the first half of the sprint." },
      { id: "d6", n: 4, label: "Day\u00a06 52\u00a0points", x: 330, y: 34, w: 100, h: 62, correct: true, rationale: "Remaining work rose from 48 to 52. Finishing work can only lower the line, so a rise means items were added, or re-estimated upwards, after the sprint began." },
      { id: "d8", n: 5, label: "Day\u00a08 36\u00a0points", x: 438, y: 34, w: 100, h: 62, correct: false, rationale: "The line falls by sixteen again, so the team's pace is unchanged after the rise. The extra work is carried here, not caused here." },
      { id: "d10", n: 6, label: "Day\u00a010 20\u00a0points", x: 546, y: 34, w: 100, h: 62, correct: false, rationale: "The twenty points left at the end are what the product owner is reacting to. Read on its own the figure looks like slow work; its cause is at day 6." }
    ],
    explanation: "A burndown can only fall when work is finished, so any rise is work that entered the sprint after it started. Here the team burned sixteen points in every two-day interval, including the one in which the line rose, which means about twenty points were added at day 6 and the twenty left at the end are that addition. Reading the shape of the line rather than its end point is what separates a pace problem from a scope problem, and the two call for quite different conversations.",
    mindset: "Read the shape of the line before judging its end, because a rise is scope, not slowness."
  },

  {
    id: "pmp-pr2-061", type: "graphic", domain: "process", ecoTask: "process-2",
    approach: "hybrid", difficulty: 3, tags: ["burnup", "scope", "forecast", "archetype-challenge-the-premise"],
    stem: "The release burnup for your programme's customer portal shows the points completed and the total release scope at the end of each sprint. Velocity has stayed close to 20 points. The sponsor believes the release date is slipping because the team has slowed. Select the sprint in which the change that moved the forecast furthest was recorded.",
    graphic: { viewBox: "0 0 660 140", title: "Release burnup, completed points and total scope at the end of five sprints", caption: "Select a sprint. Use Tab or the arrow keys to move between sprints, then Enter or Space." },
    regions: [
      { id: "s1", n: 1, label: "Sprint\u00a01 done\u00a020 scope\u00a0100", x: 8, y: 30, w: 118, h: 76, correct: false, rationale: "Twenty points done against a scope of 100 is the starting position. Nothing has moved yet, so nothing here can explain a slipping date." },
      { id: "s2", n: 2, label: "Sprint\u00a02 done\u00a041 scope\u00a0100", x: 142, y: 30, w: 118, h: 76, correct: false, rationale: "Twenty-one points completed and scope unchanged. The forecast improved slightly in this sprint rather than slipping." },
      { id: "s3", n: 3, label: "Sprint\u00a03 done\u00a060 scope\u00a0118", x: 276, y: 30, w: 118, h: 76, correct: false, rationale: "Scope grew by 18 points here, which did push the forecast out by most of a sprint. It is a real change and not the largest one on the chart." },
      { id: "s4", n: 4, label: "Sprint\u00a04 done\u00a081 scope\u00a0122", x: 410, y: 30, w: 118, h: 76, correct: false, rationale: "Scope grew by only 4 points while the team completed 21. The gap to the finish narrowed in this sprint." },
      { id: "s5", n: 5, label: "Sprint\u00a05 done\u00a0100 scope\u00a0150", x: 544, y: 30, w: 108, h: 76, correct: true, rationale: "Scope jumped by 28 points while the team completed its usual 19. The remaining work rose from 41 to 50, the largest single move on the chart, with velocity unchanged." }
    ],
    explanation: "A burnup keeps completed work and total scope on separate lines, which is exactly what lets it distinguish a team that has slowed from a finish line that has moved. Here completion climbs steadily by about 20 points a sprint while scope rises by 18, then 4, then 28. The forecast is slipping because the release is growing, and that is a scope decision for the product owner and sponsor rather than a performance problem for the team.",
    mindset: "When a forecast slips, check whether the finish line moved before asking whether the team slowed."
  },

  {
    id: "pmp-pr6-061", type: "graphic", domain: "process", ecoTask: "process-6",
    approach: "predictive", difficulty: 3, tags: ["earned-value", "s-curve", "variance", "archetype-apply-the-rule"],
    stem: "The chart summarises cumulative planned value, earned value and actual cost, in thousands, at the end of each of the first five months of your office refurbishment project. Select the first month in which the project was both behind schedule and over budget.",
    graphic: { viewBox: "0 0 660 140", title: "Cumulative planned value, earned value and actual cost for months 1 to 5", caption: "Select a month. Use Tab or the arrow keys to move between months, then Enter or Space." },
    regions: [
      { id: "m1", n: 1, label: "Month\u00a01: PV\u00a0100 EV\u00a0100 AC\u00a095", x: 8, y: 30, w: 118, h: 76, correct: false, rationale: "Earned value equals planned value and actual cost is below it. The project is on schedule and slightly under budget." },
      { id: "m2", n: 2, label: "Month\u00a02: PV\u00a0220 EV\u00a0230 AC\u00a0210", x: 142, y: 30, w: 118, h: 76, correct: false, rationale: "Earned value is above planned value and above actual cost. The project is ahead of schedule and under budget, the opposite of the condition asked for." },
      { id: "m3", n: 3, label: "Month\u00a03: PV\u00a0360 EV\u00a0340 AC\u00a0330", x: 276, y: 30, w: 118, h: 76, correct: false, rationale: "Earned value has fallen below planned value, so the project is behind schedule. Actual cost is still below earned value, so it is not yet over budget." },
      { id: "m4", n: 4, label: "Month\u00a04: PV\u00a0500 EV\u00a0450 AC\u00a0480", x: 410, y: 30, w: 118, h: 76, correct: true, rationale: "Schedule variance is 450 minus 500, which is negative, and cost variance is 450 minus 480, also negative. This is the first month in which both conditions hold." },
      { id: "m5", n: 5, label: "Month\u00a05: PV\u00a0640 EV\u00a0600 AC\u00a0650", x: 544, y: 30, w: 108, h: 76, correct: false, rationale: "The project is behind schedule and over budget here as well. It is not the first month in which that is true, which month 4 is." }
    ],
    explanation: "Both variances are measured against earned value, the value of the work actually done. Schedule variance compares it with planned value, and cost variance compares it with actual cost; a negative figure means behind schedule or over budget respectively. Comparing actual cost with planned value, which is the instinctive reading of an S-curve, mixes the two questions and can make a project that is spending slowly because it is late look as if it is under budget.",
    mindset: "Judge both schedule and cost against earned value, never against each other."
  },

  {
    id: "pmp-pr3-061", type: "graphic", domain: "process", ecoTask: "process-3",
    approach: "agile", difficulty: 3, tags: ["kanban", "wip-limits", "flow", "archetype-diagnose-first"],
    stem: "Your team's average lead time has doubled over the past month. The board shows the items in each column against its work-in-progress limit. The developers propose raising the development limit from 4 to 6 so they can keep busy. Select the column the team should address first to restore flow.",
    graphic: { viewBox: "0 0 660 140", title: "Kanban board, items in each column against its work-in-progress limit", caption: "Select a column. Use Tab or the arrow keys to move between columns, then Enter or Space." },
    regions: [
      { id: "k1", n: 1, label: "Ready\u00a0queue 5\u00a0items limit\u00a06", x: 8, y: 30, w: 118, h: 76, correct: false, rationale: "The ready queue is inside its limit and simply holds work waiting to start. It is not what is slowing items that have already started." },
      { id: "k2", n: 2, label: "In\u00a0development 4\u00a0items limit\u00a04", x: 142, y: 30, w: 118, h: 76, correct: false, rationale: "Development is at its limit, which is the limit working as intended. Raising it, as the developers propose, would push more work into a queue that is already overflowing downstream." },
      { id: "k3", n: 3, label: "Code\u00a0review 7\u00a0items limit\u00a03", x: 276, y: 30, w: 118, h: 76, correct: true, rationale: "Seven items against a limit of three, with testing starved behind it, marks the constraint. Swarming on reviews and honouring the limit is what will release the work already in progress." },
      { id: "k4", n: 4, label: "In\u00a0testing 1\u00a0item limit\u00a04", x: 410, y: 30, w: 118, h: 76, correct: false, rationale: "Testing has spare capacity because little is reaching it. A starved column is a symptom of the constraint upstream, not the place to intervene." },
      { id: "k5", n: 5, label: "Done\u00a0this\u00a0week 2\u00a0items", x: 544, y: 30, w: 108, h: 76, correct: false, rationale: "Two items finished this week is the outcome the team wants to improve. It measures the problem and cannot be acted on directly." }
    ],
    explanation: "Work-in-progress limits make a constraint visible: the column over its limit, with an empty column after it, is where work is waiting. Lead time grows with the amount of work in the system, so adding capacity upstream of the constraint, which is what raising the development limit does, lengthens lead time rather than shortening it. The remedy is to finish what is stuck, typically by the whole team helping with reviews, before starting anything new.",
    mindset: "Fix the column that is over its limit before adding capacity anywhere upstream of it."
  },

  {
    id: "pmp-p3-061", type: "graphic", domain: "people", ecoTask: "people-3",
    approach: "agile", difficulty: 2, tags: ["team-development", "tuckman", "leadership", "archetype-apply-the-rule"],
    stem: "Three months after forming, your team has agreed its working norms, members resolve most disagreements between themselves, and they have begun giving each other candid feedback in retrospectives. They still check with you before decisions they are entitled to make. Select the stage of team development the team is most likely in.",
    graphic: { viewBox: "0 0 660 130", title: "Five stages of team development, left to right", caption: "Select a stage. Use Tab or the arrow keys to move between stages, then Enter or Space." },
    regions: [
      { id: "t1", n: 1, label: "Forming", x: 8, y: 34, w: 118, h: 62, correct: false, rationale: "Forming is polite and tentative, with members unsure of their roles and reliant on the leader. This team has agreed norms and resolves disagreements, which is well past that." },
      { id: "t2", n: 2, label: "Storming", x: 142, y: 34, w: 118, h: 62, correct: false, rationale: "Storming is marked by friction over roles and approach that the team cannot yet settle itself. This team settles most disagreements without help." },
      { id: "t3", n: 3, label: "Norming", x: 276, y: 34, w: 118, h: 62, correct: true, rationale: "Agreed norms, self-resolved disagreements and growing candour are the signs of norming. Checking with the leader on decisions it owns shows the team is not yet fully autonomous." },
      { id: "t4", n: 4, label: "Performing", x: 410, y: 34, w: 118, h: 62, correct: false, rationale: "A performing team works autonomously and takes the decisions it is entitled to without seeking permission. The checking-in is the evidence that it is not there yet." },
      { id: "t5", n: 5, label: "Adjourning", x: 544, y: 34, w: 108, h: 62, correct: false, rationale: "Adjourning is the close of the team's work together, when it disbands. Nothing in the description suggests the team is ending." }
    ],
    explanation: "The stages are recognised by behaviour rather than by elapsed time. A norming team has settled how it works together and handles most friction itself, and the remaining step to performing is autonomy over its own decisions. That gives the leader a clear move: hand decisions back explicitly, and decline gently to make the ones the team already owns.",
    mindset: "Diagnose a team's stage from what it does, then lead it towards the next one."
  },

  {
    id: "pmp-p2-061", type: "graphic", domain: "people", ecoTask: "people-2",
    approach: "hybrid", difficulty: 3, tags: ["conflict", "collaboration", "design", "archetype-apply-the-rule"],
    stem: "Your two most senior engineers disagree about how the interface between the billing and scheduling platforms should be structured. Both positions rest on sound concerns, the design will be hard to change once built, the design freeze is three weeks away, and you need both engineers committed to whatever is chosen. Select the approach to the conflict that fits best.",
    graphic: { viewBox: "0 0 660 230", flow: false, title: "Five approaches to conflict, arranged by assertiveness and cooperativeness", caption: "Assertiveness rises towards the top and cooperativeness towards the right. Select an approach. Use Tab or the arrow keys to move between approaches, then Enter or Space." },
    regions: [
      { id: "c1", n: 1, label: "Competing", x: 40, y: 20, w: 170, h: 56, correct: false, rationale: "Forcing one design through would be quick and would leave one engineer overruled on a decision both care about, losing the commitment the situation needs." },
      { id: "c2", n: 2, label: "Collaborating", x: 450, y: 20, w: 170, h: 56, correct: true, rationale: "Working the problem together to meet both sets of concerns fits a lasting, important decision with time available and a need for both engineers' commitment." },
      { id: "c3", n: 3, label: "Compromising", x: 245, y: 87, w: 170, h: 56, correct: false, rationale: "Splitting the difference suits a quick settlement between equals. In a design that is hard to change, a half-measure can satisfy neither concern and leave a weaker interface." },
      { id: "c4", n: 4, label: "Avoiding", x: 40, y: 154, w: 170, h: 56, correct: false, rationale: "Postponing the disagreement suits trivial issues or cooling-off periods. With a design freeze three weeks away, avoidance simply lets the deadline decide." },
      { id: "c5", n: 5, label: "Accommodating", x: 450, y: 154, w: 170, h: 56, correct: false, rationale: "Asking one engineer to yield preserves goodwill on minor issues. Here it would discard a sound concern on an important, lasting decision." }
    ],
    explanation: "The five approaches differ in how far each party's concerns are pursued, and each has situations it suits. Collaboration costs the most time and attention, which is why it is reserved for decisions that are important, lasting and need genuine commitment, and why it fits only when there is time to do it. Every condition in this situation points to it; the other approaches trade away either the quality of the design or the commitment of one of the people who must build it.",
    mindset: "Spend the time to collaborate when the decision is important, lasting and needs everyone's commitment."
  },

  {
    id: "pmp-p4-061", type: "graphic", domain: "people", ecoTask: "people-4",
    approach: "predictive", difficulty: 3, tags: ["stakeholders", "power-interest", "regulator", "archetype-act-now"],
    stem: "The power and interest grid for your reservoir dam safety upgrade was drawn at the start of the project. This week the regional regulator announced a formal review of the dam's safety case and asked for monthly updates on your project's progress. Select the stakeholder whose engagement approach should change now.",
    graphic: { viewBox: "0 0 660 215", flow: false, title: "Power and interest grid with six stakeholders and their current engagement approach", caption: "Power rises towards the top and interest towards the right. Select a stakeholder. Use Tab or the arrow keys to move between stakeholders, then Enter or Space." },
    regions: [
      { id: "g1", n: 1, label: "Regulator keep\u00a0satisfied", x: 15, y: 15, w: 150, h: 56, correct: true, rationale: "The regulator's power was always high, and its formal review and request for monthly updates show its interest is now high too. It belongs in manage closely." },
      { id: "g2", n: 2, label: "Finance\u00a0board keep\u00a0satisfied", x: 170, y: 15, w: 150, h: 56, correct: false, rationale: "The finance board has high power and nothing in the news changes its interest. Its current approach still fits." },
      { id: "g3", n: 3, label: "Asset\u00a0director manage\u00a0closely", x: 340, y: 15, w: 150, h: 56, correct: false, rationale: "The asset director is already managed closely. The review may add to what you discuss with him, but it does not change his approach." },
      { id: "g4", n: 4, label: "Pump\u00a0vendor monitor", x: 15, y: 145, w: 150, h: 56, correct: false, rationale: "The vendor has low power and low interest in the project's governance. A regulatory review of the safety case does not move it." },
      { id: "g5", n: 5, label: "Residents keep\u00a0informed", x: 340, y: 145, w: 150, h: 56, correct: false, rationale: "Residents are interested and are kept informed. The review may be worth mentioning to them, and it does not give them more power over the project." },
      { id: "g6", n: 6, label: "Operators keep\u00a0informed", x: 495, y: 145, w: 150, h: 56, correct: false, rationale: "Operators remain highly interested with limited power over the project. The review changes nothing about their position on the grid." }
    ],
    explanation: "A power and interest grid is a snapshot, and its value depends on being redrawn when a stakeholder's position moves. A powerful stakeholder who was content to be kept satisfied becomes one to manage closely the moment its interest rises, and a formal review with a request for regular updates is exactly that signal. Leaving the old classification in place means engaging the most powerful party in the project at a distance just as it starts paying attention.",
    mindset: "Redraw the stakeholder map when a stakeholder's interest moves, not at the next scheduled review."
  },

  {
    id: "pmp-p7-061", type: "graphic", domain: "people", ecoTask: "people-7",
    approach: "hybrid", difficulty: 3, tags: ["knowledge-transfer", "skills-matrix", "single-point", "archetype-act-now"],
    stem: "Two developers leave your platform team in six weeks, at the end of their contracts. The skills matrix shows, for each component, how many people can maintain it without help and how many of those are leaving. Select the component where knowledge transfer should start first.",
    graphic: { viewBox: "0 0 660 140", flow: false, title: "Skills matrix, people able to maintain each of five components and how many are leaving", caption: "Select a component. Use Tab or the arrow keys to move between components, then Enter or Space." },
    regions: [
      { id: "k1", n: 1, label: "Payments 1\u00a0expert 1\u00a0leaving", x: 8, y: 30, w: 118, h: 76, correct: true, rationale: "The only person who can maintain payments is leaving, so in six weeks nobody will be able to. That is the one component heading for zero coverage." },
      { id: "k2", n: 2, label: "Reporting 2\u00a0experts 1\u00a0leaving", x: 142, y: 30, w: 118, h: 76, correct: false, rationale: "Reporting drops to a single expert, which is a weakness to address. It still has someone who can maintain it after the departures." },
      { id: "k3", n: 3, label: "Search 2\u00a0experts 1\u00a0leaving", x: 276, y: 30, w: 118, h: 76, correct: false, rationale: "Search falls from two experts to one. The risk rises, and coverage remains." },
      { id: "k4", n: 4, label: "Identity 3\u00a0experts none\u00a0leaving", x: 410, y: 30, w: 118, h: 76, correct: false, rationale: "Identity has three experts and loses none. It is the best-covered component on the matrix." },
      { id: "k5", n: 5, label: "Notifications 1\u00a0expert none\u00a0leaving", x: 544, y: 30, w: 108, h: 76, correct: false, rationale: "A single expert is a standing risk worth addressing. That person is staying, so it is not the transfer that has a deadline." }
    ],
    explanation: "Knowledge transfer is prioritised by the coverage that will remain, not by how many people are leaving overall. A component whose only expert is departing will have no one able to maintain it, which makes it the transfer with a hard deadline. Components dropping to a single expert, and those already resting on one person who is staying, are real risks that belong in the plan after it.",
    mindset: "Start knowledge transfer where coverage is heading for zero, then shore up the single points."
  },

  {
    id: "pmp-b5-061", type: "graphic", domain: "business", ecoTask: "business-5",
    approach: "predictive", difficulty: 3, tags: ["risk", "expected-monetary-value", "ranking", "archetype-apply-the-rule"],
    stem: "The diagram shows the six open threats on your warehouse automation project, each with its probability and cost impact. The sponsor has asked you to rank them by expected monetary value before the risk review. Select the threat with the highest expected monetary value.",
    graphic: { viewBox: "0 0 660 180", flow: false, title: "Six open threats with probability and cost impact", caption: "Select a threat. Use Tab or the arrow keys to move between threats, then Enter or Space." },
    regions: [
      { id: "r1", n: 1, label: "Risk\u00a01 10%\u00a0chance 200,000\u00a0impact", x: 15, y: 15, w: 200, h: 70, correct: false, rationale: "Ten per cent of 200,000 is 20,000. It is a large impact at a low probability, and its expected value is below threat 3's." },
      { id: "r2", n: 2, label: "Risk\u00a02 50%\u00a0chance 30,000\u00a0impact", x: 230, y: 15, w: 200, h: 70, correct: false, rationale: "Half of 30,000 is 15,000. A likely threat with a modest impact gives one of the lowest values on the diagram." },
      { id: "r3", n: 3, label: "Risk\u00a03 40%\u00a0chance 60,000\u00a0impact", x: 445, y: 15, w: 200, h: 70, correct: true, rationale: "Forty per cent of 60,000 is 24,000, the highest of the six. Neither its probability nor its impact is the largest; the product is." },
      { id: "r4", n: 4, label: "Risk\u00a04 80%\u00a0chance 25,000\u00a0impact", x: 15, y: 95, w: 200, h: 70, correct: false, rationale: "Eighty per cent of 25,000 is 20,000. It is the most likely threat, and its expected value is still below threat 3's." },
      { id: "r5", n: 5, label: "Risk\u00a05 5%\u00a0chance 400,000\u00a0impact", x: 230, y: 95, w: 200, h: 70, correct: false, rationale: "Five per cent of 400,000 is 20,000. Its impact is the largest, and it may deserve separate attention against the organisation's risk thresholds, but its expected value is not the highest." },
      { id: "r6", n: 6, label: "Risk\u00a06 30%\u00a0chance 50,000\u00a0impact", x: 445, y: 95, w: 200, h: 70, correct: false, rationale: "Thirty per cent of 50,000 is 15,000, joint lowest with threat 2." }
    ],
    explanation: "Expected monetary value multiplies probability by impact, so it ranks threats by their average cost rather than by either factor alone. The instinctive picks, the most likely threat and the most damaging one, both come out lower here. Expected value is a ranking tool rather than the whole of risk judgement: a low-probability, high-impact threat can exceed the organisation's risk threshold on its own, and that is a separate question the review should also ask.",
    mindset: "Rank threats by probability times impact, then check separately that no single impact exceeds what the organisation can bear."
  },

  {
    id: "pmp-b1-061", type: "graphic", domain: "business", ecoTask: "business-1",
    approach: "predictive", difficulty: 3, tags: ["governance", "raci", "delegation", "archetype-apply-the-rule"],
    stem: "Under your organisation's delegation policy, any contract above 250,000 is awarded on the sponsor's authority, and the procurement lead prepares and runs every tender. The responsibility assignment matrix for your laboratory fit-out's main contract is shown. Select the assignment that must change so that each activity has exactly one accountable person consistent with the policy.",
    graphic: { viewBox: "0 0 660 210", flow: false, title: "Responsibility assignment matrix, three activities by three roles", caption: "Rows are activities and columns are roles. Select an assignment. Use Tab or the arrow keys to move between assignments, then Enter or Space." },
    regions: [
      { id: "sp-s", n: 1, label: "Specification Sponsor:\u00a0C", x: 15, y: 12, w: 200, h: 56, correct: false, rationale: "The sponsor is consulted on the specification, which is reasonable. The specification row has a single accountable owner already." },
      { id: "sp-e", n: 2, label: "Specification Engineering:\u00a0A", x: 230, y: 12, w: 200, h: 56, correct: false, rationale: "Engineering owning the technical specification is a sensible single point of accountability, and nothing in the policy contradicts it." },
      { id: "sp-p", n: 3, label: "Specification Procurement:\u00a0C", x: 445, y: 12, w: 200, h: 56, correct: false, rationale: "Procurement is consulted so the specification can be tendered. It is not an accountability conflict." },
      { id: "ti-s", n: 4, label: "Tender\u00a0issue Sponsor:\u00a0I", x: 15, y: 79, w: 200, h: 56, correct: false, rationale: "Keeping the sponsor informed of the tender issue is consistent with the policy, which gives the sponsor the award, not the tender." },
      { id: "ti-e", n: 5, label: "Tender\u00a0issue Engineering:\u00a0C", x: 230, y: 79, w: 200, h: 56, correct: false, rationale: "Engineering is consulted on technical content in the tender. That supports the procurement lead without competing for accountability." },
      { id: "ti-p", n: 6, label: "Tender\u00a0issue Procurement:\u00a0A", x: 445, y: 79, w: 200, h: 56, correct: false, rationale: "The policy says the procurement lead prepares and runs every tender, so procurement being accountable for the tender issue is correct." },
      { id: "ca-s", n: 7, label: "Contract\u00a0award Sponsor:\u00a0A", x: 15, y: 146, w: 200, h: 56, correct: false, rationale: "The policy puts awards above 250,000 on the sponsor's authority, so this accountability is the one to keep." },
      { id: "ca-e", n: 8, label: "Contract\u00a0award Engineering:\u00a0C", x: 230, y: 146, w: 200, h: 56, correct: false, rationale: "Consulting engineering on the award is good practice for a technical contract and does not create a second accountable party." },
      { id: "ca-p", n: 9, label: "Contract\u00a0award Procurement:\u00a0A", x: 445, y: 146, w: 200, h: 56, correct: true, rationale: "The award row has two accountable people, and the policy gives the award to the sponsor. Procurement's role here should be responsible, running the process that leads to the sponsor's decision." }
    ],
    explanation: "A responsibility assignment matrix should give every activity exactly one accountable person, the one who answers for the outcome and signs it off, while any number can be responsible, consulted or informed. Where a row has two, the organisation's delegation rules decide which one stays. Two accountable parties on a contract award is a governance gap that tends to surface only when the award is disputed.",
    mindset: "Give every decision exactly one accountable owner, and let the delegation rules say who."
  },

  {
    id: "pmp-b6-061", type: "graphic", domain: "business", ecoTask: "business-6",
    approach: "hybrid", difficulty: 3, multi: true, tags: ["pareto", "quality", "improvement", "archetype-apply-the-rule"],
    stem: "Your claims operations team logged every claim that needed rework last month and charted the causes, largest first, with the running percentage. The improvement squad can tackle a few causes before the next review. Select the causes that together account for about 80 per cent of the rework. Select three.",
    graphic: { viewBox: "0 0 660 140", flow: false, title: "Pareto chart of rework causes, largest first, with the running percentage", caption: "Select three causes. Use Tab or the arrow keys to move between causes, then Enter or Space." },
    regions: [
      { id: "p1", n: 1, label: "Wrong\u00a0address 42\u00a0cases running\u00a042%", x: 6, y: 30, w: 100, h: 76, correct: true, rationale: "The largest single cause, at 42 of the 100 reworked claims. Any effort aimed at the vital few starts here." },
      { id: "p2", n: 2, label: "Missing\u00a0proof 26\u00a0cases running\u00a068%", x: 114, y: 30, w: 100, h: 76, correct: true, rationale: "With the first cause, it brings the running total to 68 per cent. It belongs in the vital few." },
      { id: "p3", n: 3, label: "Duplicates 12\u00a0cases running\u00a080%", x: 222, y: 30, w: 100, h: 76, correct: true, rationale: "This takes the running total to 80 per cent. The three largest causes together account for four-fifths of the rework." },
      { id: "p4", n: 4, label: "Late\u00a0payment 8\u00a0cases running\u00a088%", x: 330, y: 30, w: 100, h: 76, correct: false, rationale: "Eight cases is real rework, and it sits beyond the 80 per cent line. It is one of the useful many to address after the vital few." },
      { id: "p5", n: 5, label: "Timeouts 7\u00a0cases running\u00a095%", x: 438, y: 30, w: 100, h: 76, correct: false, rationale: "Seven per cent of the rework. Tackling it before the larger causes would spend the squad's capacity where it removes least." },
      { id: "p6", n: 6, label: "Other 5\u00a0cases running\u00a0100%", x: 546, y: 30, w: 100, h: 76, correct: false, rationale: "A residual category of mixed causes. It is the least actionable bar on any Pareto chart and the last place to start." }
    ],
    explanation: "A Pareto chart orders causes by frequency and adds the running percentage so the few causes that produce most of the effect can be read straight off it. Here three of six causes account for 80 per cent of the rework, which is where limited improvement capacity returns the most. The chart says where to look, not why: the squad's next step is root-cause analysis on those three, since a frequent cause is not necessarily an easy one to remove.",
    mindset: "Spend limited improvement effort on the few causes that produce most of the problem."
  }

];
