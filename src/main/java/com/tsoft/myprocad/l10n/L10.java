package com.tsoft.myprocad.l10n;

import com.tsoft.myprocad.model.Application;

public class L10 {
    public static String get(String[] resources) {
        int index = Application.getInstance().getLanguage().ordinal();
        if (resources.length <= index) index = 0;
        return resources[index];
    }

    public static String get(String[] resources, Object ... params) {
        return String.format(get(resources), params);
    }

    public static String[] APPLICATION_NAME = { "My Pro CAD" };

    public static String[] MYPROCAD_FILES = { "MyProCAD Files (*.mpc)", "Файлы MyProCAD (*.mpc)" };
    public static String[] PDF_FILES = { "PDF Files (*.pdf)", "Файлы PDF (*.pdf)" };
    public static String[] OBJ_FILES = { "3D Model Files (*.obj)", "Файлы 3D-моделей (*.obj)" };

    public static String[] WINDOW_UNTITLED = { "Untitled", "Без имени" };
    public static String[] DIALOG_SAVE_BUTTON = { "Save", "Сохранить" };
    public static String[] DIALOG_CLOSE_BUTTON = { "Close", "Закрыть" };
    public static String[] DIALOG_ADD_BUTTON = { "Add", "Добавить" };
    public static String[] DIALOG_OK_BUTTON = { "OK", "Готово" };
    public static String[] DIALOG_CANCEL_BUTTON = { "Cancel", "Отмена" };
    public static String[] DIALOG_YES_BUTTON = { "Yes", "Да" };
    public static String[] DIALOG_NO_BUTTON = { "No", "Нет" };

    public static String[] MENU_NEW_PROJECT_NAME = { "New", "Создать" };
    public static String[] MENU_OPEN_PROJECT_NAME = { "Open", "Открыть" };
    public static String[] MENU_SAVE_PROJECT_NAME = { "Save", "Сохранить" };
    public static String[] MENU_SAVE_PROJECT_AS_NAME = { "Save as ...", "Сохранить как ..." };
    public static String[] MENU_CLOSE_PROJECT_NAME = { "Close", "Закрыть" };
    public static String[] MENU_SETTINGS_NAME = { "Settings", "Настройки" };
    public static String[] MENU_ABOUT_NAME = { "About ...", "О программе" };
    public static String[] MENU_EXIT_NAME = { "Exit", "Выход" };
    public static String[] MENU_HELP_NAME = { "Help", "Справка" };

    public static String[] MENU_CALCULATION_TRIANGLE = { "Triangle", "Треугольник" };
    public static String[] MENU_CALCULATION_RIGHT_TRIANGLE = { "Right Triangle", "Прямоугольный треугольник" };

    public static String[] MENU_MATERIALS_REPORT_NAME = { "Materials", "Материалы" };
    public static String[] MENU_ZOOM_OUT_NAME = { "Zoom out (Ctrl+-)", "Уменьшить (Ctrl+-)" };
    public static String[] MENU_ZOOM_OUT_HINT = { "Zoom out (Ctrl+-)", "Уменьшить (Ctrl+-)" };
    public static String[] MENU_ZOOM_IN_NAME = { "Zoom in (Ctrl++)", "Увеличить (Ctrl++)" };
    public static String[] MENU_ZOOM_IN_HINT = { "Zoom in (Ctrl++)", "Увеличить (Ctrl++)" };
    public static String[] MENU_CALCULATOR_HELP_NAME = { "Help", "Справка" };
    public static String[] MENU_CALCULATOR_HELP_HINT = { "Calculator Help", "Справка по калькулятору" };
    public static String[] MENU_CREATE_LABELS_NAME = { "Create a label", "Создать надпись" };
    public static String[] MENU_CREATE_LEVEL_MARKS_NAME = { "Create a level mark", "Создать отметку уровня" };
    public static String[] MENU_EDIT_FOLDERS_NAME = { "Folders", "Папки" };
    public static String[] MENU_EDIT_PROJECT_NAME = { "Project's items", "Состав проекта" };

    public static String[] MENU_CREATE_DIMENSION_LINES_NAME = { "Create s dimension", "Создать размер" };
    public static String[] MENU_CREATE_WALLS_NAME = { "Create a wall", "Создать стену" };
    public static String[] MENU_CREATE_BEAMS_NAME = { "Create a beam", "Создать балку" };
    public static String[] MENU_PAN_NAME = { "Pan", "Обзор" };
    public static String[] MENU_PAN_HINT = { "Pan in plan", "Обзор плана" };
    public static String[] MENU_SELECT_NAME = { "Select", "Выбрать" };
    public static String[] MENU_SELECT_HINT = { "Select an object on the plan", "Выбрать объект на плане" };
    public static String[] MENU_SELECT_ALL_NAME = { "Select all (Ctrl+A)", "Выделить все (Ctrl+A)" };
    public static String[] MENU_SELECT_WALLS_NAME = { "Select walls", "Выделить стены" };
    public static String[] MENU_SELECT_BY_MATERIAL_NAME = { "Select by material", "Выделить по материалу" };
    public static String[] MENU_SELECT_BY_PATTERN_NAME = { "Select by pattern", "Выделить по шаблону" };
    public static String[] MENU_ESCAPE_NAME = { "Escape", "Сброс" };
    public static String[] MENU_GENERATE_SCRIPT_NAME = { "Generate a script", "Сгенерировать скрипт" };
    public static String[] MENU_MOVE_SELECTION_LEFT_NAME = { "Move selection left", "Сдвинуть выделение влево" };
    public static String[] MENU_MOVE_SELECTION_UP_NAME = { "Move selection up", "Сдвинуть выделение вверх" };
    public static String[] MENU_MOVE_SELECTION_DOWN_NAME = { "Move selection down", "Сдвинуть выделение вниз" };
    public static String[] MENU_MOVE_SELECTION_RIGHT_NAME = { "Move selection right", "Сдвинуть выделение вправо" };
    public static String[] MENU_MOVE_SELECTION_FAST_LEFT_NAME = { "Move selection fast left", "Сдвинуть выделение больше влево" };
    public static String[] MENU_MOVE_SELECTION_FAST_UP_NAME = { "Move selection fast up", "Сдвинуть выделение больше вверх" };
    public static String[] MENU_MOVE_SELECTION_FAST_DOWN_NAME = { "Move selection fast down", "Сдвинуть выделение больше вниз" };
    public static String[] MENU_MOVE_SELECTION_FAST_RIGHT_NAME = { "Move selection fast right", "Сдвинуть выделение больше вправо" };
    public static String[] MENU_DELETE_NAME = { "Delete (Del)", "Удалить (Del)" };
    public static String[] MENU_DELETE_HINT = { "Delete selected objects (Del)", "Удалить выделенные объекты (Del)" };
    public static String[] MENU_COPY_NAME = { "Copy (Ctrl+C)", "Копировать (Ctrl+C)" };
    public static String[] MENU_COPY_HINT = { "Copy to Clipboard (Ctrl+C)", "Копировать в буфер обмена (Ctrl+C)" };
    public static String[] MENU_PASTE_NAME = { "Paste (Ctrl+V)", "Вставить (Ctrl+V)" };
    public static String[] MENU_PASTE_HINT = { "Paste from Clipboard (Ctrl+V)", "Вставить из буфера обмена (Ctrl+V)" };
    public static String[] MENU_CUT_NAME = { "Cut (Ctrl+X)", "Вырезать (Ctrl+X)" };
    public static String[] MENU_CUT_HINT = { "Cut to Clipboard (Ctrl+X)", "Вырезать в буфер обмена (Ctrl+X)" };
    public static String[] MENU_REDO_NAME = { "Redo", "Повторить" };
    public static String[] MENU_REDO_HINT = { "Redo last operation", "Повторить последнюю операцию" };
    public static String[] MENU_UNDO_NAME = { "Undo (Ctrl+Z)", "Отменить (Ctrl+Z)" };
    public static String[] MENU_UNDO_HINT = { "Undo last operation (Ctrl+Z)", "Отменить последнюю операцию (Ctrl+Z)" };

