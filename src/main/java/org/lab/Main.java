import org.lab.model.*;
import org.lab.simulation.PrisonerPayoffMatrix;
import org.lab.simulation.Tournament;

public static void main(String[] args) {

    Tournament tournament = new Tournament();
    PrisonerPayoffMatrix matrix = new PrisonerPayoffMatrix();

    List<Agent> agents = new ArrayList<>();

    AlwaysCooperate alwaysCooperate = new AlwaysCooperate();
    AlwaysDefect alwaysDefect = new AlwaysDefect();
    Grudger grudger = new Grudger();
    Pavlovian pavlovian = new Pavlovian();
    TitForTat titForTat = new TitForTat();

    agents.add(alwaysCooperate);
    agents.add(alwaysDefect);
    agents.add(grudger);
    agents.add(pavlovian);
    agents.add(titForTat);

    Map<String, Integer> leaderboard = tournament.runTournament(agents, matrix, 10);


    leaderboard.forEach( (player, points) -> {
        System.out.println(player + " " + points);
    });

}