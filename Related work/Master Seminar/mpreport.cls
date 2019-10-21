%%
%% mpreport, an attempt at a fancier report style
%% @(#) $Id: mpreport.cls,v 1.17 2008/03/05 17:27:29 mxp Exp $

%% NOTE:  This class automatically specifies the a4paper option and
%%        loads the graphicx package.

%% The class begins by announcing itself as |mpreport.cls|.
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{mpreport}[1999/06/04 A variant report class]

%% It passes all options on to the |report| class.
\DeclareOption*{\PassOptionsToClass{\CurrentOption}{article}}
\ProcessOptions

%% It then loads the class |report|.
\LoadClass[a4paper]{article}

%% We also need the |ifthen| package.
\RequirePackage{ifthen}
\RequirePackage{titlesec}


   
     
%% Dimensions
   \setlength\hoffset{-1in}
   \setlength\voffset{-1cm}
   \setlength\footskip{1.5cm}
   \setlength\textwidth{135mm}
   \setlength\textheight{.725\paperheight}
   \setlength{\oddsidemargin}{35mm}
   \setlength{\evensidemargin}{40mm}

   \parindent0mm
   \parskip1ex


   \newlength{\marginWidth}
   \setlength{\marginWidth}{5cm}
   

   \newlength{\fullWidth}
   \setlength{\fullWidth}{\textwidth}
   \addtolength{\fullWidth}{5cm}

   \newlength{\mp@chapnoht} % height of the chapter number
   \newlength{\mp@chaptlht} % height of the chapter title
   \newlength{\EX}
   \settoheight{\EX}{\normalsize X}
   \setlength{\EX}{2\EX}

\endinput