    public static String[] MENU_ROTATE_CLOCKWISE_NAME = { "Rotate Clockwise", "Повернуть по часовой стрелке" };
    public static String[] MENU_SPLIT_IN_TWO_NAME = { "Split in two", "Разбить на две" };

    public static String[] MENU_SHOW_PROJECT_IN_3D_NAME = { "Show in 3D", "Показать в 3D" };
    public static String[] MENU_SHOW_PLAN_IN_3D_NAME = { "Show in 3D", "Показать в 3D" };
    public static String[] MENU_PRINT_TO_PDF_NAME = { "Print to PDF", "Распечатать в PDF" };
    public static String[] MENU_MATERIALS_NAME = { "Materials", "Материалы" };
    public static String[] MENU_FIND_MATERIALS_USAGE_NAME = { "Find materials", "Найти материалы" };
    public static String[] MENU_PRINT_NAME = { "Print", "Печать" };
    public static String[] MENU_PRINT_PREVIEW_NAME = { "Print preview", "Предварительный просмотр" };
    public static String[] MENU_ADD_PLAN_NAME = { "Add plan", "Добавить план" };
    public static String[] MENU_DELETE_PLAN_NAME = { "Delete plan", "Удалить план" };
    public static String[] MENU_PLAN_EXPORT_TO_OBJ_NAME = { "Export to 3D Model", "Экспортировать в 3D-модель" };
    public static String[] MENU_ADD_NOTES_NAME = { "Add notes", "Добавить записи" };
    public static String[] MENU_DELETE_NOTES_NAME = { "Delete notes", "Удалить записи" };

    public static String[] FILE_MENU = { "File", "Файл" };
    public static String[] PROJECT_MENU = { "Project", "Проект" };
    public static String[] CALCULATION_MENU = { "Calculation", "Расчеты" };
    public static String[] PLAN_MENU = { "Plan", "План" };
    public static String[] SELECTION_MENU = { "Selection", "Выделение" };
    public static String[] NOTES_MENU = { "Notes", "Записи" };
    public static String[] REPORT_MENU = { "Reports", "Отчеты" };

    public static String[] OPEN_DIALOG = { "Open", "Открыть" };
    public static String[] SAVE_DIALOG = { "Save", "Сохранить" };
    public static String[] ERROR_DIALOG = { "Error", "Ошибка" };
    public static String[] MESSAGE_DIALOG = { "Message", "Сообщение" };
    public static String[] SELECT_DIALOG = { "Select", "Выбор" };
    public static String[] CONFIRM_DIALOG = { "Confirm", "Подтверждение" };

    public static String[] CONFIRM_OVERWRITE_MESSAGE = {
            "<html><b>File '%s' already exists." +
            "<br>Do you want to overwrite it ?</b></html>",

            "<html><b>Файл '%s' уже существует." +
            "<br>Перезаписать его ?</b></html>",
    };
    public static String[] CONFIRM_OVERWRITE_OVERWRITE = { "Overwrite", "Перезаписать" };
    public static String[] CONFIRM_OVERWRITE_CANCEL = { "Cancel", "Отмена" };
    public static String[] CONFIRM_SAVE_MESSAGE = {
            "<html><b>Do you want to save project %s ?</b>" +
            "<br><font size=\"-2\">Modifications will be lost if you don't save them.</html>",

            "<html><b>Вы хотите сохранить проект %s ?</b>" +
            "<br><font size=\"-2\">Все изменения будут потеряны, если проект не сохранить.</html>"
    };
    public static String[] CONFIRM_SAVE_TITLE = { "Save", "Сохранение" };
    public static String[] CONFIRM_SAVE_SAVE = { "Save", "Сохранить" };
    public static String[] CONFIRM_SAVE_DO_NOT_SAVE = { "Do not save", "Не сохранять" };
    public static String[] CONFIRM_SAVE_CANCEL = { "Cancel", "Отмена" };
    public static String[] CONFIRM_EXIT_MESSAGE = {
            "<html><b>Do you really really want to quit ?</b>" +
            "<br><font size=\"-2\">Modifications on unsaved project will be lost</html>",

            "<html><b>Вы действительно хотите выйти ?</b>" +
            "<br><font size=\"-2\">Все изменения в несохраненном проекте будут потеряны.</html>",
    };
    public static String[] CONFIRM_EXIT_TITLE = { "Exit" };
    public static String[] CONFIRM_EXIT_QUIT = { "Quit" };
    public static String[] CONFIRM_EXIT_DO_NOT_QUIT = { "Do not quit", "Не выходить" };

    public static String[] PROJECT_ALREADY_OPEN = { "Project '%s' is already open", "Проект '%s' уже открыт" };
    public static String[] PROJECT_CREATE_MESSAGE = { "Creating a project ...", "Создание проекта ..." };
    public static String[] PROJECT_OPEN_MESSAGE = { "Reading a project ...", "Чтение проекта ..." };
    public static String[] PROJECT_SAVE_MESSAGE = { "Saving the project ...", "Запись проекта ..." };

    public static String[] PRINT_MESSAGE = { "Printing ...", "Идет печать ..." };
    public static String[] PRINT_TO_PDF_MESSAGE = { "Printing to PDF ...", "Идет печать в PDF ..." };

    public static String[] PRINT_PREVIEW_TITLE  = { "Print preview", "Предварительный просмотр" };
    public static String[] PRINT_PREVIEW_SHOW_PREVIOUS_PAGE  = { "Show previous page", "Показать предыдущую страницу" };
    public static String[] PRINT_PREVIEW_SHOW_NEXT_PAGE  = { "Show next page", "Показать следующую страницу" };
    public static String[] PRINT_PREVIEW_PAGE_LABEL  = { "Page %1$d/%2$d", "Страница %1$d/%2$d" };
    public static String[] COMMAND_WINDOW  = { "Command Window", "Окно команд" };

