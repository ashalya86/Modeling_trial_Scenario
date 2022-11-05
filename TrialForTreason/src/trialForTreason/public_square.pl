%seeing monuments

:- ensure_loaded(library(pfc_lib)).

:- add_PFC((
state_of_affairs(A, location(X))
)).

% entering square creates the percept
:- add_PFC((
	primitive_event(A, entering_square)
	==> percept(A, observing_monument)
	)).

:- add_PFC((
	percept (A, observing_monument), indicates(observing_monument, proposition)
	==> reason_to_believe (A, proposition)
 	)).
%...................................................
%If you have a percept then you have a reason to believe
 :- add_PFC((
 		percept(A, P) ==> reason_to_believe (A, P)
 		)).
 %If A have a reason to believe a state of affairs and that indicates a percept P then A has a reason to believe  P.
 % S would be a monyment 27 in the public square.
 :- add_PFC((
 	reason_to_believe (A, S), indicates (S, A, P) 
 	==> reason_to_believe (A, P)
 	)).
 	
 % Now all Polites in the square are known to percieive 
 % 1. Monuments are percieved by anyone in the square (Now all polites in the square known to percieve the monument_27)
 % 2. If I have pereived a monument, does polites have a reason to believe that(Above rule will help)
 % 2. A perception gives a reason to believe
 
 :- add_PFC((
 	location(polites, main_square) ==> percept(polites, location(monument_27, public_square))
 	)).
 
 :- add_PFC((
 	primitive_event(A, entering_square, location(B, public_square))
 	==> percept(A, location(monument, public_square))
 	)).
 
 :- add_PFC((
 	indicates (publicPerception, Anyone, reason_to_believe(A, publicPerception))
 	)).
 
 :- add_PFC((
 	percept(A, P), {public(P)}, reason_to_believe(B, P) 
 	==> percept(B, P)
 	)).
 
 
 % Agent A percieves P and the perception P is public, there is a reason to believe B also in the square then B has a perception about P 
 % Rule no 1 will tell that if B is in the public square then he sees the monument and there is a reason to believe about P
 % We percieve that B is in the square, so there is a reason to believe
 :- add_PFC((
 perception(A, P), {public(P)}, reason_to_believe(B, P) 
 ==> perception(B, P)
 )).
 
 % C3 - each person's logic of reasoning allows inference may hold. So anyone can see that sounds as a traitor.
 % Monuments in the public square indicates hipparchus is a traitor.	
 :- add_PFC((
 indicates(location(Monument, public_square), Anyone, traitor(hipparchus))
 )).
 
 %goal is to get true for query ck(hipaarchus). Indicators should help reaching this.
 
 
% Now all Polites in the square are known to percieive the public monument.
% Monument in the public square, so the Polites has a perception that the monument is in the square.
:- add_PFC((
 location(polites, main_square) ==> perception(polites, location(monument_27, public_square))
 )).
 
 % 1.Monuments are perceived by anyone in the square.
 :- add_PFC((
 location(A, public_square), monument(M), location(M, public_square)
 ==> prim_action(A, observe(M))
 )).
 
 % if I'm in a public place 
 % and some other agent is in the same place,
 % and I observe object O 
 % and O has the affordance give public information 
 % then that agent observes O.
 :- add_PFC((
 location(A, public_square), location(B, public_square), observe(A, O),
 affordance(public_information(O))
 ==> observe(B, O)
 )).
 
 % 2. perception gives a reason to believe
 
 % 3. public_information indicates to anyone that there is a reason to believe that there is a public information. 
 % public information means every one can see it. 
 % public information indicates to agent that the other people have reason to believe there is a public information
 :- add_PFC((
 indicates(public_information(O), A, reason_to_believe(A, public_information(O)))
 )).
 
 % Agent A observes O
 % and the perception P is public
 % and there is a reason to believe B also in the square then B has a perception about P 
 :- add_PFC((
 observe(A, O), {public(P)}, reason_to_believe(B, P) 
 ==> perception(B, P)
 )).
 
 % C3 - each person's logic of reasoning allows inference may hold. So anyone can see that sounds as a traitor.
 % Monument_27 in the public square indicates hipparchus is a traitor.	
 :- add_PFC((
 indicates(location(monument_27, public_square), A, traitor(hipparchus))
 )).
 
 %goal is to get true for query ck( traitor(hipparchus)).
 
 
 
 
