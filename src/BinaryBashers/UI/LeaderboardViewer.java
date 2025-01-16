package BinaryBashers.UI;

import BinaryBashers.Enemies.ScoreManager;
import dev.WinterRose.SaxionEngine.Button;
import dev.WinterRose.SaxionEngine.*;

public class LeaderboardViewer extends Behavior
{
    private final int level;
    public final BoxRenderer boxRenderer;

    public LeaderboardViewer(Button button, int level)
    {
        this.level = level;
        button.onHoverDraw.add(this::render);
        boxRenderer = new BoxRenderer(new Vector2(150, 100));
    }

    @Override
    public void awake()
    {
        boxRenderer.transform = transform;
        boxRenderer.owner = owner;
        boxRenderer.awake();
    }

    @Override
    public void update()
    {
        boxRenderer.hideImmediately();
        boxRenderer.update();
    }

    public void render(Button.ButtonDrawAction args)
    {
        Painter painter = args.painter;
        boxRenderer.showImmediately();
        boxRenderer.borderColor = Application.current().getActiveScene().getScenePallet().getColorFromIndex(6);
        boxRenderer.backgroundColor = Application.current().getActiveScene().getScenePallet().getColorFromIndex(1);
        boxRenderer.render(painter);

        var scores = ScoreManager.getInstance().getScores(level);
        StringBuilder builder = new StringBuilder("Highscores:\n\n");
        if(scores.isEmpty())
        {
            builder.append("No scores yet");
        }
        else
        {
            for (int i = 0; i < scores.size(); i++)
            {
                ScoreManager.PlayerScore score = scores.get(i);
                builder.append(score.getName() + " - " + score.getScore() + "\n");
                if(i == 5)
                    break; // dont show more than 5 scores on the UI
            }
        }

        painter.drawText(builder.toString(), transform.getPosition().subtract(new Vector2(72, 50)), new Vector2(0), owner.getScene().getScenePallet().getColorFromIndex(6));
    }
}
