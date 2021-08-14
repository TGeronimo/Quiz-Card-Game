package cardgame;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardPlayer {

	private JTextArea display;
	private JTextArea answer;
	private ArrayList<QuizCard> cardList;
	private QuizCard currentCard;
	private int currentCardIndex;
	private JFrame frame;
	private JButton nextButton;
	private boolean isShowAnswer;
	
	public static void main(String[] args) {
		QuizCardPlayer reader = new QuizCardPlayer();
		reader.go();
	}
	
//	constrói a GUI
	public void go() {
		frame = new JFrame("Quiz Card Player");
		JPanel mainPanel = new JPanel()	;
		Font bigFont = new Font("sanserif", Font.BOLD, 24);
		
		display = new JTextArea(10, 20);
		display.setFont(bigFont);
		
		display.setLineWrap(true);
		display.setEditable(false);
		
		JScrollPane qScroller = new JScrollPane(display);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		nextButton = new JButton("Show question");
		mainPanel.add(qScroller);
		mainPanel.add(nextButton);
		nextButton.addActionListener(new NextCardListener());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadMenuItem = new JMenuItem("Load Card Set");
		loadMenuItem.addActionListener(new OpenMenuListener());
		fileMenu.add(loadMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(640, 500);
		frame.setVisible(true);
	}

	
//	se essa for uma pergunta, mostre a resposta, caso contrário exiba a próxima pergunta
//	configura um flag que indicará se estamos visualizando uma pergunta ou resposta
	public class NextCardListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			if (isShowAnswer) {
//				exibe a resposta porque a pergunta já foi vista
				display.setText(currentCard.getAnswer());
				nextButton.setText("Next Card");
				isShowAnswer = false;
			} else {
				// exibe a próxima pergunta
				if (currentCardIndex < cardList.size()) {
					showNextCard();
				} else {
// 					não há mais cartões
					display.setText("That was last card");
					nextButton.setEnabled(false);
				}
			}
		}
	}

//	abre a caixa de diálogo de um arquivo
//	permite que o usuário navegue e selecione um conjunto de cartões a ser aberto
	public class OpenMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);
			loadFile(fileOpen.getSelectedFile());
		}
	}
	
//	deve construir um ArrayList de cartões, lendo-os em um arquivo de texto
//	chamado pelo manupilador de eventos de OpenMenuListener, lê o arquivo uma linha de cada vez
//	e solicita ao método makeCard() que crie um novo cartão a partir da linha
//	(uma linha do campo conterá tanto a pergunta quanto a resposta, serparadas por um símbolo "/"
	private void loadFile(File file) {
		cardList = new ArrayList<QuizCard>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				makeCard(line);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Couldn't read the cardList out");
			e.printStackTrace();			
		}
		showNextCard();
	}

//	
	private void makeCard(String lineToParse) {
		String[] result = lineToParse.split("/");
		QuizCard card = new QuizCard(result[0], result[1]);
		cardList.add(card);
		System.out.println("made a card");
		
		
	}

	private void showNextCard() {
		currentCard = cardList.get(currentCardIndex);
		currentCardIndex++;
		display.setText(currentCard.getQuestion());
		nextButton.setText("Show answer");
		isShowAnswer = true;
		
	}
	
}