    public static String[] PROPERTY_CANT_BE_EMPTY = { "Property can't be empty", "Свойство не может быть пустым" };
    public static String[] PROPERTY_MUST_BE_BETWEEN_VALUES = { "Property must be in [%d, %d]", "Свойство должно быть в интервале [%d, %d]" };
    public static String[] CANT_OPEN_PROJECT = { "Can't open the project. File '%s' is invalid or corrupted", "Невозможно открыть проект. Файл '%s' неверный или испорчен" };
    public static String[] CANT_SAVE_PROJECT = { "Can't save the project. Try to save into another location", "Невозможно сохранить проект. Попробуйте сохранить в другое место" };
    public static String[] CANT_OPEN_HELP_PAGE = { "Can't start the browser. Please, visit %s", "Не удалось запустить browser. Посетите %s" };

    public static String[] ABOUT_MESSAGE = {
          "<html><font face=\"sans-serif\"><center><font size=\"+2\"><b>MyProCAD</b></font> <sup>&reg;</sup>" +
          "<br><b>Version %1$s</b>" +
          "<br><font size=\"-1\">Java version %2$s</font>" +
          "<br><font size=\"-1\">PDF print provided by <a href=\"http://www.lowagie.com/iText/\">iText</a></font>" +
          "<p>Please, visit <a href=\"http:/myprocad.com/\">http://myprocad.com/</a>" +
          "<br>for software updates and bug report." +
          "</p><p><font size=\"-2\">© Copyrights 2013-2014 My Pro CAD" +
          "<br>All Rights Reserved</font></html>",

          "<html><font face=\"sans-serif\"><center><font size=\"+2\"><b>MyProCAD</b></font> <sup>&reg;</sup>" +
          "<br><b>Версия %1$s</b>" +
          "<br><font size=\"-1\">Версия Java %2$s</font>" +
          "<br><font size=\"-1\">Печать в PDF предоставлена <a href=\"http://www.lowagie.com/iText/\">iText</a></font>" +
          "<p>Посетите <a href=\"http:/myprocad.com/\">http://myprocad.com/</a>" +
          "<br>для обновления программы и сообщений об ошибках." +
          "</p><p><font size=\"-2\">© Авторские права принадлежат 2013-2014 My Pro CAD" +
          "<br>Все права зарезервированы</font></html>"
    };
    public static String[] ABOUT_TITLE = { "About the program", "О программе" };
    public static String[] SETTINGS_DIALOG_TITLE = { "Settings", "Настройки" };
    public static String[] THREADED_TASK_DIALOG_TITLE = { "Threaded Task", "Фоновый процесс" };

    public static String[] CALCULATOR_LABEL = { "Calculator: ", "Калькулятор: " };
    public static String[] CALCULATOR_ERROR = { "Error", "Ошибка" };

    /** Common */
    public static String[] CANCEL = { "Cancel", "Отмена" };
    public static String[] SELECT_ITEM = { "Select an item", "Выберите элемент" };
    public static String[] VIEW_CATEGORY = { "View", "Представление" };
    public static String[] J3D_CATEGORY = { "3D View", "3D Вид" };
    public static String[] PROPERTIES_CATEGORY = { "Properties", "Свойства" };
    public static String[] INFO_CATEGORY = { "Info", "Информация" };
    public static String[] X_CATEGORY = { "X Coordinate", "Координата X" };
    public static String[] Y_CATEGORY = { "Y Coordinate", "Координата Y" };
    public static String[] Z_CATEGORY = { "Z Coordinate", "Координата Z" };
    public static String[] START_PROPERTY = { "Start (mm)", "Начало (мм)" };
    public static String[] END_PROPERTY = { "End (mm)", "Конец (мм)" };
    public static String[] MIN_PROPERTY = { "Min (mm)", "Минимум (мм)" };
    public static String[] MAX_PROPERTY = { "Max (mm)", "Максимум (мм)" };
    public static String[] ROTATION_ANGLE_0 = { "0", "0" };
    public static String[] ROTATION_ANGLE_90 = { "90", "90" };
    public static String[] ROTATION_ANGLE_180 = { "180", "180" };
    public static String[] ROTATION_ANGLE_270 = { "270", "270" };
    public static String[] MOVE_PROPERTY = { "Move (mm)", "Переместить (мм)" };
    public static String[] SHIFT_PROPERTY = { "Shift (+/-mm)", "Сдвинуть (+/-мм)" };
    public static String[] DISTANCE_PROPERTY = { "Distance (mm)", "Расстояние (мм)" };
    public static String[] AREA_PROPERTY = { "Area (m2)", "Площадь (м2)" };
    public static String[] BACKGROUND_COLOR_PROPERTY = { "Background Color", "Цвет фона" };
    public static String[] FOREGROUND_COLOR_PROPERTY = { "Foreground Color", "Цвет линий" };
    public static String[] BORDER_COLOR_PROPERTY = { "Border Color", "Цвет границы" };
    public static String[] BORDER_WIDTH_PROPERTY = { "Border Width", "Толщина границы" };
    public static String[] SHOW_WIRED_PROPERTY = { "Show wired", "Показать каркас" };
    public static String[] KA_COLOR_PROPERTY = { "Ambient Color", "Фоновый свет" };
    public static String[] KD_COLOR_PROPERTY = { "Diffuse Color", "Рассеянный свет" };
    public static String[] KS_COLOR_PROPERTY = { "Specular Color", "Зеркальный свет" };
    public static String[] KE_COLOR_PROPERTY = { "Emissive Color", "Излучаемый свет" };
    public static String[] SHININESS_PROPERTY = { "Shininess", "Яркость" };
    public static String[] TRANSPARENCY_PROPERTY = { "Transparency", "Прозрачность" };
    public static String[] NUMBER_OF_ITEMS = { "%d item(s)", "%d элемент(а,ов)" };
    /** Status Panel */
    public static String[] STATUS_PANEL_X_LABEL = { "Mouse coordinates, X: ", "Координаты мыши, X: " };
    public static String[] STATUS_PANEL_Y_LABEL = { "Y: ", "Y: " };
    public static String[] STATUS_PANEL_MODE_LABEL = { "Mode: ", "Режим: " };
    public static String[] STATUS_PANEL_SELECTION_MODE = { "Selection", "Выбор" };
    public static String[] STATUS_PANEL_PANNING_MODE = { "Panning", "Обзор" };
    public static String[] STATUS_PANEL_WALL_CREATION_MODE = { "Wall Creation", "Создание стен" };
    public static String[] STATUS_PANEL_BEAM_CREATION_MODE = { "Beam Creation", "Создание балки" };
    public static String[] STATUS_PANEL_DIMENSION_LINE_MODE = { "Dimension Line Creation", "Создание размеров" };
    public static String[] STATUS_PANEL_LABEL_MODE = { "Label Creation", "Создание надписи" };
    public static String[] STATUS_PANEL_LEVEL_MARK_MODE = { "Level Mark Creation", "Создание метки уровня" };

