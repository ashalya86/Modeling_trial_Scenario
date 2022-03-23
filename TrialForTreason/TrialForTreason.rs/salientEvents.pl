attended_action(A, E, T):- 
	action(A, E, T),
	salient(A, E, T).
salient(A, E, T):-
	counts_as(A, E, cooperate(G), T).

counts_as(A, announce_leader, cooperate(secure_city), T).
counts_as(A, seeing_monument, cooperate(secure_city), T).
counts_as(A, attending_rituals, cooperate(secure_city), T). 



