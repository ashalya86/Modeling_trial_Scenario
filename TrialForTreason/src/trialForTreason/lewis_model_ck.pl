:- ensure_loaded(library(pfc_lib)).
%:- expects_dialect(pfc).
:-op(145, xf, @).  

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Additional functions %%%%%%%%%%%%%%%%%%%
% https://stackoverflow.com/questions/31181756/determine-the-maximum-depth-of-a-term
depth(T, D) :-
  compound(T)
  ->  aggregate(max(B+1), P^A^(arg(P, T, A), depth(A, B)), D)
  ;   D = 0.
  
nested_copy(ModelName, Agent,
            bel(ModelName, P), bel(ModelName>>Agent, P)).
nested_copy(ModelName, Agent,
            percept(ModelName, P), percept(ModelName>>Agent, P)).

model_suffix(M, M) :- atom(M).
model_suffix(_>>L, L).

%%%%%%%%%%%%%%%%%%%%%%%%%%%% predicate with constants %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%55

% percepts of an agent when entering the square
%:- add_PFC((
%percept(me, citizen(me))
%)).
%:- add_PFC(( 
%percept(me, location(me, square))
%)).
%:- add_PFC(( percept(me, location(m27, square))
%)).
%:- add_PFC((
% percept(me, states(m27, traitor(hipparchus)))
% )).
%:- add_PFC((
%percept(me, affordance(m27, public_information))
 %)).

:- add_PFC(( 
percept(me, citizen(af))
)).
:- add_PFC((
percept(me, location(af, square))
)).


% Rules
% Rule 1
:- add_PFC((
percept(M, P), {M == me, Self = me; M = _>>Self},
bel(M, location(Self, square)), 
bel(M, location(C, square)), 
bel(M, citizen(C))
==> percept(M>>C, P)
)).

% Rule 2
:- add_PFC((
percept(me, P) ==> bel(me, P)
)).

% Rule 3
:- add_PFC((
bel(me, citizen(C)) ==> bel(me>>C, citizen(C))
)).

% Ruel 4
:- add_PFC((
bel(me, states(M,S)), bel(me, affordance(M, public_information)) 
==> bel(me, S)
)).

% Agent's theory of mind
% Rule 5

% Rule 6
:- add_PFC((
( Conditions ==> bel(M, B) ),
{ depth(M, D), D < 2 }, bel(M, citizen(C)),
{ model_suffix(M,L), (L == af -> true ; L \== C) }
 ==>
{ mapsubterms(nested_copy(M, C), Conditions, ModifiedConditions),
  mapsubterms(nested_copy(M, C), B, ModifiedB) },
( ModifiedConditions ==> bel(M>>C, ModifiedB) )
)).


% Omitted: Definition of depth/2 for depth of model nesting

% Define c1(Ag, P)
% Define property c1(Ag, P)
:- add_PFC((
bel(Ag, P), { atom(Ag) },  bel(Ag>>af, P) 
==> 
c1(Ag, P)
)). 

% Define property c2(Ag, P)
:- add_PFC((
bel(Ag,P), { atom(Ag) },  bel(Ag>>af, P),
{ justifications(bel(Ag>>af, P), JList1),
  justifications(bel(Ag>>af>>af, P), JList2),
  member(J1, JList1), member(J2, JList2),
  subset(J1, J2) }
==> 
c2(Ag, P)
)).

% Define property c3(Ag, P1, P2)
:- add_PFC((
bel(Ag>>af, P1), { atom(Ag) }, bel(Ag>>af, P2), { P1 \== P2 },
{ justifications(bel(Ag>>af, P1), JList1),
  justifications(bel(Ag>>af, P2), JList2),
  member(J1, JList1), member(J2, JList2), subset(J1, J2) }
==>
c3(Ag, P1, P2)
)).

% Define property c4(Ag, P1, P2)
:- add_PFC((
bel(Ag>>af, P1), { atom(Ag) }, bel(Ag>>af, P2),
{ justifications(bel(Ag>>af, P1), JList1),
  justifications(bel(Ag>>af, P2), JList2),
  member(J1, JList1), member(J2, JList2), 
  subset(J1, J2) },
bel(Ag>>af>>af, P1), bel(Ag>>af>>af, P2),
{ justifications(bel(Ag>>af>>af, P1), JList3),
  justifications(bel(Ag>>af>>af, P2), JList4),
  member(J3, JList3), member(J4, JList4), 
  subset(J3, J4) }
==>
c4(Ag, P1, P2)
)).

% Define property ck(Ag, P1, P2)
:- add_PFC((
c1(Ag, P1), c2(Ag, P1), c3(Ag, P1, P2), c4(Ag, P1, P2) 
==> 
ck(Ag, P2)
)).
