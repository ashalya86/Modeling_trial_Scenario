ensure_loaded(library(pfc_lib)).

add_PFC((attended_action(assembly, decree(Prop)) ==> Prop)).
add_PFC(( group_goal(Group, Goal), member(A, Group) ==> salient(A, cooperate(Goal)))).




add_PFC((salient(A, cooperate(Goal)) ==> group_goal(Group, Goal), member(A, Group))).
add_PFC((salient(A, defect(Goal)) ==> group_goal(Group, Goal), member(A, Group))).



add_PFC((salient(assembly, decree(_)))).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

add_PFC((action(A, Action2) ==> action(A, Action1), counts_as(Action1, Action2))).
add_PFC((counts_as(rebuildingWalls, cooperate(secureCity)))).
add_PFC((action(kamal, rebuildingWalls))).