    /** Buttons in dialogs */
    public static String[] MOVE_UP_BUTTON = { "Move Up", "Переместить выше" };
    public static String[] MOVE_DOWN_BUTTON = { "Move Down", "Переместить ниже" };
    public static String[] ADD_BUTTON = { "Add", "Добавить" };
    public static String[] DELETE_BUTTON = { "Delete", "Удалить" };
    public static String[] RENAME_BUTTON = { "Rename", "Переименовать" };

    /** Property Editors */
    public static String[] PLAN_PROPERTIES_INFO_MESSAGE = { "Plan", "План" };
    public static String[] WALL_PROPERTIES_INFO_MESSAGE = { "Wall", "Стена" };
    public static String[] BEAM_PROPERTIES_INFO_MESSAGE = { "Beam", "Балка" };
    public static String[] DIMENSION_LINE_PROPERTIES_INFO_MESSAGE = { "Dimension Line", "Размер" };
    public static String[] LABEL_PROPERTIES_INFO_MESSAGE = { "Label", "Надпись" };
    public static String[] LEVEL_MARK_PROPERTIES_INFO_MESSAGE = { "Selection", "Метка уровня" };
    public static String[] SELECTION_PROPERTIES_INFO_MESSAGE = { "Selection", "Выделение" };

    public static String[] APPLICATION_VIEW_CATEGORY = { "View", "Представление" };
    public static String[] APPLICATION_LANGUAGE_PROPERTY = { "Language", "Язык" };

    public static String[] PLAN_VIEW_CATEGORY = { "View", "Представление" };
    public static String[] PLAN_PASTE_CATEGORY = { "Paste", "Вставка из буфера" };
    public static String[] PLAN_PRINT_CATEGORY = { "Print", "Печать" };
    public static String[] PLAN_LIGHTS_PROPERTY = { "Lights", "Свет" };
    public static String[] PLAN_RULERS_PROPERTY = { "Rulers", "Рулетка" };
    public static String[] PLAN_GRID_PROPERTY = { "Grid", "Сетка" };
    public static String[] PLAN_SCALE_PROPERTY = { "Scale", "Масштаб" };
    public static String[] PLAN_NAME_PROPERTY = { "Name", "Имя" };
    public static String[] PLAN_LEVEL_PROPERTY = { "Level", "Уровень" };
    public static String[] PLAN_LEVEL_START_PROPERTY = { "Level Start (mm)", "Начало уровня (мм)" };
    public static String[] PLAN_LEVEL_END_PROPERTY = { "Level End (mm)", "Конец уровня (мм)" };
    public static String[] PLAN_CUSTOM_LEVEL_PROPERTY = { "Custom", "По умолчанию" };
    public static String[] PLAN_PASTE_OFFSET_X_PROPERTY = { "X Offset (mm)", "Смещение по Х (мм)" };
    public static String[] PLAN_PASTE_OFFSET_Y_PROPERTY = { "Y Offset (mm)", "Смещение по Y (мм)" };
    public static String[] PLAN_PASTE_OFFSET_Z_PROPERTY = { "Z Offset (mm)", "Смещение по Z (мм)" };
    public static String[] PLAN_PASTE_OPERATION_PROPERTY = { "Paste Operation", "Операция вставки"};
    public static String[] PLAN_PRINT_PAPER_ORIENTATION_PROPERTY = { "Paper Orientation", "Расположение листа" };
    public static String[] PLAN_PRINT_PAPER_SIZE_PROPERTY = { "Paper Size", "Размер бумаги" };
    public static String[] PLAN_PRINT_PAPER_WIDTH_PROPERTY = { "Paper Width", "Ширина листа" };
    public static String[] PLAN_PRINT_PAPER_HEIGHT_PROPERTY = { "Paper Height", "Высота листа" };
    public static String[] PLAN_PRINT_PAPER_TOP_MARGIN_PROPERTY = { "Top Margin", "Верхний отступ" };
    public static String[] PLAN_PRINT_PAPER_LEFT_MARGIN_PROPERTY = { "Left Margin", "Левый отступ" };
    public static String[] PLAN_PRINT_PAPER_RIGHT_MARGIN_PROPERTY = { "Right Margin", "Правый отступ" };
    public static String[] PLAN_PRINT_PAPER_BOTTOM_MARGIN_PROPERTY = { "Bottom Margin", "Нижний отступ" };
    public static String[] PLAN_PRINT_RULERS_PROPERTY = { "Print Rulers", "Печать рулетки" };
    public static String[] PLAN_PRINT_GRID_PROPERTY = { "Print Grid", "Печать сетки" };
    public static String[] PLAN_PRINT_SCALE_PROPERTY = { "Print Scale", "Масштаб печати" };

    public static String[] WALL_SHAPE_PROPERTY = { "Shape", "Фигура" };
    public static String[] WALL_DIAGONAL_WIDTH_PROPERTY = { "Diagonal Width (mm)", "Диагональная ширина (мм)" };
    public static String[] WALL_PATTERN_PROPERTY = { "Pattern", "Шаблон" };
    public static String[] WALL_ALWAYS_SHOW_BORDERS_PROPERTY = { "Always show borders", "Всегда показывать границы" };
    public static String[] WALL_MATERIAL_PROPERTY = { "Material", "Материал" };
    public static String[] MATERIAL_DENSITY_PROPERTY = { "Density (tn/m3)", "Удельный вес (тн/м3)" };
    public static String[] WALL_SKIP_IN_REPORTS_PROPERTY = { "Skip in reports", "Пропускать в отчетах" };
    public static String[] WALL_SIZE_PROPERTY = { "%dx%dx%dx (LWH)", "%dx%dx%dx (ДШВ)" };
    public static String[] WALL_OUTER_LENGTH_PROPERTY = { "Outer length (m)", "Внешняя длина (м)" };
    public static String[] VOLUME_PROPERTY = { "Volume (m3)", "Объем (м3)" };
    public static String[] WEIGHT_PROPERTY = { "Weight (tn)", "Вес (тн)" };
    public static String[] PRICE_PROPERTY = { "Price", "Цена" };

    public static String[] BEAM_XOZ_ANGLE_PROPERTY = { "Angle XoZ", "Угол XoZ" };
    public static String[] BEAM_XOY_ANGLE_PROPERTY = { "Angle XoY", "Угол XoY" };
    public static String[] BEAM_YOZ_ANGLE_PROPERTY = { "Angle YoZ", "Угол YoZ" };
    public static String[] BEAM_WIDTH_PROPERTY = { "Width (mm)", "Ширина (мм)" };
    public static String[] BEAM_HEIGHT_PROPERTY = { "Height (mm)", "Высота (мм)" };

