
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
 
 
 