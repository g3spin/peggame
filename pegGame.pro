start([r,r,r,r,e,b,b,b,b]).  % starting position

% can move from a position P1 to position P2
move(P1,P2):- append(A,[r,e|B],P1), append(A,[e,r|B],P2).
move(P1,P2):- append(A,[e,b|B],P1), append(A,[b,e|B],P2).
move(P1,P2):- append(A,[e,r,b|B],P1), append(A,[b,r,e|B],P2).
move(P1,P2):- append(A,[r,b,e|B],P1), append(A,[e,b,r|B],P2).

solved([b,b,b,b,e,r,r,r,r]).   % the target position to be reached

pegs :- start(P), solve(P, [], R), 
        maplist(writeln, R), nl, nl, fail ; true.

% solve( ?InitialPosition, +PreviousPositionsList, ?ResultingPath)
solve(P, Prev, R):- 
    solved(P) -> reverse([P|Prev], R) ; 
    move(P, Q), \+memberchk(Q, Prev), solve(Q, [P|Prev], R).