    public static String[] DIMENSION_LINE_COLOR_PROPERTY = { "Line Color", "Цвет линии" };
    public static String[] DIMENSION_LINE_WIDTH_PROPERTY = { "Line Width", "Толщина линии" };
    public static String[] DIMENSION_TEXT_PROPERTY = { "Text (empty for default)", "Тест (пусто по умолчанию)" };
    public static String[] DIMENSION_OFFSET_PROPERTY = { "Offset (mm)", "Смещение (мм)" };
    public static String[] DIMENSION_FONT_FAMILY_PROPERTY = { "Font Family", "Семейство шрифтов" };
    public static String[] DIMENSION_FONT_SIZE_PROPERTY = { "Font Size", "Размер шрифта" };
    public static String[] DIMENSION_START_POINT_SHAPE_TYPE_PROPERTY = { "Start Point", "Начальная точка" };
    public static String[] DIMENSION_END_POINT_SHAPE_TYPE_PROPERTY = { "End Point", "Конечная точка" };
    public static String[] LENGTH_PROPERTY = { "Length (mm)", "Длина (мм)" };
    public static String[] DIMENSION_LINE_ANGLE_PROPERTY = { "Angle (degrees)", "Угол (градусы)" };

    public static String[] LABEL_ROTATION_PROPERTY = { "Rotation", "Поворот" };
    public static String[] LABEL_TEXT_PROPERTY = { "Text", "Текст" };
    public static String[] LABEL_FONT_FAMILY_PROPERTY = { "Font Family", "Семейство шрифтов" };
    public static String[] LABEL_FONT_SIZE_PROPERTY = { "Font Size", "Размер шрифта" };

    public static String[] LEVEL_MARK_COORDINATES_CATEGORY = { "Coordinates", "Координаты" };
    public static String[] LEVEL_MARK_X_PROPERTY = { "X (mm)", "X (мм)" };
    public static String[] LEVEL_MARK_Y_PROPERTY = { "Y (mm)", "Y (мм)" };
    public static String[] LEVEL_MARK_Z_START_PROPERTY = { "Z Start (mm)", "Начало по Z (мм)" };
    public static String[] LEVEL_MARK_Z_END_PROPERTY = { "Z End (mm)", "Конец по Z (мм)" };
    public static String[] LEVEL_MARK_ROTATED_PROPERTY = { "Rotated", "Повернуть" };
    public static String[] LEVEL_MARK_TEXT_PROPERTY = { "Text", "Текст" };
    public static String[] LEVEL_MARK_FONT_FAMILY_PROPERTY = { "Font Family", "Семейство шрифтов" };
    public static String[] LEVEL_MARK_FONT_SIZE_PROPERTY = { "Font Size", "Размер шрифта" };

    public static String[] SELECTION_X_CATEGORY = { "X Coordinate", "Координата X" };
    public static String[] SELECTION_Y_CATEGORY = { "Y Coordinate", "Координата Y" };
    public static String[] SELECTION_Z_CATEGORY = { "Z Coordinate", "Координата Z" };
    public static String[] SELECTION_INFO_CATEGORY = { "Info", "Информация" };
    public static String[] SELECTION_X_MIN_PROPERTY = { "X min (mm)", "Минимум X (мм)" };
    public static String[] SELECTION_X_MAX_PROPERTY = { "X max (mm)", "Максимум X (мм)" };
    public static String[] SELECTION_MOVE_X_PROPERTY = { "Move X (mm)", "Установить X (мм)" };
    public static String[] SELECTION_MOVE_DX_PROPERTY = { "Move DX (+/-mm)", "Сдвинуть X (+/-мм)" };
    public static String[] SELECTION_Y_MIN_PROPERTY = { "Y min (mm)", "Минимум Y (мм)" };
    public static String[] SELECTION_Y_MAX_PROPERTY = { "Y max (mm)", "Максимум Y (мм)" };
    public static String[] SELECTION_MOVE_Y_PROPERTY = { "Move Y (mm)", "Установить Y (мм)" };
    public static String[] SELECTION_MOVE_DY_PROPERTY = { "Move DY (+/-mm)", "Сдвинуть Y (+/-мм)" };
    public static String[] SELECTION_Z_MIN_PROPERTY = { "Z min (mm)", "Минимум Z (мм)" };
    public static String[] SELECTION_Z_MAX_PROPERTY = { "Z max (mm)", "Максимум Z (мм)" };
    public static String[] SELECTION_SET_Z_START_PROPERTY = { "Set Z Start (mm)", "Установить начало по Z (мм)" };
    public static String[] SELECTION_SET_Z_END_PROPERTY = { "Set Z End (mm)", "Установить конец по Z (мм)" };
    public static String[] SELECTION_MOVE_Z_PROPERTY = { "Move Z (mm)", "Установить Z (мм)" };
    public static String[] SELECTION_MOVE_DZ_PROPERTY = { "Move DZ (+/-mm)", "Сдвинуть Z (+/-мм)" };
    public static String[] SELECTION_AMOUNT_PROPERTY = { "Selected (items)", "Выбрано (объектов)" };

    public static String[] LIGHT_TYPE = { "Light Type", "Тип света" };
    public static String[] LIGHT_COLOR = { "Color", "Цвет" };
    public static String[] LIGHT_CX = { "Center X", "Центр X" };
    public static String[] LIGHT_CY = { "Center Y", "Центр Y" };
    public static String[] LIGHT_CZ = { "Center Z", "Центр Z" };
    public static String[] LIGHT_DX = { "Direction X", "Направление X" };
    public static String[] LIGHT_DY = { "Direction Y", "Направление Y" };
    public static String[] LIGHT_DZ = { "Direction Z", "Направление Z" };

    /** Calculation */
    public static String[] CALCULATION_DIALOG_TITLE = { "Calculations", "Расчеты" };
    public static String[] CALCULATION_PARAMETERS_CATEGORY = { "Mechanics", "Нагрузки" };
    public static String[] CALCULATION_A_LEG_PROPERTY = { "a Leg (mm)", "Сторона a (мм)" };
    public static String[] CALCULATION_B_LEG_PROPERTY = { "b Leg (mm)", "Сторона b (мм)" };
    public static String[] CALCULATION_C_LEG_PROPERTY = { "c Leg (mm)", "Сторона c (мм)" };
    public static String[] CALCULATION_C_HYPOTENUSE_PROPERTY = { "c Hypotenuse (mm)", "Гипотенуза c (мм)" };
    public static String[] CALCULATION_A_ANGLE_PROPERTY = { "A Angle (degrees)", "Угол A (градусы)" };
    public static String[] CALCULATION_B_ANGLE_PROPERTY = { "B Angle (degrees)", "Угол B (градусы)" };
    public static String[] CALCULATION_C_ANGLE_PROPERTY = { "C Angle (degrees)", "Угол C (градусы)" };
    public static String[] CALCULATION_AREA_PROPERTY = { "Area (mm2)", "Площадь (мм2)" };
    public static String[] CALCULATION_AREA_M2_PROPERTY = { "Area (m2)", "Площадь (м2)" };
    public static String[] CALCULATION_PERIMETER_PROPERTY = { "Perimeter (mm)", "Периметр (мм)" };
    public static String[] CALCULATION_PROPERTY = { "Calculation", "Расчет" };
    public static String[] VIEW_VALUE = { "View ...", "Открыть ..." };

