package infomgmag.mars;

import infomgmag.Board;
import infomgmag.Hand;

public class CardAgent {

    Hand hand;

    CardAgent(Hand hand) {
        this.hand = hand;
    }

    public int tradeIn(Board board) {
        int useInfantry = 0, useCavalry = 0, useArtillery = 0, useWildcards = 0;
        if (hand.getNumberOfCards() > 4) {
            if (hand.getInfantry() > 0 && hand.getArtillery() > 0 && hand.getCavalry() > 0) {
                useInfantry = 1;
                useArtillery = 1;
                useCavalry = 1;
            } else if (hand.getInfantry() > 2) {
                useInfantry = 3;
            } else if (hand.getCavalry() > 2) {
                useCavalry = 3;
            } else if (hand.getArtillery() > 2) {
                useArtillery = 3;
            } else if (hand.getWildcards() == 1) {
                if (hand.getInfantry() > 1) {
                    useInfantry = 2;
                    useWildcards = 1;
                } else if (hand.getCavalry() > 1) {
                    useCavalry = 2;
                    useWildcards = 1;
                } else if (hand.getArtillery() > 1) {
                    useArtillery = 2;
                    useWildcards = 1;
                } else if (hand.getArtillery() > 0 && hand.getCavalry() > 0) {
                    useArtillery = 1;
                    useCavalry = 1;
                    useWildcards = 1;
                } else if (hand.getArtillery() > 0 && hand.getInfantry() > 0) {
                    useArtillery = 1;
                    useInfantry = 1;
                    useWildcards = 1;
                } else if (hand.getCavalry() > 0 && hand.getInfantry() > 0) {
                    useCavalry = 1;
                    useInfantry = 1;
                    useWildcards = 1;
                }
            } else if (hand.getWildcards() == 2) {
                if (hand.getInfantry() > 0) {
                    useInfantry = 1;
                    useWildcards = 2;
                } else if (hand.getCavalry() > 0) {
                    useCavalry = 1;
                    useWildcards = 2;
                } else if (hand.getArtillery() > 0) {
                    useArtillery = 1;
                    useWildcards = 2;
                }
            }
        }

        if (useInfantry > 0 || useArtillery > 0 || useCavalry > 0) {
            int reinforcementsByCards = board.getAndMoveGoldenCavalry();
            hand.setInfantry(hand.getInfantry() - useInfantry);
            hand.setArtillery(hand.getArtillery() - useArtillery);
            hand.setCavalry(hand.getCavalry() - useCavalry);
            hand.setWildCards(hand.getWildcards() - useWildcards);
            board.setArtillery(board.getArtillery() + useArtillery);
            board.setCavalry(board.getCavalry() + useCavalry);
            board.setInfantry(board.getInfantry() + useInfantry);
            board.setWildcards(board.getWildcards() + useWildcards);
            return reinforcementsByCards;
        }
        return 0;
    }
}
