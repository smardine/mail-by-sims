package fenetre.principale;


/*
 * DocumentViewer.java  1.0
 * 
 * Copyright (c) 1999 Emmanuel PUYBARET - eTeks.
 * All Rights Reserved.
 *
 */
 
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
 
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.io.IOException;
 
// Classe de fen�tre Swing permettant de visualiser un
// document (HTML ou texte)
public class DocumentViewer extends JFrame 
                            implements HyperlinkListener, 
                                       ActionListener
{
  // Composant Swing permettant de visualiser un document
  JEditorPane viewer       = new JEditorPane ();
  // Champ de saisie de l'URL � visualiser
  JTextField  urlTextField = new JTextField ();
 
  public DocumentViewer () 
  {	
    // Construction de l'Interface Graphique
    // Panel en haut avec un label et le champ de saisie
    JPanel inputPanel = new JPanel (new BorderLayout ());
    JLabel label = new JLabel ("URL : ");    
    inputPanel.add (label, BorderLayout.WEST);
    inputPanel.add (urlTextField, BorderLayout.CENTER);
    // Zone scroll�e au centre avec le document    
    JScrollPane scrollPane = new JScrollPane (viewer);
    // Ajout des composants � la fen�tre
    getContentPane ().add (inputPanel, BorderLayout.NORTH);
    getContentPane ().add (scrollPane, BorderLayout.CENTER);
    
    // Mode non editable pour recevoir les clics sur les 
    // liens hypertexte
    viewer.setEditable (false);
    // Ajout du listener de clic sur lien
    viewer.addHyperlinkListener (this);
    // Ajout du listener de modification de la saisie
    urlTextField.addActionListener (this);
  }
 
  // M�thode appel�e apr�s un clic sur un lien hyper texte
  public void hyperlinkUpdate (HyperlinkEvent event) 
  {
    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
    {
      // Modification du champ de saisie
      urlTextField.setText (event.getURL ().toString ());
      if (event instanceof HTMLFrameHyperlinkEvent) 
      {
        // Evenement sp�cial en cas d'utilisation de Frame HTML
        HTMLDocument doc = (HTMLDocument)viewer.getDocument ();
        doc.processHTMLFrameHyperlinkEvent (
                       (HTMLFrameHyperlinkEvent)event);
      }
      else
        // Chargement de la page
        loadPage (urlTextField.getText ());
    }
  }
 
  // M�thode appel�e apr�s une modification de la saisie
  public void actionPerformed (ActionEvent event)
  {
    loadPage (urlTextField.getText ());
  }
        
  public void loadPage (String urlText)
  {
    try 
    {
      // Modification du document visualise
      viewer.setPage (new URL (urlText));
    } 
    catch (IOException ex) 
    {
      System.err.println ("Acces impossible a " + urlText);
    }
  }
  
  // M�thode main () d'exemple de mise en oeuvre.
  // Utilisation : java DocumentViewer
  public static void main (String [] args)
  {
    JFrame viewerFrame = new DocumentViewer ();
    viewerFrame.setSize (400, 300);
    viewerFrame.show ();
  }
}