    public static String[] WOOD_BEAM_MENU = { "Wood Beam", "Деревянная балка" };
    public static String[] MENU_ADD_WOOD_BEAM_NAME = { "Add Wood Beam", "Добавить деревянную балку" };
    public static String[] MENU_DELETE_WOOD_BEAM_NAME = { "Delete Wood Beam", "Удалить деревянную балку" };

    /* Beam */
    public static String[] CALCULATION_BEAM_LEFT_SUPPORT_PROPERTY = { "Left Support (m)", "Левая опора (м)" };
    public static String[] CALCULATION_BEAM_RIGHT_SUPPORT_PROPERTY = { "Right Support (m)", "Правая опора (м)" };
    public static String[] CALCULATION_BEAM_ELASTIC_STRENGTH_PROPERTY = { "Elastic Strength, E (MPa)", "Модуль упругости E (МПа)" };
    public static String[] CALCULATION_BEAM_ALLOWABLE_STRESS_PROPERTY = { "Allowable Stress, [σ] (MPa)", "Допускаемое напряжение [σ] (МПа)" };
    public static String[] CALCULATION_BEAM_BENDING_MOMENTS_PROPERTY = { "Bending moments, M (kNm)", "Изгибающие моменты M (кНм)" };
    public static String[] CALCULATION_BEAM_FORCES_PROPERTY = { "Forces, F (kN)", "Сосредоточенные силы F (кН)" };
    public static String[] CALCULATION_BEAM_DISTRIBUTED_FORCES_PROPERTY = { "Distributed Forces, g (kN/m)", "Распределенные нагрузки, g (кН/м)" };
    public static String[] CALCULATION_BEAM_B_PROPERTY = { "B, mm", "Расстояние между осями балок, мм" };
    public static String[] CALCULATION_BEAM_PERMANENT_LOAD_PROPERTY = { "Permanent load", "Постоянная нагрузка" };
    public static String[] CALCULATION_BEAM_TEMPORARY_LOAD_PROPERTY = { "Temporary load", "Временная нагрузка" };
    public static String[] FILL_MECHANICS_PROPERTIES = { "Add load on the beam", "Введите нагрузку на балку" };

    public static String[] CALCULATION_BEAM_MOMENTS_TABLE_COLUMN_VM = { "Bending moment, M (kNm)", "Изгибающий момент M (кНм)" };
    public static String[] CALCULATION_BEAM_MOMENTS_TABLE_COLUMN_ZM = { "Distance, L (m)", "Приложение (м)" };

    public static String[] CALCULATION_BEAM_FORCES_TABLE_COLUMN_VS = { "Force, F (kN)", "Сосредоточенная сила F (кН)" };
    public static String[] CALCULATION_BEAM_FORCES_TABLE_COLUMN_ZS = { "Distance, L (m)", "Приложение (м)" };

    public static String[] CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Q1 = { "Force, F (kN)", "<html><center>Сосредоточенная<br>сила F (кН)</center>" };
    public static String[] CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Z1 = { "Distance, L (m)", "Приложение (м)" };
    public static String[] CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Q2 = { "Force, F (kN)", "<html><center>Сосредоточенная сила F (кН)</center>" };
    public static String[] CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Z2 = { "Distance, L (m)", "Приложение (м)" };

    public static String[] CALCULATION_BEAM_PERMANENT_LOAD_TABLE_COLUMN_NAME = { "Name", "<html><center>Название</center>" };
    public static String[] CALCULATION_BEAM_PERMANENT_LOAD_TABLE_COLUMN_DENSITY = { "Density", "<html><center>Плотность</center>" };
    public static String[] CALCULATION_BEAM_PERMANENT_LOAD_TABLE_COLUMN_H = { "Height, m", "<html><center>Высота слоя, м</center>" };

    public static String[] CALCULATION_BEAM_TEMPORARY_LOAD_TABLE_COLUMN_NAME = { "Name", "<html><center>Название</center>" };
    public static String[] CALCULATION_BEAM_TEMPORARY_LOAD_TABLE_COLUMN_VALUE = { "Load, kg/m2", "<html><center>Нагрузка, кгс/м2</center>" };

    public static String[] CALCULATION_BEAM_ERROR_FORCE_IS_ZERO = { "Row %d. Zero force must not be specified", "Строка %d. Не нужно задавать нулевую силу" };
    public static String[] CALCULATION_BEAM_ERROR_FORCE_IS_OUT = { "Row %d. The force must be on the beam", "Строка %d. Сила должна быть на балке" };

    public static String[] CALCULATION_BEAM_COORDS_FIG = { "Fig. 1 Beam's coordinate system", "Рис. 1 Система координат балки" };
    public static String[] CALCULATION_BEAM_ABOUT_FIG = { "Fig. 2 A beam", "Рис. 2 Балка" };
    public static String[] CALCULATION_BEAM_BEAM_FIG = { "Fig. 3 Beam's loading", "Рис. 3 Балка и нагрузка на неё" };
    public static String[] CALCULATION_BEAM_SHEARING_FORCES_FIG = { "Fig. 4 Shearing forces", "Рис. 4 Эпюра перерезывающих сил" };
    public static String[] CALCULATION_BEAM_BENDING_MOMENTS_FIG = { "Fig. 5 Bending moments", "Рис. 5 Эпюра изгибающих моментов" };
    public static String[] CALCULATION_BEAM_NAPR_FIG = { "Fig. 6 Napr", "Рис. 6 Распределение напряжений в опасном сечении" };
    public static String[] CALCULATION_BEAM_ROTATIONS_FIG = { "Fig. 7 Rotation angles", "Рис. 7 Эпюра углов поворота" };
    public static String[] CALCULATION_BEAM_MOVEMENTS_FIG = { "Fig. 8 Movements", "рис. 8 Эпюра прогибов (перемещений)" };

    /* Wood Beam */
    public static String[] CALCULATION_WOOD_BEAM_L_PROPERTY = { "Length, mm", "Длина пролета, мм" };
    public static String[] CALCULATION_WOOD_BEAM_B_PROPERTY = { "Distance between the beams, mm", "Расстояние между балками, мм)" };
    public static String[] CALCULATION_WOOD_BEAM_PERMANENT_LOAD_PROPERTY = { "Permanent Load", "Постоянная нагрузка" };
    public static String[] CALCULATION_WOOD_BEAM_TEMPORARY_LOAD_PROPERTY = { "Temporary Load", "Временная нагрузка" };
    public static String[] CALCULATION_WOOD_BEAM_SAG_PROPERTY = { "Max Beam's Sag", "Макс. допустимый прогиб" };
    public static String[] CALCULATION_WOOD_CALC_ALL_PROPERTY = { "Calculate for all beams sizes", "Рассчитать по всем типоразмерам" };

