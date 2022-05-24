:- ensure_loaded(library(pfc_lib)).
:- add_PFC((person(P), ~female(P) ==> male(P))).
:- add_PFC(person(joe)).