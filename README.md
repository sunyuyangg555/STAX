STAX
================
Open Source STAF service .

Architect:
---------
![STAX](doc/STAX.png?raw=true "Title")

Features:
---------
- 1、 stax service upgrade jython2.5 to jython2.7;
- 2、 stax support function STAX event;
- 3、 stax support plugins with pf4j

Plugins:
--------
- 1、 xfs plugin using [xfs-service](https://github.com/sunyuyangg555/staf-services/tree/main/services/xfs)
- 2、 pe plugin support 
```
<ini>
    <query>
        <section name="xxx">
            <option name="YYY"></option>
        </section>
    </query>
</ini>

<beep hz="" vol=""></beep>

<dialog></dialog>

```

Build:
---------
Run the bellow command:
```
git clone https://github.com/sunyuyangg555/STAX.git
cd stax
gradle clean build
```

Use plugins

copy `build/plugins/*-*.zip` to `c:/STAF/service/stax/plugins` dir

How to use:
---------

create a test xml:
```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE stax SYSTEM "stax.dtd">
<stax>
    <defaultcall function="Main"/>
    <function name="Main">
        <sequence>
            <dis>
                <sequence>
                    <open>'your.xfs.sp.logicalname'</open>
                    <status/>
                    <script>
                        deviceStatus = json.loads(STAFResult)
                    </script>
                    <if expr="'ONLINE' in deviceStatus['device']">
                        <return>[0, '']</return>
                    </if>
                    <reset>
                        {
                          'number': '0',
                          'retract': {
                            'retractArea': 'RA_RETRACT',
                            'outputPosition': 'NULL',
                            'index': '0'
                          },
                          'outputPosition': 'NULL',
                        }
                    </reset>
                    <dialog>STAFResult</dialog>
                </sequence>
            </dis>
        </sequence>
    </function>
</stax>
```
this xml script do:
- 1、use `dis` a CEN/XFS cdm service to `open` a connection.
- 2、use `status` to get device status
- 3、check the device status is ONLINE
- 4、if not then `reset`

current STAX service support xml elements:
```
<!--
   STAf eXecution (STAX) Document Type Definition (DTD)

   STAX Version: 3.5.17

   STAX Extension Elements: []

   Generated Date: 20210429-10:27:08

   This DTD module is identified by the SYSTEM identifier:

     SYSTEM 'stax.dtd'

-->

<!-- Parameter entities referenced in Element declarations -->

<!ENTITY % stax-elems 'function | script | signalhandler'>

<!ENTITY % task       'control-media | beep | card-unit-info | 
                       fig | release | export-rsa-issuer-signed-item | 
                       set-guid-light | raw-data | ccu | 
                       block | if | form-list | 
                       cash-unit-info | signalhandler | eject-slot-start | 
                       epp | retain-slot-end | cash-in-start | 
                       retain-card | script | call-with-map | 
                       delay | ukey-isr | doc | 
                       reset | terminate | reset-count | 
                       card-isr | media-list | start-exchange | 
                       status | read-form | log | 
                       eject-card | configure-note-types | cash-in-end | 
                       digitalio | bcr | initialization | 
                       set-cash-unit-info | iterate | print-form | 
                       break | read-input | rtn | 
                       testcase | breakpoint | call | 
                       write-raw-data | throw | retract | 
                       cash-in-rollback | end-exchange | return | 
                       open | acc | import-rsa-public-key | 
                       retain-slot-start | action-keys | read-raw-data | 
                       hold | dis | loop | 
                       continue | raise | dispense-card | 
                       mcr | enable-events | cash-in-status | 
                       mcr-idc | dispense | trd | 
                       jnl | import-key | sequence | 
                       set-card-unit-info | idr | currency-exp | 
                       try | job | fdk-keys | 
                       psb | retain-slot-start-ex | import | 
                       crypt | nop | stafcmd | 
                       timer | dialog | parallel | 
                       hcr | paralleliterate | note-type-list | 
                       read-image | on | rethrow | 
                       process | capabilities | ini | 
                       isr-idc | message | cash-in | 
                       call-with-list | rpt | slots-info | 
                       open-shutter | close-shutter | tcstatus'>

<!--================= STAX Job Definition ========================== -->
<!--
     The root element STAX contains all other elements.  It consists
     of an optional defaultcall element and any number of function,
     script, and/or signalhandler elements.
-->
<!ELEMENT stax         ((%stax-elems;)*, defaultcall?, (%stax-elems;)*)>

<!--================= The Default Call Function Element ============ -->
<!--
     The defaultcall element defines the function to call by default
     to start the job.  This can be overridden by the 'FUNCTION'
     parameter when submitting the job to be executed.
     The function attribute's value is a literal.
-->
<!ELEMENT defaultcall  (#PCDATA)>
<!ATTLIST defaultcall
          function     IDREF    #REQUIRED
>
<!ELEMENT control-media     (#PCDATA)><!ELEMENT beep     EMPTY>

<!ATTLIST beep

        hz       CDATA   #IMPLIED

        msecs       CDATA   #IMPLIED

        vol       CDATA   #IMPLIED

        ><!ELEMENT card-unit-info     EMPTY><!ELEMENT fig     (%task;)>
<!--================= The Release Element ========================== -->
<!--
     The release element specifies to release a block in the job.
     If an if attribute is specified and it evaluates via Python to
     false, the release element is ignored.
-->
<!ELEMENT release    EMPTY>
<!ATTLIST release
          block      CDATA    #IMPLIED
          if         CDATA    "1"
>
<!ELEMENT export-rsa-issuer-signed-item     (#PCDATA)><!ELEMENT set-guid-light     (#PCDATA)><!ELEMENT raw-data     (#PCDATA)><!ELEMENT ccu     (%task;)>
<!--================= The Block Element ============================ -->
<!--
     Defines a task block that can be held, released, or terminated.
     Used in conjunction with the hold/terminate/release elements to
     define a task block that can be held, terminated, or released.
     The name attribute value is evaluated via Python.
-->
<!ELEMENT block      (%task;)>
<!ATTLIST block
          name       CDATA    #REQUIRED
>

<!--================= The Conditional Element (if-then-else) ======= -->
<!--
     Allows you to write an if or a case construct with zero or more
     elseifs and one or no else statements.

     The expr attribute value is evaluated via Python and must evaluate
     to a boolean value.
-->
<!ELEMENT if         ((%task;), elseif*, else?)>
<!ATTLIST if
          expr       CDATA   #REQUIRED
>
<!ELEMENT elseif     (%task;)>
<!ATTLIST elseif
          expr       CDATA   #REQUIRED
>
<!ELEMENT else       (%task;)>
<!ELEMENT form-list     EMPTY><!ELEMENT cash-unit-info     EMPTY>
<!--================= The Signal Handler Element =================== -->
<!--
     The signalhandler element defines how to handle a specified signal.
     The signal attribute value is evaluated via Python.
-->
<!ELEMENT signalhandler (%task;)>
<!ATTLIST signalhandler
          signal     CDATA        #REQUIRED
>
<!ELEMENT eject-slot-start     (type, cmdData)>

<!ELEMENT type     (#PCDATA)>

<!ELEMENT cmdData     (#PCDATA)><!ELEMENT epp     (%task;)><!ELEMENT retain-slot-end     (#PCDATA)><!ELEMENT cash-in-start     (#PCDATA)><!ELEMENT retain-card     (#PCDATA)>
<!--================= The Script Element =========================== -->
<!--
     Specifies Python code to be executed.
-->
<!ELEMENT script     (#PCDATA)>

<!--================= The Call-With-Map Element ==================== -->
<!--
     Perform a function with the referenced name with any number of
     arguments in the form of a map of named arguments.  The function
     and name attribute values as well as the argument value are
     evaluated via Python.
-->
<!ELEMENT call-with-map       (call-map-arg*)>
<!ATTLIST call-with-map
          function   CDATA    #REQUIRED
>

<!ELEMENT call-map-arg        (#PCDATA)>
<!ATTLIST call-map-arg
          name       CDATA    #REQUIRED
>
<!ELEMENT delay     (#PCDATA)><!ELEMENT ukey-isr     (%task;)><!ELEMENT doc     (%task;)><!ELEMENT reset     (#PCDATA)>
<!--================= The Terminate Element ======================== -->
<!--
     The terminate element specifies to terminate a block in the job.
     If an if attribute is specified and it evaluates via Python to
     false, the terminate element is ignored.
-->
<!ELEMENT terminate  EMPTY>
<!ATTLIST terminate
          block      CDATA    #IMPLIED
          if         CDATA    "1"
>
<!ELEMENT reset-count     EMPTY><!ELEMENT card-isr     (%task;)><!ELEMENT mdeia-list     EMPTY><!ELEMENT start-exchange     (#PCDATA)><!ELEMENT status     EMPTY><!ELEMENT read-form     (#PCDATA)>
<!--================= The Log Element ============================== -->
<!--
     Writes a message and its log level to a STAX Job User Log file.
     The message must evaluate via Python to a string.

     The log level specified defaults to 'info'.  If specified, it
     must evaluate via Python to a string containing one of the
     following STAF Log Service Log levels:
       fatal, warning, info, trace, trace2, trace3, debug, debug2,
       debug3, start, stop, pass, fail, status, user1, user2, user3,
       user4, user5, user6, user7, user8
     The message attribute is evaluated via Python.  If it evaluates
     to true, the message text will also be sent to the STAX Job Monitor.
     The message attribute defaults to the STAXMessageLog variable whose
     value defaults to 0 (false) but can by changed within the STAX job
     to turn on messaging.

     If an if attribute is specified and it evaluates via Python to
     false, then the log element is ignored.
-->
<!ELEMENT log         (#PCDATA)>
<!ATTLIST log
          level       CDATA       "'info'"
          message     CDATA       "STAXMessageLog"
          if          CDATA       "1"
>
<!ELEMENT eject-card     (#PCDATA)><!ELEMENT configure-note-types     (#PCDATA)><!ELEMENT cash-in-end     EMPTY><!ELEMENT digitalio     (%task;)><!ELEMENT bcr     (%task;)><!ELEMENT initialization     EMPTY><!ELEMENT set-cash-unit-info     (#PCDATA)>
<!--================= The Iterate Element ========================= -->
<!--
     The iterate element iterates through a list of items, performing
     its contained task while substituting each item in the list.
     The iterated tasks are performed in sequence.
-->
<!ELEMENT iterate  (%task;)>
<!-- var      is the name of the variable which will contain the
              current item in the list or tuple being iterated.
              It is a literal.
     in       is the list or tuple to be iterated.  It is evaluated
              via Python and must evaluate to be a list or tuple.
     indexvar is the name of a variable which will contain the index of
              the current item in the list or tuple being iterated.
              It is a literal.  The value for the first index is 0.
-->
<!ATTLIST iterate
          var        CDATA    #REQUIRED
          in         CDATA    #REQUIRED
          indexvar   CDATA    #IMPLIED
>
<!ELEMENT print-form     (#PCDATA)>
<!--================= Break Element ================================ -->
<!--
     The break element can be used to break out of a loop or iterate
     element.
-->
<!ELEMENT break      EMPTY>
<!ELEMENT read-input     (#PCDATA)><!ELEMENT rtn     (%task;)>
<!--================= The Testcase Element ========================= -->
<!--
     Defines a testcase.  Used in conjunction with the tcstatus
     element to mark the status for a testcase.
     The name attribute value is evaluated via Python.
-->
<!ELEMENT testcase   (%task;)>
<!ATTLIST testcase
          name       CDATA    #REQUIRED
          mode       CDATA    "'default'"
>

<!--================= The Breakpoint Element ============================ -->
<!--
     The breakpoint element allows you to denote a breakpoint.
-->
<!ELEMENT breakpoint     EMPTY>

<!--================= The Call Element ============================= -->
<!--
     Perform a function with the referenced name.
     The function attribute value is evaluated via Python.
     Arguments can be specified as data to the call element.
     Arguments are evaluated via Python.
-->
<!ELEMENT call       (#PCDATA)>
<!ATTLIST call
          function   CDATA    #REQUIRED
>

<!ELEMENT write-raw-data     (#PCDATA)>
<!--================= The Throw Element ============================ -->
<!--
     The throw element specifies an exception to throw.
     The exception attribute value and any additional information
     is evaluated via Python.
-->
<!ELEMENT throw      (#PCDATA)>
<!ATTLIST throw
          exception  CDATA        #REQUIRED
>
<!ELEMENT retract     (#PCDATA)><!ELEMENT cash-in-rollback     EMPTY><!ELEMENT end-exchange     (#PCDATA)>
<!--================= The Return Element =========================== -->
<!--
     Specifies a value to return from a function.
-->
<!ELEMENT return     (#PCDATA)>
<!ELEMENT open     (#PCDATA)><!ELEMENT acc     (%task;)><!ELEMENT import-rsa-public-key     (#PCDATA)><!ELEMENT retain-slot-start     EMPTY><!ELEMENT action-keys     (#PCDATA)><!ELEMENT read-raw-data     (#PCDATA)>
<!--================= The Hold Element ============================= -->
<!--
     The hold element specifies to hold a block in the job.
     If an if attribute is specified and it evaluates via Python to
     false, the hold element is ignored.

     The default timeout is 0 which specifies to hold the block
     indefinitely.  A non-zero timeout value specifies the maximum time
     that the block will be held,  The timeout can be expressed in
     milliseconds, seconds, minutes, hours, days, or weeks.
     It is evaluated via Python.
       Examples:  timeout="'1000'"   (1000 milliseconds or 1 second)
                  timeout="'5s'"     (5 seconds)
                  timeout="'1m'"     (1 minute)
                  timeout="'2h'"     (2 hours)
                  timeout="0"        (hold indefinitely)
-->
<!ELEMENT hold       EMPTY>
<!ATTLIST hold
          block      CDATA    #IMPLIED
          if         CDATA    "1"
          timeout    CDATA    "0"
>
<!ELEMENT dis     (%task;)>
<!--================= The Loop Element ============================= -->
<!--
     The loop element performs a task a specified number of times,
     allowing specification of an upper and lower bound with an
     increment value and where the index counter is available to
     sub-tasks.  Also, while and/or until expressions can be
     specified.
-->
<!ELEMENT loop       (%task;)>
<!-- var      is the name of a variable which will contain the loop
              index variable.  It is a literal.
     from     is the starting value of the loop index variable.
              It must evaluate to an integer value via Python.
     to       is the maximum value of the loop index variable
              It must evaluate to an integer value via Python.
     by       is the increment value for the loop index variable
              It must evaluate to an integer value via Python.
     while    is an expression that must evaluate to a boolean value
              and is performed at the top of each loop.  If it
              evaluates to false, it breaks out of the loop.
     until    is an expression that must evaluate to a boolean value
              and is performed at the bottom of each loop.  If it
              evaluates to false, it breaks out of the loop.
-->
<!ATTLIST loop
          var        CDATA    #IMPLIED
          from       CDATA    '1'
          to         CDATA    #IMPLIED
          by         CDATA    '1'
          while      CDATA    #IMPLIED
          until      CDATA    #IMPLIED
>

<!--================= Continue Element ============================= -->
<!--
     The continue element can be used to continue to the top of a loop
     or iterate element.
-->
<!ELEMENT continue   EMPTY>

<!--================= The Function Element ========================= -->
<!--
     The function element defines a named task which can be called.
     The name, requires, and scope attribute values are literals.
     If desired, the function can be described using a function-prolog
     element (or the deprecated function-description element) and/or a
     function-epilog element.  Also, if desired, the function element
     can define the arguments that can be passed to the function.
     The function element can also define any number of function-import
     elements if it requires functions from other xml files.  The
     function-import element must specify either the file or directory
     attribute.
-->
<!ELEMENT function    ((function-prolog | function-description)?,
                       (function-epilog)?,
                       (function-import)*,
                       (function-no-args | function-single-arg |
                        function-list-args | function-map-args)?,
                       (%task;))>
<!ATTLIST function
          name         ID       #REQUIRED
          requires     IDREFS   #IMPLIED
          scope        (local | global) "global"
>

<!ELEMENT function-prolog       (#PCDATA)>

<!ELEMENT function-epilog       (#PCDATA)>

<!ELEMENT function-description  (#PCDATA)>

<!ELEMENT function-import       (#PCDATA)>
<!ATTLIST function-import
          file         CDATA    #IMPLIED
          directory    CDATA    #IMPLIED
          machine      CDATA    #IMPLIED
>
<!ELEMENT function-no-args      EMPTY>

<!ELEMENT function-single-arg   (function-required-arg |
                                 function-optional-arg |
                                 function-arg-def)>

<!ELEMENT function-list-args    ((((function-required-arg+,
                                    function-optional-arg*) |
                                  (function-required-arg*,
                                    function-optional-arg+)),
                                 (function-other-args)?) |
                                 function-arg-def+)>

<!ELEMENT function-map-args     (((function-required-arg |
                                   function-optional-arg)+,
                                  (function-other-args+)?) |
                                  function-arg-def+)>

<!ELEMENT function-required-arg (#PCDATA)>
<!ATTLIST function-required-arg
          name         CDATA    #REQUIRED
>

<!ELEMENT function-optional-arg (#PCDATA)>
<!ATTLIST function-optional-arg
          name         CDATA    #REQUIRED
          default      CDATA    "None"
>

<!ELEMENT function-other-args   (#PCDATA)>
<!ATTLIST function-other-args
          name         CDATA    #REQUIRED
>

<!ELEMENT function-arg-def      (function-arg-description?,
                                 function-arg-private?,
                                 function-arg-property*)>
<!ATTLIST function-arg-def
          name         CDATA    #REQUIRED
          type         (required | optional | other) "required"
          default      CDATA    "None"
>

<!ELEMENT function-arg-description  (#PCDATA)>

<!ELEMENT function-arg-private   EMPTY>

<!ELEMENT function-arg-property  (function-arg-property-description?,
                                 function-arg-property-data*)>
<!ATTLIST function-arg-property
          name         CDATA    #REQUIRED
          value        CDATA    #IMPLIED
>

<!ELEMENT function-arg-property-description  (#PCDATA)>

<!ELEMENT function-arg-property-data (function-arg-property-data)*>
<!ATTLIST function-arg-property-data
          type         CDATA    #REQUIRED
          value        CDATA    #IMPLIED
>

<!--================= The Raise Element ============================ -->
<!--
     A raise signal element raises a specified signal.
     Signals can also be raised by the STAX execution engine.
     The signal attribute value is evaluated via Python.
-->
<!ELEMENT raise      EMPTY>
<!ATTLIST raise
          signal     CDATA        #REQUIRED
>
<!ELEMENT dispense-card     (#PCDATA)><!ELEMENT mcr     (%task;)><!ELEMENT enable-events     EMPTY><!ELEMENT cash-in-status     EMPTY><!ELEMENT mcr-idc     (%task;)><!ELEMENT dispense     (#PCDATA)><!ELEMENT trd     (%task;)><!ELEMENT jnl     (%task;)><!ELEMENT import-key     (#PCDATA)>
<!--================= The Sequence Element ========================= -->
<!--
     The sequence element performs one or more tasks in sequence.
-->
<!ELEMENT sequence   (%task;)+>
<!ELEMENT set-card-unit-info     (#PCDATA)><!ELEMENT idr     (%task;)><!ELEMENT currency-exp     EMPTY>
<!--=============== The Try / Catch / Finally Elements ============= --> 
<!-- 
     The try element allows you to perform a task and to catch 
     exceptions that are thrown.  Also, if a finally element is 
     specified, then the finally task is executed, no matter whether 
     the try task completes normally or abruptly, and no matter whether 
     a catch element is first given control. 
--> 
<!ELEMENT try        ((%task;), ((catch+) | ((catch*), finally)))> 
<!-- 
     The catch element performs a task when the specified exception is 
     caught.  The var attribute specifies the name of the variable to 
     receive the data specified within the throw element.  The typevar 
     attribute specifies the name of the variable to receive the type 
     of the exception.  The sourcevar attribute specifies the name
     of the variable to receive the source information for the exception.
 
--> 
<!ELEMENT catch      (%task;)> 
<!ATTLIST catch 
          exception  CDATA        #REQUIRED 
          var        CDATA        #IMPLIED 
          typevar    CDATA        #IMPLIED 
          sourcevar  CDATA        #IMPLIED 
> 
<!ELEMENT finally    (%task;)> 

<!--================== The STAX Job Element ===================== -->
<!--
     Specifies a STAX sub-job to be executed.  This element is equivalent
     to a STAX EXECUTE request.

     The name attribute specifies the name of the job. The job name
     defaults to the value of the function name called to start the job.
     Its name and all of its element values are evaluated via Python.
     The job element must contain a location element and either a
     file or data element.  This attribute is equivalent to the
     JOBNAME option for a STAX EXECUTE command.

     The clearlogs attribute specifies to delete the STAX Job and Job
     User logs before the job is executed to ensure that only one job's
     contents are in the log.  This attribute is equivalent to the
     CLEARLOGS option for a STAX EXECUTE command.  The default is the
     same option that was specified for the parent job.  Valid values
     include 'parent', 'default', 'enabled', and 'disabled'.

     The monitor attribute specifies whether to automatically monitor the
     subjob.  Note that 'Automatically monitor recommended sub-jobs' must
     be selected in the STAX Job Monitor properties in order for it to be
     used.  The default value for the monitor attribute is 0, a false
     value.

     The logtcelapsedtime attribute specifies to log the elapsed time
     for a testcase in the summary record in the STAX Job log and on a
     LIST TESTCASES request.  This attribute is equivalent to the
     LOGTCELAPSEDTIME option for a STAX EXECUTE command.  The default is
     the same option that was specified for the parent job.  Valid values
     include 'parent', 'default', 'enabled', and 'disabled'.

     The logtcnumstarts attribute specifies to log the number of starts
     for a testcase in the summary record in the STAX Job log and on a
     LIST TESTCASES request.  This attribute is equivalent to the
     LOGNUMSTARTS option for a STAX EXECUTE command.  The default is
     the same option that was specified for the parent job.  Valid values
     include 'parent', 'default', 'enabled', and 'disabled'.

     The logtcstartstop attribute specifies to log start/stop records
     for testcases in the STAX Job log.  This attribute is equivalent to
     the LOGTCSTARTSTOP option for a STAX EXECUTE command.  The default
     is the same option that was specified for the parent job.  Valid
     values include 'parent', 'default', 'enabled', and 'disabled'.

     The pythonoutput attribute specifies where to write Python stdout/stderr
     (e.g. from a print statement in a script element).  This attribute
     is equivalent to the PYTHONOUTPUT option for a STAX EXECUTE command.
     The default is the same option that was specified for the parent job.
     Valid values include 'parent', 'default', 'jobuserlog', 'message',
     'jobuserlogandmsg', and 'jvmlog'.

     The pythonloglevel attribute specifies the log level to use when writing
     Python stdout (e.g. from a print statement in a script element) if the
     python output is written to the STAX Job User Log.  This attribute is
     equivalent to the PYTHONLOGLEVEL option for a STAX EXECUTE command.
     The default is the same option that was specified for the parent job.
     Valid values include 'parent', 'default', or a valid STAF log level
     such as 'Info', 'Trace', 'User1', etc.

     The invalidloglevelaction attribute specifies the action to take when a
     log or message element uses an invalid STAF logging level.  This attribute
     is equivalent to the INVALIDLOGLEVELACTION option for a STAX EXECUTE
     command.  The default is the same option that was specified for the parent
     job.  Valid values include 'parent', 'default', 'raisesignal', and
     'loginfo'.

     The job element must contain either a job-file or job-data element.

     The job element has the following optional elements:
       job-function, job-function-args, job-scriptfile(s), job-script,
       job-hold, and job-action

     Each of these optional elements may specify an if attribute.
     The if attribute must evaluate via Python to a true or false value.
     If it does not evaluate to a true value, the element is ignored.
     The default value for the if attribute is 1, a true value.
     Note that in Python, true means any nonzero number or nonempty
     object; false means not true, such as a zero number, an empty
     object, or None. Comparisons and equality tests return 1 or 0
     (true or false).
-->
<!ELEMENT job        ((job-file | job-data),
                      job-function?, job-function-args?,
                      (job-scriptfile | job-scriptfiles)?,
                      job-script*, job-hold?, job-action?)>
<!ATTLIST job
          name                  CDATA   #IMPLIED
          clearlogs             CDATA   "'parent'"
          monitor               CDATA   #IMPLIED
          logtcelapsedtime      CDATA   "'parent'"
          logtcnumstarts        CDATA   "'parent'"
          logtcstartstop        CDATA   "'parent'"
          pythonoutput          CDATA   "'parent'"
          pythonloglevel        CDATA   "'parent'"
          invalidloglevelaction CDATA   "'parent'"
>

<!--
     The job-file element specifies the fully qualified name of a file
     containing the XML document for the STAX job to be executed.
     The job-file element is equivalent to the FILE option for a STAX
     EXECUTE command.

     The machine attribute specifies the name of the machine where the
     xml file is located.  If not specified, it defaults to Python
     variable STAXJobXMLMachine.  The machine attribute is equivalent
     to the MACHINE option for a STAX EXECUTE command.
  -->
<!ELEMENT job-file           (#PCDATA)>
<!ATTLIST job-file
          machine    CDATA   "STAXJobXMLMachine"
>

<!--
     The job-data element specifies a string containing the XML document
     for the job to be executed.  This element is equivalent to the
     DATA option for a STAX EXECUTE command.

     The eval attribute specifies whether the data is be evaluated by
     Python in the parent job.  For example, if the job-data information
     is dynamically generated and assigned to a Python variable, rather
     than just containing the literal XML information, then you would
     need to set the eval attribute to true (e.g. eval="1").
     The default for the eval attribute is false ("0").
  -->
<!ELEMENT job-data           (#PCDATA)>
<!ATTLIST job-data
          eval       CDATA   "0"
>

<!--
     The job-function element specifies the name of the function element
     to call to start the job, overriding the defaultcall element, if any,
     specified in the XML document. The <function name> must be the name
     of a function element specified in the XML document. This element is
     equivalent to the FUNCTION option for a STAX EXECUTE command.
-->
<!ELEMENT job-function       (#PCDATA)>
<!ATTLIST job-function
          if         CDATA   "1"
>

<!--
     The job-function-args element specifies arguments to pass to the
     function element called to start the job, overriding the arguments,
     if any, specified for the defaultcall element in the XML document.
     This element is equivalent to the ARGS option for a STAX EXECUTE
     command.

     The eval attribute specifies whether the data is to be evaluated
     by Python in the parent job.  The default for the eval attribute
     is false ("0").
-->
<!ELEMENT job-function-args  (#PCDATA)>
<!ATTLIST job-function-args
          if         CDATA   "1"
          eval       CDATA   "0"
>

<!--
     The job-script element specifies Python code to be executed.
     This element is equivalent to the SCRIPT option for a STAX
     EXECUTE command.  Multiple job-script elements may be specified.

     The eval attribute specifies whether the data is to be evaluated
     by Python in the parent job.  The default for the eval attribute
     is false ("0").
-->
<!ELEMENT job-script         (#PCDATA)>
<!ATTLIST job-script
          if         CDATA   "1"
          eval       CDATA   "0"
>

<!--
     The job-scriptfile element (equivalent to the job-scriptfiles
     element) specifies the fully qualified name of a file containing
     Python code to be executed, or a list of file names containing
     Python code to be executed. The value must evaluate via Python to
     a string or a list of strings. This element is equivalent to the
     SCRIPTFILE option for a STAX EXECUTE command.

     Specifying only one scriptfile could look like either:
       ['C:/stax/scriptfiles/scriptfile1.py']      or 
       'C:/stax/scriptfiles/scriptfiel1.py'
     Specifying a list containing 3 scriptfiles could look like:
       ['C:/stax/scriptfiles/scriptfile1.py',
        'C:/stax/scriptfiles/scriptfile2.py',
         C:/stax/scriptfiles/scriptfile2.py' ]

     The machine attribute specifies the name of the machine where the
     SCRIPTFILE(s) are located. If not specified, it defaults to Python
     variable STAXJobScriptFileMachine.  This attribute is equivalent
     to the SCRIPTFILEMACHINE option for a STAX EXECUTE command.
-->
<!ELEMENT job-scriptfile     (#PCDATA)>
<!ATTLIST job-scriptfile
          if         CDATA   "1"
          machine    CDATA   "STAXJobScriptFileMachine"
>

<!ELEMENT job-scriptfiles    (#PCDATA)>
<!ATTLIST job-scriptfiles
          if         CDATA   "1"
          machine    CDATA   "STAXJobScriptFileMachine"
>

<!--
     The job-hold element specifies to hold the job.  This element is
     equivalent to the HOLD option for a STAX EXECUTE command,

     The default timeout is 0 which specifies to hold the job indefinitely.
     A non-zero timeout value specifies the maximum time that the job
     will be held,  The timeout can be expressed in milliseconds, seconds,
     minutes, hours, days, weeks, or years.  It is evaluated via Python.
       Examples:  timeout="'1000'"   (1000 milliseconds or 1 second)
                  timeout="'5s'"     (5 seconds)
                  timeout="'1m'"     (1 minute)
                  timeout="'2h'"     (2 hours)
                  timeout="'0'"      (hold indefinitely)
-->
<!ELEMENT job-hold           (#PCDATA)>
<!ATTLIST job-hold
          if         CDATA   "1"
          timeout    CDATA   "0"
>

<!--
     The job-action element specifies a task to be executed after the
     sub-job has started. This task will be executed in parallel with
     the sub-job via a new STAX-Thread. The task will be able to use the
     STAXSubJobID variable to obtain the sub-job ID in order to interact
     with the job. If the job completes before the task completes, the
     job will remain in a non-complete state until the task completes.
     If the job cannot be started, the job-action task is not executed.
-->
<!ELEMENT job-action         (%task;)>
<!ATTLIST job-action
          if        CDATA    "1"
>
<!ELEMENT fdk-keys     (#PCDATA)><!ELEMENT psb     (%task;)><!ELEMENT retain-slot-start-ex     (#PCDATA)>
<!--================= The Import Element =========================== -->
<!--
     Allows importing of functions from other STAX XML job file(s).
     Either the file or directory attribute must be specified.
     All attributes and sub-elements are evaluated via Python.
-->
<!ELEMENT import         (import-include?, import-exclude?)?>
<!ATTLIST import
          file           CDATA       #IMPLIED
          directory      CDATA       #IMPLIED
          machine        CDATA       #IMPLIED
          replace        CDATA       "0"
          mode           CDATA       "'error'"
>
<!ELEMENT import-include (#PCDATA)>
<!ELEMENT import-exclude (#PCDATA)>

<!ELEMENT crypt     (#PCDATA)>
<!--================= The No Operation Element ===================== -->
<!--
     No operation action.
-->
<!ELEMENT nop        EMPTY>

<!--================= The STAF Command Element ===================== -->
<!--
     Specifies a STAF command to be executed.
     Its name and all of its element values are evaluated via Python.
-->
<!ELEMENT stafcmd    (location, service, request)>
<!ATTLIST stafcmd
          name       CDATA   #IMPLIED
>
<!ELEMENT service    (#PCDATA)>
<!ELEMENT request    (#PCDATA)>

<!--================= The Timer Element ============================ -->
<!--
     The timer element runs a task for a specified duration.
     If the task is still running at the end of the specified duration,
     then the RC variable is set to 1, else if the task ended before
     the specified duration, the RC variable is set to 0, else if the
     timer could not start due to an invalid duration, the RC variable
     is set to -1.
-->
<!ELEMENT timer     (%task;)>
<!-- duration is the maximum length of time to run the task.
       Time can be expressed in milliseconds, seconds, minutes,
       hours, days, weeks, or years.  It is evaluated via Python.
         Examples:  duration='50'    (50 milliseconds)
                    duration='90s'   (90 seconds)
                    duration='5m'    ( 5 minutes)
                    duration='36h'   (36 hours)
                    duration='3d'    ( 3 days)
                    duration='1w'    ( 1 week)
                    duration='1y'    ( 1 year)
-->
<!ATTLIST timer
          duration   CDATA        #REQUIRED
>
<!ELEMENT dialog     (#PCDATA)>
<!--================= The Parallel Element ========================= -->
<!--
     The parallel element performs one or more tasks in parallel.
-->
<!ELEMENT parallel   (%task;)+>
<!ELEMENT hcr     (%task;)>
<!--================= The Parallel Iterate Element ================ -->
<!--
     The parallel iterate element iterates through a list of items,
     performing its contained task while substituting each item in
     the list.  The iterated tasks are performed in parallel.
-->
<!ELEMENT paralleliterate  (%task;)>
<!-- var      is the name of a variable which will contain the current
              item in the list or tuple being iterated.
              It is a literal.
     in       is the list or tuple to be iterated.  It is evaluated
              via Python and must evaluate to be a list or tuple.
     indexvar is the name of a variable which will contain the index of
              the current item in the list or tuple being iterated.
              It is a literal.  The value of the first index is 0.
     maxthreads is the maximum number of STAX-Threads that can be running
              simultaneously at a time.  It must evaluate to an integer
              value >= 0 via Python.  The default is 0 which means
              there is no maximum.
-->
<!ATTLIST paralleliterate
          var        CDATA    #REQUIRED
          in         CDATA    #REQUIRED
          indexvar   CDATA    #IMPLIED
          maxthreads CDATA    "0"
>
<!ELEMENT note-type-list     EMPTY><!ELEMENT read-image     (#PCDATA)><!ELEMENT on     (#PCDATA)>
<!--================= The Rethrow Element ========================= -->
<!--
     The rethrow element specifies to rethrow the current exception.
-->
<!ELEMENT rethrow      EMPTY>

<!--================= The STAF Process Element ===================== -->
<!--
     Specifies a STAF process to be started.
     All of its non-empty element values are evaluated via Python.
-->
<!ENTITY % procgroup1 '((parms?, workdir?) | (workdir?, parms?))'>
<!ENTITY % procgroup2 '((title?, workload?) | (workload?, title?))'>
<!ENTITY % procgroup1a '((parms?, workload?) | (workload?, parms?))'>
<!ENTITY % procgroup2a '((title?, workdir?) | (workdir?, title?))'>
<!ENTITY % procgroup3 '(((vars | var | envs | env)*, useprocessvars?) |
                        (useprocessvars?, (vars | var | envs | env)*))'>
<!ENTITY % procgroup4 '(((username, password?)?, disabledauth?) |
                        ((disabledauth?, (username, password?)?)))'>
<!ENTITY % procgroup5 '((stdin?, stdout?, stderr?) |
                        (stdout?, stderr?, stdin?) |
                        (stderr?, stdin?, stdout?) |
                        (stdin?, stderr?, stdout?) |
                        (stdout?, stdin?, stderr?) |
                        (stderr?, stdout?, stdin?))'>
<!ENTITY % returnfileinfo '(returnfiles | returnfile)*'>
<!ENTITY % procgroup5a '((%returnfileinfo;, returnstdout?, returnstderr?) |
                        (returnstdout?, returnstderr?, %returnfileinfo;) |
                        (returnstderr?, %returnfileinfo;, returnstdout?) |
                        (%returnfileinfo;, returnstderr?, returnstdout?) |
                        (returnstdout?, %returnfileinfo;, returnstderr?) |
                        (returnstderr?, returnstdout?, %returnfileinfo;))'>
<!ENTITY % procgroup6 '((stopusing?, console?, focus?, statichandlename?) |
                        (stopusing?, console?, statichandlename?, focus?) |
                        (stopusing?, focus?, console?, statichandlename?) |
                        (stopusing?, focus?, statichandlename?, console?) |
                        (stopusing?, statichandlename?, console?, focus?) |
                        (stopusing?, statichandlename?, focus?, console?) |
                        (console?, focus?, stopusing?, statichandlename?) |
                        (console?, focus?, statichandlename?, stopusing?) |
                        (console?, stopusing?, focus?, statichandlename?) |
                        (console?, stopusing?, statichandlename?, focus?) |
                        (console?, statichandlename?, focus?, stopusing?) |
                        (console?, statichandlename?, stopusing?, focus?) |
                        (focus?, console?, stopusing?, statichandlename?) |
                        (focus?, console?, statichandlename?, stopusing?) |
                        (focus?, stopusing?, console?, statichandlename?) |
                        (focus?, stopusing?, statichandlename?, console?) |
                        (focus?, statichandlename?, console?, stopusing?) |
                        (focus?, statichandlename?, stopusing?, console?) |
                        (statichandlename?, stopusing?, console?, focus?) |
                        (statichandlename?, stopusing?, focus?, console?) |
                        (statichandlename?, console?, focus?, stopusing?) |
                        (statichandlename?, console?, stopusing?, focus?) |
                        (statichandlename?, focus?, console?, stopusing?) |
                        (statichandlename?, focus?, stopusing?, console?))'>
<!ELEMENT process    (location, command,
                      ((%procgroup1;, %procgroup2;) |
                       (%procgroup2;, %procgroup1;) |
                       (%procgroup1a;, %procgroup2a;) |
                       (%procgroup2a;, %procgroup1a;)),
                      %procgroup3;,
                      ((%procgroup4;, %procgroup5;, %procgroup5a;, %procgroup6;) |
                       (%procgroup4;, %procgroup6;, %procgroup5;, %procgroup5a;) |
                       (%procgroup5;, %procgroup5a;, %procgroup4;, %procgroup6;) |
                       (%procgroup5;, %procgroup5a;, %procgroup6;, %procgroup4;) |
                       (%procgroup6;, %procgroup4;, %procgroup5;, %procgroup5a;) |
                       (%procgroup6;, %procgroup5;, %procgroup5a;, %procgroup4;)),
                      other?, process-action?)>
<!ATTLIST process
          name        CDATA   #IMPLIED
>

<!--
     The process element must contain a location element and a
     command element.
-->
<!ELEMENT location            (#PCDATA)>
<!ELEMENT command             (#PCDATA)>
<!ATTLIST command
          mode        CDATA   "'default'"
          shell       CDATA   #IMPLIED
>

<!--
     The parms element specifies any parameters that you wish to
     pass to the command.
     The value is evaluated via Python to a string.
-->
<!ELEMENT parms               (#PCDATA)>
<!ATTLIST parms
          if        CDATA     "1"
>

<!--
     The workload element specifies the name of the workload for
     which this process is a member.  This may be useful in
     conjunction with other process elements.
     The value is evaluated via Python to a string.
-->
<!ELEMENT workload            (#PCDATA)>
<!ATTLIST workload
          if        CDATA     "1"
>

<!--
     The title element specifies the program title of the process.
     Unless overridden by the process, the title will be the text
     that is displayed on the title bar of the application.
     The value is evaluated via Python to a string.
-->
<!ELEMENT title               (#PCDATA)>
<!ATTLIST title
          if        CDATA     "1"
>

<!--
     The workdir element specifies the directory from which the
     command should be executed.  If you do not specify this
     element, the command will be started from whatever directory
     STAFProc is currently in.
     The value is evaluated via Python to a string.
-->
<!ELEMENT workdir             (#PCDATA)>
<!ATTLIST workdir
          if        CDATA     "1"
>

<!--
     The vars (and var) elements specify STAF variables that go into the
     process specific STAF variable pool.
     The value must evaluate via Python to a string or a list of 
     strings. Multiple vars elements may be specified for a process.
     The format for each variable is:
       'varname=value'
     So, a list containing 3 variables could look like:
       ['var1=value1', 'var2=value2', 'var3=value3']
     Specifying only one variable could look like either:
       ['var1=value1']      or 
       'var1=value1'
-->
<!ELEMENT vars                (#PCDATA)>
<!ATTLIST vars
          if        CDATA     "1"
>

<!ELEMENT var                 (#PCDATA)>
<!ATTLIST var
          if        CDATA     "1"
>

<!--
     The envs (and env) elements specify environment variables that will
     be set for the process.  Environment variables may be mixed case,
     however most programs assume environment variable names will
     be uppercase, so, in most cases, ensure that your environment
     variable names are uppercase.
     The value must evaluate via Python to a string or a list of 
     strings. Multiple envs elements may be specified for a process.
     The format for each variable is:
       'varname=value'
     So, a list containing 3 variables could look like:
       ['ENV_VAR_1=value1', 'ENV_VAR_2=value2', 'ENV_VAR_3=value3']
     Specifying only one variable could look like either:
       ['ENV_VAR_1=value1']      or 
       'ENV_VAR_1=value1'
-->
<!ELEMENT envs                (#PCDATA)>
<!ATTLIST envs
          if        CDATA     "1"
>

<!ELEMENT env                 (#PCDATA)>
<!ATTLIST env
          if        CDATA     "1"
>
<!--
     The useprocessvars element specifies that STAF variable
     references should try to be resolved from the STAF variable
     pool associated with the process being started first.
     If the STAF variable is not found in this pool, the STAF
     global variable pool should then be searched.
-->
<!ELEMENT useprocessvars      EMPTY>
<!ATTLIST useprocessvars
          if        CDATA     "1"
>

<!--
     The stopusing element allows you to specify the method by
     which this process will be STOPed, if not overridden on the
     STOP command.
     The value is evaluated via Python to a string.
-->
<!ELEMENT stopusing           (#PCDATA)>
<!ATTLIST stopusing
          if        CDATA     "1"
>

<!--
     The console element allows you to specify if the process should
     get a new console window or share the STAFProc console.

     use    Must evaluate via Python to a string containing either:
            - 'new' specifies that the process should get a new console
              window.  This option only has effect on Windows systems.
              This is the default for Windows systems.
            - 'same' specifies that the process should share the
              STAFProc console.  This option only has effect on Windows
              systems.  This is the default for Unix systems.
-->
<!ELEMENT console             EMPTY>
<!ATTLIST console
          if        CDATA     "1"
          use       CDATA     #REQUIRED
>

<!--
     The focus element allows you to specify the focus that is to be
     given to any new windows opened when starting a process on a Windows
     system.  The window(s) it effects depends on whether you are using
     the 'default' or the 'shell' command mode:
       - Default command mode (no SHELL option):  The focus specified is
         given to any new windows opened by the command specified.
       - Shell command mode:  The focus specified is given only to the
         new shell command window opened, not to any windows opened by
         the specified command.

     The focus element only has effect on Windows systems and requires
     STAF V3.1.4 or later on the machine where the process is run.

     mode   Must evaluate via Python to a string containing one of the
            following values:
            - 'background' specifies to display a window in the background
              (e.g. not give it focus) in its most recent size and position.
              This is the default.
            - 'foreground' specifies to display a window in the foreground
              (e.g. give it focus) in its most recent size and position.
            - 'minimized' specifies to display a window as minimized.
-->
<!ELEMENT focus               EMPTY>
<!ATTLIST focus
          if        CDATA     "1"
          mode      CDATA     #REQUIRED
>

<!--
     The username element specifies the username under which 
     the process should be started.
     The value is evaluated via Python to a string.
-->
<!ELEMENT username            (#PCDATA)>
<!ATTLIST username
          if        CDATA     "1"
>

<!--
     The password element specifies the password with which to
     authenticate the user specified with the username element.
     The value is evaluated via Python to a string.
-->
<!ELEMENT password            (#PCDATA)>
<!ATTLIST password
          if        CDATA     "1"
>

<!-- The disabledauth element specifies the action to take if a
     username/password is specified but authentication has been disabled.

     action  Must evaluate via Python to a string containing either:
             - 'error' specifies that an error should be returned.
             - 'ignore'  specifies that any username/password specified
               is ignored if authentication is disabled.
             This action overrides any default specified in the STAF
             configuration file.
-->
<!ELEMENT disabledauth        EMPTY>
<!ATTLIST disabledauth
          if        CDATA     "1"
          action    CDATA     #REQUIRED
>

<!--
     Specifies that a static handle should be created for this process.
     The value is evaluated via Python to a string.  It will be the
     registered name of the static handle.  Using this option will also
     cause the environment variable STAF_STATIC_HANDLE to be set
     appropriately for the process.
-->
<!ELEMENT statichandlename    (#PCDATA)>
<!ATTLIST statichandlename
          if        CDATA     "1"
>

<!--
     The stdin element specifies the name of the file from which
     standard input will be read.  The value is evaluated via
     Python to a string.
-->
<!ELEMENT stdin               (#PCDATA)>
<!ATTLIST stdin
          if        CDATA     "1"
>

<!--
     The stdout element specifies the name of the file to which
     standard output will be redirected.
     The mode and filename are evaluated via Python to a string.
-->
<!ELEMENT stdout              (#PCDATA)>
<!--  mode  specifies what to do if the file already exists.
            The value must evaluate via Python to one of the
            following:
            'replace' - specifies that the file will be replaced.
            'append'  - specifies that the process' standard
                        output will be appended to the file.
-->
<!ATTLIST stdout
          if        CDATA     "1"
          mode      CDATA     "'replace'"
>

<!--
     The stderr element specifies the file to which standard error will
     be redirected. The mode and filename are evaluated via Python to a
     string.
-->
<!ELEMENT stderr              (#PCDATA)>
<!-- mode   specifies what to do if the file already exists or to
            redirect standard error to the same file as standard output.
            The value must evaluate via Python to one of the following:
            'replace' - specifies that the file will be replaced.
            'append'  - specifies that the process's standard error will
                        be appended to the file.
            'stdout'  - specifies to redirect standard error to the
                        same file to which standard output is redirected.
                        If a file name is specified, it is ignored.
-->
<!ATTLIST stderr
          if        CDATA     "1"
          mode      CDATA     "'replace'"
>

<!--
     The returnstdout element specifies to return in STAXResult
     the contents of the file where standard output was redirected
     when the process completes.
-->
<!ELEMENT returnstdout        EMPTY>
<!ATTLIST returnstdout
          if        CDATA     "1"
>

<!--
     The returnstderr element specifies to return in STAXResult
     the contents of the file where standard error was redirected
     when the process completes.
-->
<!ELEMENT returnstderr        EMPTY>
<!ATTLIST returnstderr
          if        CDATA     "1"
>

<!--
     The returnfiles (and returnfile) elements specify that the
     contents of the specified file(s) should be returned in
     STAXResult when the process completes.  The value must evaluate
     via Python to a string or a list of strings.  Multiple returnfile(s)
     elements may be specified for a process.
-->
<!ELEMENT returnfiles         (#PCDATA)>
<!ATTLIST returnfiles
          if        CDATA     "1"
>

<!ELEMENT returnfile          (#PCDATA)>
<!ATTLIST returnfile
          if        CDATA     "1"
>

<!--
     The process-action element specifies a task to be executed
     when a process has started.
-->
<!ELEMENT process-action      (%task;)>
<!ATTLIST process-action
          if        CDATA     "1"
>
<!--
     The other element specifies any other STAF parameters that
     may arise in the future.  It is used to pass additional data
     to the STAF PROCESS START request.
     The value is evaluated via Python to a string.
-->
<!ELEMENT other               (#PCDATA)>
<!ATTLIST other
          if        CDATA     "1"
>
<!ELEMENT capabilities     EMPTY><!--

<ini load="file/path">

  <query>

    <section name="xxx">

      <option name="yyy"></option>

    </section>

  </query>

</ini>

-->

<!ELEMENT ini     (query| update| remove)>

        <!ATTLIST ini

                path       CDATA   #IMPLIED

                >

        <!ELEMENT query     (section)>



        <!ELEMENT update     (section)>

        <!ELEMENT remove     (section)>



        <!ELEMENT section     (option)>

        <!ATTLIST section

                name       CDATA   #IMPLIED

                >

        <!ELEMENT option     (#PCDATA)>

        <!ATTLIST option

                name       CDATA   #IMPLIED

                ><!ELEMENT isr-idc     (%task;)>
<!--================= The Message Element ========================== -->
<!--
     Generates an event and makes the message value available to the
     STAX Job Monitor.  The message must evaluate via Python to a string.

     The log attribute is evaluated via Python to a boolean.  If it
     evaluates to true, the message text will also be logged in the STAX
     Job User log.  The log attribute defaults to the STAXLogMessage
     variable whose value defaults to 0 (false) but can by changed within
     the STAX job to turn on logging.

     The log level is ignored if the log attribute does not evaluate to
     true.  It defaults to 'info'.  If specified, it must evaluate via
     Python to a string containing one of the following STAF Log Service
     logging levels:
       fatal, warning, info, trace, trace2, trace3, debug, debug2,
       debug3, start, stop, pass, fail, status, user1, user2, user3,
       user4, user5, user6, user7, user8

     If an if attribute is specified and it evaluates via Python to
     false, the message element is ignored.
-->
<!ELEMENT message     (#PCDATA)>
<!ATTLIST message
          log         CDATA       "STAXLogMessage"
          level       CDATA       "'info'"
          if          CDATA       "1"
>
<!ELEMENT cash-in     EMPTY>
<!--================= The Call-With-List Element =================== -->
<!--
     Perform a function with the referenced name with any number of
     arguments in the form of a list.  The function attribute value
     and argument values are evaluated via Python.
-->
<!ELEMENT call-with-list      (call-list-arg*)>
<!ATTLIST call-with-list
          function   CDATA    #REQUIRED
>

<!ELEMENT call-list-arg       (#PCDATA)>

<!ELEMENT rpt     (%task;)><!ELEMENT solts-info     EMPTY><!ELEMENT open-shutter     (#PCDATA)><!ELEMENT close-shutter     (#PCDATA)>
<!--================= The Testcase Status Element ================== -->
<!--
     Marks status result ('pass' or 'fail' or 'info') for a testcase
     and allows additional information to be specified.  The status
     result and the additional info is evaluated via Python.
-->
<!ELEMENT tcstatus   (#PCDATA)>
<!ATTLIST tcstatus
          result     CDATA  #REQUIRED
>


```

License
=====================
Code of STAX is licensed under [Apache License 2.0][10].