    public static String[] CALCULATION_WOOD_BEAM_ERROR_EMPTY_NAME = { "Row %d. Name can't be empty", "Строка %d. Укажите имя" };
    public static String[] CALCULATION_WOOD_BEAM_ERROR_INVALID_DENSITY = { "Row %d. Density can be [1..3000] kgs/m3", "Строка %d. Плотность должна быть в диапазоне [1..3000] кгс/м3" };
    public static String[] CALCULATION_WOOD_BEAM_ERROR_INVALID_H = { "Row %d. Material's layer can be [0.001..3] m", "Строка %d. Толщина слоя материала должна быть в диапазоне [0.001..3] м" };

    /** Level */
    public static String[] EDIT_LEVELS_TITLE = { "Levels", "Уровни" };
    public static String[] LEVEL_AT_LEAST_ONE_MUST_EXIST = { "At least one level must exist", "Должен быть хотя бы один уровень " };
    public static String[] LEVEL_NAME_CANT_BE_EMPTY = { "Row %d. Level's name can't be empty", "Строка %d. Имя уровня не может быть пустым" };
    public static String[] LEVEL_INVALID_Z_COORD = { "Row %d. Z-Coordinate must be in [%d, %d]", "Строка %d. Координата Z должна быть в диапазоне [%d, %d]" };
    public static String[] LEVEL_ALREADY_EXISTS = { "Row %d. Level already exists", "Строка %d. Уровень уже существует" };
    public static String[] CANT_REMOVE_LEVEL = { "Row %d. Can't remove %s as it contains %d item(s)", "Строка %d. Нельзя удалить уровень если он содержит элемент(ы)" };
    public static String[] CONFIRM_LEVEL_REMOVAL = { "Remove %s ?", "Удалить %s ?" };

    public static String[] LEVEL_COLUMN_NAME = { "Name", "Название" };
    public static String[] LEVEL_COLUMN_START = { "Start (mm)", "Начало (мм)" };
    public static String[] LEVEL_COLUMN_END = { "End (mm)", "Конец (мм)" };

    /** Materials */
    public static String[] MATERIAL_UNIT_M = { "m", "м" };
    public static String[] MATERIAL_UNIT_M2 = { "m2", "м2" };
    public static String[] MATERIAL_UNIT_M3 = { "m3", "м3" };

    public static String[] CONFIRM_MATERIAL_REMOVAL = { "Remove %s ?", "Удалить %s ?" };

    public static String[] MATERIAL_UNKNOWN = { "Unknown", "Неопределен" };
    public static String[] FIND_BY_MATERIAL_NAME = { "Material's name", "Имя материала" };
    public static String[] SELECT_PLANS = { "Select plans", "Выберите планы" };
    public static String[] PATTERN_NAME = { "Pattern", "Шаблон" };
    public static String[] FIND_BY_PATTERN = { "Wall's pattern", "Шаблон стены" };
    public static String[] MATERIAL_NAME_CANT_BE_EMPTY = { "Row %d. Material's name can't be empty", "Строка %d. Название материала не может быть пустым" };
    public static String[] MATERIAL_ALREADY_EXISTS = { "Row %d. Material already exists", "Строка %d. Материал уже существует" };
    public static String[] CANT_REMOVE_MATERIAL = { "Can not remove material \"%s\" as it is used in plan(s): %s\n. Replace it first with another material.", "Нельзя удалить материал \"%s\", так как он используется на планах: %s\n. Сначала замените его другим материалом." };
    public static String[] CANT_REMOVE_DEFAULT_MATERIAL = { "Can not remove material \"%s\" as it is the default one. You can rename it.", "Нельзя удалить материал \"%s\", т.к. это материал по умолчанию. Однако, его можно переименовать" };

    public static String[] SELECT_MATERIAL_REPORT = { "Select report", "Выберите отчет" };
    public static String[] MATERIAL_REPORT_PROJECT = { "Totals for project", "Итого по проекту" };
    public static String[] MATERIAL_REPORT_GROUP_BY_LEVEL = { "Totals for project and for each level", "Итог по проекту и по каждому уровню" };
    public static String[] MATERIAL_REPORT_LEVEL = { "Current level", "Выбранный уровень" };
    public static String[] MATERIAL_REPORT_LEVEL_TOTAL = { "Totals for the current level", "Итог для выбранного уровня" };

    public static String[] MATERIAL_REPORT_MATERIAL_COLUMN = { "Material", "Материал" };
    public static String[] MATERIAL_REPORT_VOLUME_COLUMN = { "Volume,m3", "Объем, м3" };
    public static String[] MATERIAL_REPORT_WEIGHT_COLUMN = { "Weight,tn", "Вес, тн" };
    public static String[] MATERIAL_REPORT_AMOUNT_COLUMN = { "Amount", "Кол-во" };
    public static String[] MATERIAL_REPORT_PRICE_COLUMN = { "Price, 1m3", "Цена, 1м3" };
    public static String[] MATERIAL_REPORT_COST_COLUMN = { "Cost", "Стоимость" };

    /** Materials Editor */
    public static String[] EDIT_MATERIALS_TITLE = { "Materials", "Материалы" };
    public static String[] MATERIAL_COLUMN_NAME = { "Name", "Название" };
    public static String[] MATERIAL_COLUMN_SG = { "Gravity (tn/m3)", "Удельный вес (тн/м3)" };
    public static String[] MATERIAL_COLUMN_PRICE = { "Price (per m3)", "Цена (за м3)" };
    public static String[] MATERIAL_COLUMN_UNIT = { "Unit", "Ед. изм." };

    /** Items */
    public static String[] DIMENSION_LINE_TYPE_NAME = { "Dimension Line", "Размер" };
    public static String[] LABEL_TYPE_NAME = { "Label", "Метка" };
    public static String[] LEVEL_MARK_TYPE_NAME = { "Level mark", "Метка уровня" };
    public static String[] PROJECT_ITEM_NAME = { "Name", "Имя" };
    public static String[] PROJECT_ITEM_TYPE = { "Type", "Тип" };
    public static String[] PROJECT_ITEM_NOTES = { "Notes", "Записки" };
    public static String[] PROJECT_ITEM_PLAN = { "Plan", "План" };
    public static String[] PROJECT_ITEM_BEAM = { "Beam", "Балка" };
    public static String[] PROJECT_ITEM_WOOD_BEAM = { "Wood Beam", "Деревянная балка" };
    public static String[] INPUT_FOLDER_NAME = { "Folder", "Папка" };
    public static String[] DEFAULT_FOLDER_NAME = { "Default", "Общее" };
    public static String[] EDIT_FOLDERS_TITLE = { "Edit Folders", "Изменить папки" };
    public static String[] FOLDER_LABEL = { "Folder", "Папка" };
    public static String[] EDIT_PROJECT_TITLE = { "Project Items", "Состав проекта" };
    public static String[] ITEM_ALREADY_EXISTS = { "Item '%s' already exists", "Элемент '%s' уже существует" };
    public static String[] CANT_REMOVE_THE_ONLY_PROJECT_ITEM = { "Can't remove the only project item", "Нельзя удалить последний элемент проекта" };
    public static String[] CONFIRM_ITEM_REMOVAL = { "Remove %s ?", "Удалить %s ?" };
    public static String[] ADD_ITEM = { "Add Item", "Добавить" };
    public static String[] NEW_LABEL_TEXT = { "Text", "Текст" };

