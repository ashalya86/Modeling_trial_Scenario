%
:- ensure_loaded(library(pfc_lib)).

:-dynamic group_goal/2.

:- add_PFC((attended_action(assembly, decree(Prop)) ==> Prop)).

:- add_PFC((
    action(A, E),
    salient(A, E) ==> attended_action(A, E)
	)).

:- add_PFC((
salient(assembly, decree(_))
   )).

:- add_PFC((
      group_goal(Group, Goal), group_member(A, Group) ==>
       salient(A, cooperate(Goal))
	  )).

:- add_PFC((
      group_goal(Group, Goal), group_member(A, Group) ==>
       salient(A, defect(Goal))
	  )).

:- add_PFC((
  prim_action(A, X) ==> action(A, X)
)).


:- add_PFC((
prim_action(A, X), counts_as(X, Y) ==> action(A, Y)
)).

:- add_PFC((
prim_action(assembly, decree(group_goal(citizens, secureCity)))
)).


:-add_PFC((prim_action(citizen1,rebuildingWalls))).
:-add_PFC((prim_action(citizen1,rebuildingWalls))).
