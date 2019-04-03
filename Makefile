# config
target	= concurrente
headers =

# logic
DIR_SRC		=	src
DIR_OBJ		=	obj
DIR_LIB		=	lib
FILES_SRC	=	$(wildcard $(DIR_SRC)/*.java)
TMP			=	$(notdir $(FILES_SRC))
FILES_OBJ	=	$(addprefix $(DIR_OBJ)/,$(TMP:.java=.class))
FILES_LIB	=	$(wildcard $(DIR_LIB)/*.jar)
COMPILER 	=	javac
CLASSPATH	=	"$(DIR_SRC):$(FILES_LIB)"
JFLAGS 		=	-g -d $(DIR_OBJ)/ -cp $(CLASSPATH)


$(DIR_OBJ)/%.class: $(DIR_SRC)/%.java
	@echo -n "- compiling $<... "
	mkdir -p $(DIR_OBJ)
	@$(COMPILER) $(JFLAGS) $^
	@echo "done"

default: $(FILES_OBJ) $(headers)
	@echo "- compiling target $(target)"
	@echo -n "  "
	jar cvfm $(target).jar manifest.txt $^
	@echo "> COMPLETED: $(target) compiled."

.PHONY: clean
clean:
	@rm -rf $(DIR_OBJ)
	@rm -f $(target).jar
	@echo "> $(target) cleaned"