    public static String[] ITEM_INVALID_COORDINATE = { "Item's coordinate must be in [%d, %d]", "Координата элемента должна быть в интервале [%d, %d]" };
    public static String[] ITEM_INVALID_DIAGONAL_WIDTH = { "Diagonal width must be positive and within the rectangle", "Ширина по диагонали д.б. положительна и вписываться в прямоугольник" };
    public static String[] ITEM_INVALID_INTEGER_PROPERTY = { "The value must be in [%d, %d]", "Значение должно быть в диапазоне [%d, %d]" };
    public static String[] ITEM_INVALID_FLOAT_PROPERTY = { "The value must be in [%f, %f]", "Значение должно быть в диапазоне [%f, %f]" };

    /** Plan Panel hint */
    public static String[] MOUSE_LOCATION_HINT1 = { "%dcm", "%dсм" };
    public static String[] MOUSE_LOCATION_HINT2 = { "%dm %dcm", "%dм %dсм" };

    /** Folders */
    public static String[] CREATE_FOLDER = { "Folder name:", "Имя папки:" };
    public static String[] RENAME_FOLDER = { "Rename", "Переименовать" };
    public static String[] FOLDER_ALREADY_EXISTS = { "Folder '%s' already exists", "Папка '%s' уже существует" };
    public static String[] CANT_REMOVE_NON_EMPTY_FOLDER = { "Can't remove non-empty folder. Remove it's items first", "Нельзя удалить не пустую папку. Сначала удалите все ее элементы" };
    public static String[] CANT_REMOVE_THE_ONLY_FOLDER = { "Can't remove the only folder", "Нельзя удалить последнюю папку" };

    /** Plans */
    public static String[] DEFAULT_PLAN_NAME = { "My Plan", "Мой план" };

    public static String[] PASTE_OPERATION_MOVE_TO = { "As Is", "Как есть" };
    public static String[] PASTE_OPERATION_MOVE_TO_LEVEL = { "Place on Level", "Поместить на уровень" };
    public static String[] PASTE_OPERATION_PLUS_OFFSET = { "Plus Offsets", "Плюс смещения" };
    public static String[] PASTE_OPERATION_FIRST_SELECTED_PLUS_OFFSET = { "Plus Offsets to Selection", "Плюс смещения от выделенного" };
    public static String[] PAPER_ORIENTATION_PORTRAIT = { "Portrait", "Портрет" };
    public static String[] PAPER_ORIENTATION_LANDSCAPE = { "Landscape", "Альбом" };
    public static String[] PAPER_ORIENTATION_REVERSE_LANDSCAPE = { "Reverse Landscape", "Обратный альбом" };
    public static String[] PAPER_SIZE_A4 = { "A4", "A4" };
    public static String[] PAPER_SIZE_A3 = { "A3", "A3" };
    public static String[] PAPER_SIZE_A2 = { "A2", "A2" };
    public static String[] PAPER_SIZE_CUSTOM = { "Custom", "Другой" };
    public static String[] PAPER_PRINT_SCALE_ONE_PAGE = { "One Page", "На страницу" };
    public static String[] PAPER_PRINT_SCALE_ONE_TO_2 = { "1 to 2", "1 к 2" };
    public static String[] PAPER_PRINT_SCALE_ONE_TO_ONE = { "1 to 1", "1 к 1" };
    public static String[] PAPER_PRINT_SCALE_2_TO_ONE = { "2 to 1", "2 к 1" };

    /** Reports */
    public static String[] MATERIAL_REPORT_GRAND_TOTAL = { "GRAND TOTAL", "ИТОГО" };

    /** Wall Shapes */
    public static String[] WALL_SHAPE_RECTANGLE = { "Rectangle", "Прямоугольник" };
    public static String[] WALL_SHAPE_DIAGONAL1 = { "Diagonal 1", "Диагональ 1" };
    public static String[] WALL_SHAPE_DIAGONAL1U = { "Diagonal 1u", "Диагональ 1в" };
    public static String[] WALL_SHAPE_DIAGONAL1D = { "Diagonal 1d", "Диагональ 1н" };
    public static String[] WALL_SHAPE_DIAGONAL2 = { "Diagonal 2", "Диагональ 2" };
    public static String[] WALL_SHAPE_DIAGONAL2U = { "Diagonal 2u", "Диагональ 2в" };
    public static String[] WALL_SHAPE_DIAGONAL2D = { "Diagonal 2d", "Диагональ 2н" };
    public static String[] WALL_SHAPE_TRIANGLE1 = { "Triangle 1", "Треугольник 1" };
    public static String[] WALL_SHAPE_TRIANGLE2 = { "Triangle 2", "Треугольник 2" };
    public static String[] WALL_SHAPE_TRIANGLE3 = { "Triangle 3", "Треугольник 3" };
    public static String[] WALL_SHAPE_TRIANGLE4 = { "Triangle 4", "Треугольник 4" };
    public static String[] WALL_SHAPE_CIRCLE = { "Circle", "Круг" };

    /** Calculator */
    public static String[] CALCULATOR_HELP_TEXT = {
            "<html><b>Calculator Help</b>\n" +
            "Operations:\n" +
            "  +, -, *, / Arithmetic operations. Example: 2 + 2\n" +
            "  ^ Power. Example: 2^(1+2)\n" +
            "Functions:\n" +
            "  sin(degrees)\n" +
            "  cos(degrees)\n" +
            "  tan(degrees)\n" +
            "  sqrt(degrees) Square Root. Example: sqrt(4)\n" +
            "  qe(a, b, c) Quadratic Equation. Example: qe(1, 0, 0)\n" +
            "Constants\n" +
            "  pi" };
    public static String[] EXECUTE_JAVA_SCRIPT = { "Execute Script", "Выполнить скрипт" };
    public static String[] CLEAR_JAVA_SCRIPT_OUTPUT = { "Clear Output", "Очистить результат" };
    public static String[] HIDE_JAVA_SCRIPT_PANEL = { "X", "X" };
}
