:- ensure_loaded(library(pfc_lib)).

:-add_PFC((
  observes(A, O), location(A, public_square), location(B, public_square), {A \== B}, affordance(O, public_information)
 ==> observes(B, O)
 )).

:-add_PFC((
indicates(states(M, S), A, S)
)).

:-add_PFC((
observes(A, M), states(M, S)
==> reason_to_believe(A, states(M, S))
)).

:-add_PFC((
states(monument_27, traitor(hipparchus))
)).

:- add_PFC((
 location(monument_27, public_square)
 )).

 :- add_PFC((
 affordance(monument_27, public_information)
 )).

:-add_PFC((
observes(A, M), observes(B, M), affordance(M, public_information), states(M, S)
==> indicates(states(M, S), A, reason_to_believe(B, states(M, S)))
)).

:-add_PFC((
      member(citizen1, citizen)
  )).

:-add_PFC((
      member(citizen2, citizen)
  )).

:-add_PFC((
      member(citizen3, citizen)
  )).

